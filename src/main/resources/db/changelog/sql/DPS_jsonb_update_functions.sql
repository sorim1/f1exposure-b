--transforms array into jsonpath that returns true if path value is equal to conditionValue
create or replace function array_to_jsonpath(path text[], conditionValue jsonb)
    returns jsonpath
    language plpgsql
as
$$
declare
    str_path text := '$';
    i integer;
begin
    if array_length(path, 1) > 0 then
        for i in 1 .. array_upper(path, 1) loop
                str_path = str_path || '.'|| path[i];
            end loop;
    end if;
    if (conditionValue is not null) then
        str_path = str_path || ' ? (@ == ' || conditionValue || ')';
    end if;
    return str_path::jsonpath;
end;
$$;

--usage example for array_to_jsonpath
--select array_to_jsonpath('{"a", "d"}'::text[], '1'::jsonb);
--$."a"."d"?(@ == 1)
--select array_to_jsonpath('{"a", "d"}'::text[], null);
--$."a"."d"
--select array_to_jsonpath('{}'::text[], null);
--$

-- updates jsonb path with newValue if existing value is oldValue or oldValue is null
create or replace function conditional_jsonb_set(input jsonb, path text[], oldValue jsonb, newValue jsonb)
    returns jsonb
    language plpgsql
as
$$
begin
    if array_length(path, 1) > 0 then
        if (jsonb_path_query_first(input, array_to_jsonpath(path, null)) @> oldValue or input @? array_to_jsonpath(path, oldValue)) then
            return jsonb_set(input, path, newValue);
        else
            return input;
        end if;
    else
        if (jsonb_path_query_first(input, array_to_jsonpath(path, null)) @> oldValue) then
            return newValue;
        else
            return input;
        end if;

    end if;
end;
$$;

--usage example for conditional_jsonb_set
--select conditional_jsonb_set('{"a": {"c" : 1, "d":2}, "b": {"e" : 3, "f":4}}'::jsonb, '{"a", "d"}'::text[], '2'::jsonb, '6'::jsonb);
--{"a": {"c": 1, "d": 6}, "b": {"e": 3, "f": 4}}
--select conditional_jsonb_set('{"a": {"c" : 1, "d":2}, "b": {"e" : 3, "f":4}}'::jsonb, '{"a", "d"}'::text[], null, '6'::jsonb);
--{"a": {"c": 1, "d": 6}, "b": {"e": 3, "f": 4}}
--select conditional_jsonb_set('{"a": {"c" : 1, "d":3}, "b": {"e" : 3}}'::jsonb, '{"a"}'::text[], '{"c" : 1, "d":3}'::jsonb, '{"x" : 98, "y":99}'::jsonb);
--{"a": {"x": 98, "y": 99}, "b": {"e": 3}}
--select conditional_jsonb_set('{"c" : 1, "d":3}'::jsonb, '{}'::text[], '{"c" : 1, "d":3}'::jsonb, '{"x" : 98, "y":99}'::jsonb);
--{"x": 98, "y": 99}

-- updates path in the target array items with newValue if existing value is oldValue
CREATE OR REPLACE FUNCTION update_array_elements(target jsonb, path text[], oldValue jsonb, newValue jsonb)
    RETURNS jsonb language sql AS
$$
SELECT jsonb_agg(updated_jsonb)
       -- split the target array to individual objects
FROM jsonb_array_elements(target) individual_object,
     -- operate on each object and apply jsonb_set to it. The results are aggregated in SELECT
     LATERAL conditional_jsonb_set(individual_object, path, oldValue, newValue) updated_jsonb
$$;

--usage example for update_array_elements
--select update_array_elements('[{"c" : 1, "d": 2}, {"c":3, "f":4}]'::jsonb,  '{"c"}'::text[], '3'::jsonb, '6'::jsonb);
--[{"c": 1, "d": 2}, {"c": 6, "f": 4}]

-- updates item values in the array that is part of the input jsonb
CREATE OR REPLACE FUNCTION update_subarray_elements(input jsonb, subPath text[], itemPath text[], oldValue jsonb, newValue jsonb)
    RETURNS jsonb language plpgsql AS
$$
declare
    subarray jsonb;
begin
    subarray = input #> subPath;
    subarray = update_array_elements(subarray, itemPath, oldValue, newValue);
    return jsonb_set(input, subPath, subarray);
end;
$$;

--usage example for update_subarray_elements
--select update_subarray_elements('{"a": [{"c" : 1, "d":3}, {"c":2, "e":4}], "b": [{"e" : 3}, {"f":4}]}'::jsonb, '{"a"}'::text[], '{"c"}'::text[], '2'::jsonb, '6'::jsonb);
--{"a": [{"c": 1, "d": 3}, {"c": 6, "e": 4}], "b": [{"e": 3}, {"f": 4}]}

--select update_subarray_elements('{"a": [{"c" : 1, "d":3}, {"c":2, "e":4}], "b": [{"e" : 3}, {"f":4}]}'::jsonb, '{"a"}'::text[], '{}'::text[], '{"c" : 1, "d":3}'::jsonb, '{"x" : 98, "y":99}'::jsonb);
--{"a": [{"x": 98, "y": 99}, {"c": 2, "e": 4}], "b": [{"e": 3}, {"f": 4}]}