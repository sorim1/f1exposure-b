package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

        class Data {
        public List<Double> pTrack;
        public List<Double> pAir;
        public List<Double> pRaining;
        @JsonProperty("pWind Speed")
        public List<Double> pWindSpeed;
        public List<Double> pHumidity;
        public List<Double> pPressure;
        @JsonProperty("pWind Dir")
        public List<Double> pWindDir;
        public List<Integer> pHAM;
        public List<Integer> pBOT;
        public List<Integer> pVET;
        public List<Integer> pVER;
        public List<Integer> pLEC;
        public List<Integer> pGRO;
        public List<Integer> pMAG;
        public List<Integer> pNOR;
        public List<Integer> pRAI;
        public List<Integer> pPER;
        public List<Integer> pHUL;
        public List<Integer> pRIC;
        public List<Integer> pALB;
        public List<Integer> pGIO;
        public List<Integer> pKVY;
        public List<Integer> pSTR;
        public List<Integer> pGAS;
        public List<Integer> pSAI;
        public List<Integer> pRUS;
        public List<Integer> pKUB;
        @JsonProperty("Drivers")
        public List<Driver> drivers;
        @JsonProperty("F")
        public String f;
        @JsonProperty("DR")
        public List<DR> dR;
        @JsonProperty("SQ")
        public List<Integer> sQ;
        @JsonProperty("GR")
        public List<Integer> gR;
        @JsonProperty("RT")
        public Integer rT;
        @JsonProperty("W")
        public List<Double> w;
        @JsonProperty("E")
        public boolean e;
        @JsonProperty("L")
        public Integer l;
        @JsonProperty("TL")
        public Integer tL;
        @JsonProperty("FL")
        public String fL;
        @JsonProperty("S")
        public String s;
        @JsonProperty("R")
        public String r;
        @JsonProperty("RL")
        public String rL;
        @JsonProperty("RC")
        public String rC;
        @JsonProperty("CL")
        public String cL;
        @JsonProperty("SL")
        public Integer sL;
        @JsonProperty("PD")
        public List<PD> pD;
    }

    class Graph{
        public Integer norm;
        public Data data;
        public String xtitle;
        public String ytitle;
        public String ztitle;
        @JsonProperty("Steering")
        public Steering steering;
        @JsonProperty("GforceLat")
        public GforceLat gforceLat;
        @JsonProperty("GforceLong")
        public GforceLong gforceLong;
        @JsonProperty("Brake")
        public Brake brake;
        @JsonProperty("Performance")
        public Performance performance;
        @JsonProperty("Throttle")
        public Throttle throttle;
        @JsonProperty("TrackStatus")
        public List<String> trackStatus;
    }

    class Weather{
        public Graph graph;
    }

    class Steering{
        public List<Integer> pSAI;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pMAG;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
        public List<Integer> pGIO;
    }

    class GforceLat{
        public List<Integer> pSAI;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pMAG;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
        public List<Integer> pGIO;
    }

    class GforceLong{
        public List<Integer> pSAI;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pMAG;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
        public List<Integer> pGIO;
    }

    class Brake{
        public List<Integer> pSAI;
        public List<Integer> pMAG;
        public List<Integer> pGIO;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
    }

    class Performance{
        public List<Integer> pSAI;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pMAG;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
        public List<Integer> pGIO;
    }

    class Throttle{
        public List<Integer> pSAI;
        public List<Integer> pRIC;
        public List<Integer> pNOR;
        public List<Integer> pVET;
        public List<Integer> pRAI;
        public List<Integer> pGRO;
        public List<Integer> pGAS;
        public List<Integer> pPER;
        public List<Integer> pLEC;
        public List<Integer> pSTR;
        public List<Integer> pMAG;
        public List<Integer> pALB;
        public List<Integer> pKVY;
        public List<Integer> pHUL;
        public List<Integer> pVER;
        public List<Integer> pHAM;
        public List<Integer> pRUS;
        public List<Integer> pBOT;
        public List<Integer> pKUB;
        public List<Integer> pGIO;
    }

    class Scores{
        public Graph graph;
    }

    class LapPos{
        public Graph graph;
    }

    class Driver{
        @JsonProperty("Name")
        public String name;
        @JsonProperty("Initials")
        public String initials;
        @JsonProperty("FullName")
        public String fullName;
        @JsonProperty("FirstName")
        public String firstName;
        @JsonProperty("LastName")
        public String lastName;
        @JsonProperty("Color")
        public String color;
        @JsonProperty("Team")
        public String team;
        @JsonProperty("Num")
        public String num;
    }

    class Init{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class DR{
        @JsonProperty("B")
        public List<Object> b;
        @JsonProperty("STOP")
        public List<Integer> sTOP;
        @JsonProperty("O")
        public List<Object> o;
        @JsonProperty("OC")
        public List<String> oC;
        @JsonProperty("UC")
        public List<Integer> uC;
        @JsonProperty("G")
        public List<Object> g;
        @JsonProperty("F")
        public List<Object> f;
        @JsonProperty("X")
        public List<String> x;
        @JsonProperty("TI")
        public List<Integer> tI;
    }

    class Best{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class Opt{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class Sq{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class Free{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class Xtra{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    class PD{
        public String i;
        public Integer h;
    }

    class Commentary{
        public Data data;
        public Integer seq;
        @JsonProperty("T")
        public long t;
        @JsonProperty("TY")
        public String tY;
    }

    public class LiveTimingData{
        @JsonProperty("Weather")
        public Weather weather;
        @JsonProperty("Scores")
        public Scores scores;
        @JsonProperty("LapPos")
        public LapPos lapPos;
        public Init init;
        public Best best;
        public Opt opt;
        public Sq sq;
        public Free free;
        public Xtra xtra;
        public Commentary commentary;
        public String prefix;
        public String path;
        public Integer timezoneOffset;
    }

