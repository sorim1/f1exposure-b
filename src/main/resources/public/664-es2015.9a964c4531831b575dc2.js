(self["webpackChunkpaper_dashboard_angular"] = self["webpackChunkpaper_dashboard_angular"] || []).push([[664],{

/***/ 3664:
/***/ (function(__unused_webpack_module, __webpack_exports__, __webpack_require__) {

"use strict";
// ESM COMPAT FLAG
__webpack_require__.r(__webpack_exports__);

// EXPORTS
__webpack_require__.d(__webpack_exports__, {
  "AdminLayoutModule": function() { return /* binding */ AdminLayoutModule; }
});

// EXTERNAL MODULE: ./node_modules/@angular/router/__ivy_ngcc__/fesm2015/router.js + 7 modules
var router = __webpack_require__(4655);
// EXTERNAL MODULE: ./node_modules/@angular/common/__ivy_ngcc__/fesm2015/common.js
var common = __webpack_require__(8583);
// EXTERNAL MODULE: ./node_modules/@angular/forms/__ivy_ngcc__/fesm2015/forms.js
var fesm2015_forms = __webpack_require__(3679);
// EXTERNAL MODULE: ./src/app/utility/rest.service.ts
var rest_service = __webpack_require__(7043);
// EXTERNAL MODULE: ./src/app/utility/utility.service.ts
var utility_service = __webpack_require__(2038);
// EXTERNAL MODULE: ./node_modules/@angular/core/__ivy_ngcc__/fesm2015/core.js
var core = __webpack_require__(7716);
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/sidenav.js + 1 modules
var sidenav = __webpack_require__(3017);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/platform.js
var platform = __webpack_require__(521);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/fesm2015/coercion.js
var coercion = __webpack_require__(9490);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/observable/empty.js
var empty = __webpack_require__(9193);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/Subject.js
var Subject = __webpack_require__(9765);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/observable/fromEvent.js
var fromEvent = __webpack_require__(2759);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/auditTime.js + 1 modules
var auditTime = __webpack_require__(13);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/takeUntil.js
var takeUntil = __webpack_require__(6782);
;// CONCATENATED MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/text-field.js









/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** Options to pass to the animationstart listener. */


const listenerOptions = (0,platform/* normalizePassiveListenerOptions */.i$)({ passive: true });
/**
 * An injectable service that can be used to monitor the autofill state of an input.
 * Based on the following blog post:
 * https://medium.com/@brunn/detecting-autofilled-fields-in-javascript-aed598d25da7
 */
class AutofillMonitor {
    constructor(_platform, _ngZone) {
        this._platform = _platform;
        this._ngZone = _ngZone;
        this._monitoredElements = new Map();
    }
    monitor(elementOrRef) {
        if (!this._platform.isBrowser) {
            return empty/* EMPTY */.E;
        }
        const element = (0,coercion/* coerceElement */.fI)(elementOrRef);
        const info = this._monitoredElements.get(element);
        if (info) {
            return info.subject;
        }
        const result = new Subject/* Subject */.xQ();
        const cssClass = 'cdk-text-field-autofilled';
        const listener = ((event) => {
            // Animation events fire on initial element render, we check for the presence of the autofill
            // CSS class to make sure this is a real change in state, not just the initial render before
            // we fire off events.
            if (event.animationName === 'cdk-text-field-autofill-start' &&
                !element.classList.contains(cssClass)) {
                element.classList.add(cssClass);
                this._ngZone.run(() => result.next({ target: event.target, isAutofilled: true }));
            }
            else if (event.animationName === 'cdk-text-field-autofill-end' &&
                element.classList.contains(cssClass)) {
                element.classList.remove(cssClass);
                this._ngZone.run(() => result.next({ target: event.target, isAutofilled: false }));
            }
        });
        this._ngZone.runOutsideAngular(() => {
            element.addEventListener('animationstart', listener, listenerOptions);
            element.classList.add('cdk-text-field-autofill-monitored');
        });
        this._monitoredElements.set(element, {
            subject: result,
            unlisten: () => {
                element.removeEventListener('animationstart', listener, listenerOptions);
            }
        });
        return result;
    }
    stopMonitoring(elementOrRef) {
        const element = (0,coercion/* coerceElement */.fI)(elementOrRef);
        const info = this._monitoredElements.get(element);
        if (info) {
            info.unlisten();
            info.subject.complete();
            element.classList.remove('cdk-text-field-autofill-monitored');
            element.classList.remove('cdk-text-field-autofilled');
            this._monitoredElements.delete(element);
        }
    }
    ngOnDestroy() {
        this._monitoredElements.forEach((_info, element) => this.stopMonitoring(element));
    }
}
AutofillMonitor.ɵfac = function AutofillMonitor_Factory(t) { return new (t || AutofillMonitor)(core/* ɵɵinject */.LFG(platform/* Platform */.t4), core/* ɵɵinject */.LFG(core/* NgZone */.R0b)); };
AutofillMonitor.ɵprov = core/* ɵɵdefineInjectable */.Yz7({ factory: function AutofillMonitor_Factory() { return new AutofillMonitor(core/* ɵɵinject */.LFG(platform/* Platform */.t4), core/* ɵɵinject */.LFG(core/* NgZone */.R0b)); }, token: AutofillMonitor, providedIn: "root" });
AutofillMonitor.ctorParameters = () => [
    { type: platform/* Platform */.t4 },
    { type: core/* NgZone */.R0b }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(AutofillMonitor, [{
        type: core/* Injectable */.GSi,
        args: [{ providedIn: 'root' }]
    }], function () { return [{ type: platform/* Platform */.t4 }, { type: core/* NgZone */.R0b }]; }, null); })();
/** A directive that can be used to monitor the autofill state of an input. */
class CdkAutofill {
    constructor(_elementRef, _autofillMonitor) {
        this._elementRef = _elementRef;
        this._autofillMonitor = _autofillMonitor;
        /** Emits when the autofill state of the element changes. */
        this.cdkAutofill = new core/* EventEmitter */.vpe();
    }
    ngOnInit() {
        this._autofillMonitor
            .monitor(this._elementRef)
            .subscribe(event => this.cdkAutofill.emit(event));
    }
    ngOnDestroy() {
        this._autofillMonitor.stopMonitoring(this._elementRef);
    }
}
CdkAutofill.ɵfac = function CdkAutofill_Factory(t) { return new (t || CdkAutofill)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(AutofillMonitor)); };
CdkAutofill.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: CdkAutofill, selectors: [["", "cdkAutofill", ""]], outputs: { cdkAutofill: "cdkAutofill" } });
CdkAutofill.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: AutofillMonitor }
];
CdkAutofill.propDecorators = {
    cdkAutofill: [{ type: core/* Output */.r_U }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(CdkAutofill, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[cdkAutofill]'
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: AutofillMonitor }]; }, { cdkAutofill: [{
            type: core/* Output */.r_U
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** Directive to automatically resize a textarea to fit its content. */
class CdkTextareaAutosize {
    constructor(_elementRef, _platform, _ngZone, 
    /** @breaking-change 11.0.0 make document required */
    document) {
        this._elementRef = _elementRef;
        this._platform = _platform;
        this._ngZone = _ngZone;
        this._destroyed = new Subject/* Subject */.xQ();
        this._enabled = true;
        /**
         * Value of minRows as of last resize. If the minRows has decreased, the
         * height of the textarea needs to be recomputed to reflect the new minimum. The maxHeight
         * does not have the same problem because it does not affect the textarea's scrollHeight.
         */
        this._previousMinRows = -1;
        this._isViewInited = false;
        this._document = document;
        this._textareaElement = this._elementRef.nativeElement;
        this._measuringClass = _platform.FIREFOX ?
            'cdk-textarea-autosize-measuring-firefox' :
            'cdk-textarea-autosize-measuring';
    }
    /** Minimum amount of rows in the textarea. */
    get minRows() { return this._minRows; }
    set minRows(value) {
        this._minRows = (0,coercion/* coerceNumberProperty */.su)(value);
        this._setMinHeight();
    }
    /** Maximum amount of rows in the textarea. */
    get maxRows() { return this._maxRows; }
    set maxRows(value) {
        this._maxRows = (0,coercion/* coerceNumberProperty */.su)(value);
        this._setMaxHeight();
    }
    /** Whether autosizing is enabled or not */
    get enabled() { return this._enabled; }
    set enabled(value) {
        value = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        // Only act if the actual value changed. This specifically helps to not run
        // resizeToFitContent too early (i.e. before ngAfterViewInit)
        if (this._enabled !== value) {
            (this._enabled = value) ? this.resizeToFitContent(true) : this.reset();
        }
    }
    get placeholder() { return this._textareaElement.placeholder; }
    set placeholder(value) {
        this._cachedPlaceholderHeight = undefined;
        this._textareaElement.placeholder = value;
        this._cacheTextareaPlaceholderHeight();
    }
    /** Sets the minimum height of the textarea as determined by minRows. */
    _setMinHeight() {
        const minHeight = this.minRows && this._cachedLineHeight ?
            `${this.minRows * this._cachedLineHeight}px` : null;
        if (minHeight) {
            this._textareaElement.style.minHeight = minHeight;
        }
    }
    /** Sets the maximum height of the textarea as determined by maxRows. */
    _setMaxHeight() {
        const maxHeight = this.maxRows && this._cachedLineHeight ?
            `${this.maxRows * this._cachedLineHeight}px` : null;
        if (maxHeight) {
            this._textareaElement.style.maxHeight = maxHeight;
        }
    }
    ngAfterViewInit() {
        if (this._platform.isBrowser) {
            // Remember the height which we started with in case autosizing is disabled
            this._initialHeight = this._textareaElement.style.height;
            this.resizeToFitContent();
            this._ngZone.runOutsideAngular(() => {
                const window = this._getWindow();
                (0,fromEvent/* fromEvent */.R)(window, 'resize')
                    .pipe((0,auditTime/* auditTime */.e)(16), (0,takeUntil/* takeUntil */.R)(this._destroyed))
                    .subscribe(() => this.resizeToFitContent(true));
            });
            this._isViewInited = true;
            this.resizeToFitContent(true);
        }
    }
    ngOnDestroy() {
        this._destroyed.next();
        this._destroyed.complete();
    }
    /**
     * Cache the height of a single-row textarea if it has not already been cached.
     *
     * We need to know how large a single "row" of a textarea is in order to apply minRows and
     * maxRows. For the initial version, we will assume that the height of a single line in the
     * textarea does not ever change.
     */
    _cacheTextareaLineHeight() {
        if (this._cachedLineHeight) {
            return;
        }
        // Use a clone element because we have to override some styles.
        let textareaClone = this._textareaElement.cloneNode(false);
        textareaClone.rows = 1;
        // Use `position: absolute` so that this doesn't cause a browser layout and use
        // `visibility: hidden` so that nothing is rendered. Clear any other styles that
        // would affect the height.
        textareaClone.style.position = 'absolute';
        textareaClone.style.visibility = 'hidden';
        textareaClone.style.border = 'none';
        textareaClone.style.padding = '0';
        textareaClone.style.height = '';
        textareaClone.style.minHeight = '';
        textareaClone.style.maxHeight = '';
        // In Firefox it happens that textarea elements are always bigger than the specified amount
        // of rows. This is because Firefox tries to add extra space for the horizontal scrollbar.
        // As a workaround that removes the extra space for the scrollbar, we can just set overflow
        // to hidden. This ensures that there is no invalid calculation of the line height.
        // See Firefox bug report: https://bugzilla.mozilla.org/show_bug.cgi?id=33654
        textareaClone.style.overflow = 'hidden';
        this._textareaElement.parentNode.appendChild(textareaClone);
        this._cachedLineHeight = textareaClone.clientHeight;
        this._textareaElement.parentNode.removeChild(textareaClone);
        // Min and max heights have to be re-calculated if the cached line height changes
        this._setMinHeight();
        this._setMaxHeight();
    }
    _measureScrollHeight() {
        // Reset the textarea height to auto in order to shrink back to its default size.
        // Also temporarily force overflow:hidden, so scroll bars do not interfere with calculations.
        this._textareaElement.classList.add(this._measuringClass);
        // The measuring class includes a 2px padding to workaround an issue with Chrome,
        // so we account for that extra space here by subtracting 4 (2px top + 2px bottom).
        const scrollHeight = this._textareaElement.scrollHeight - 4;
        this._textareaElement.classList.remove(this._measuringClass);
        return scrollHeight;
    }
    _cacheTextareaPlaceholderHeight() {
        if (!this._isViewInited || this._cachedPlaceholderHeight != undefined) {
            return;
        }
        if (!this.placeholder) {
            this._cachedPlaceholderHeight = 0;
            return;
        }
        const value = this._textareaElement.value;
        this._textareaElement.value = this._textareaElement.placeholder;
        this._cachedPlaceholderHeight = this._measureScrollHeight();
        this._textareaElement.value = value;
    }
    ngDoCheck() {
        if (this._platform.isBrowser) {
            this.resizeToFitContent();
        }
    }
    /**
     * Resize the textarea to fit its content.
     * @param force Whether to force a height recalculation. By default the height will be
     *    recalculated only if the value changed since the last call.
     */
    resizeToFitContent(force = false) {
        // If autosizing is disabled, just skip everything else
        if (!this._enabled) {
            return;
        }
        this._cacheTextareaLineHeight();
        this._cacheTextareaPlaceholderHeight();
        // If we haven't determined the line-height yet, we know we're still hidden and there's no point
        // in checking the height of the textarea.
        if (!this._cachedLineHeight) {
            return;
        }
        const textarea = this._elementRef.nativeElement;
        const value = textarea.value;
        // Only resize if the value or minRows have changed since these calculations can be expensive.
        if (!force && this._minRows === this._previousMinRows && value === this._previousValue) {
            return;
        }
        const scrollHeight = this._measureScrollHeight();
        const height = Math.max(scrollHeight, this._cachedPlaceholderHeight || 0);
        // Use the scrollHeight to know how large the textarea *would* be if fit its entire value.
        textarea.style.height = `${height}px`;
        this._ngZone.runOutsideAngular(() => {
            if (typeof requestAnimationFrame !== 'undefined') {
                requestAnimationFrame(() => this._scrollToCaretPosition(textarea));
            }
            else {
                setTimeout(() => this._scrollToCaretPosition(textarea));
            }
        });
        this._previousValue = value;
        this._previousMinRows = this._minRows;
    }
    /**
     * Resets the textarea to its original size
     */
    reset() {
        // Do not try to change the textarea, if the initialHeight has not been determined yet
        // This might potentially remove styles when reset() is called before ngAfterViewInit
        if (this._initialHeight !== undefined) {
            this._textareaElement.style.height = this._initialHeight;
        }
    }
    // In Ivy the `host` metadata will be merged, whereas in ViewEngine it is overridden. In order
    // to avoid double event listeners, we need to use `HostListener`. Once Ivy is the default, we
    // can move this back into `host`.
    // tslint:disable:no-host-decorator-in-concrete
    _noopInputHandler() {
        // no-op handler that ensures we're running change detection on input events.
    }
    /** Access injected document if available or fallback to global document reference */
    _getDocument() {
        return this._document || document;
    }
    /** Use defaultView of injected document if available or fallback to global window reference */
    _getWindow() {
        const doc = this._getDocument();
        return doc.defaultView || window;
    }
    /**
     * Scrolls a textarea to the caret position. On Firefox resizing the textarea will
     * prevent it from scrolling to the caret position. We need to re-set the selection
     * in order for it to scroll to the proper position.
     */
    _scrollToCaretPosition(textarea) {
        const { selectionStart, selectionEnd } = textarea;
        const document = this._getDocument();
        // IE will throw an "Unspecified error" if we try to set the selection range after the
        // element has been removed from the DOM. Assert that the directive hasn't been destroyed
        // between the time we requested the animation frame and when it was executed.
        // Also note that we have to assert that the textarea is focused before we set the
        // selection range. Setting the selection range on a non-focused textarea will cause
        // it to receive focus on IE and Edge.
        if (!this._destroyed.isStopped && document.activeElement === textarea) {
            textarea.setSelectionRange(selectionStart, selectionEnd);
        }
    }
}
CdkTextareaAutosize.ɵfac = function CdkTextareaAutosize_Factory(t) { return new (t || CdkTextareaAutosize)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(platform/* Platform */.t4), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(common/* DOCUMENT */.K0, 8)); };
CdkTextareaAutosize.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: CdkTextareaAutosize, selectors: [["textarea", "cdkTextareaAutosize", ""]], hostAttrs: ["rows", "1", 1, "cdk-textarea-autosize"], hostBindings: function CdkTextareaAutosize_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("input", function CdkTextareaAutosize_input_HostBindingHandler() { return ctx._noopInputHandler(); });
    } }, inputs: { minRows: ["cdkAutosizeMinRows", "minRows"], maxRows: ["cdkAutosizeMaxRows", "maxRows"], enabled: ["cdkTextareaAutosize", "enabled"], placeholder: "placeholder" }, exportAs: ["cdkTextareaAutosize"] });
CdkTextareaAutosize.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: platform/* Platform */.t4 },
    { type: core/* NgZone */.R0b },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [common/* DOCUMENT */.K0,] }] }
];
CdkTextareaAutosize.propDecorators = {
    minRows: [{ type: core/* Input */.IIB, args: ['cdkAutosizeMinRows',] }],
    maxRows: [{ type: core/* Input */.IIB, args: ['cdkAutosizeMaxRows',] }],
    enabled: [{ type: core/* Input */.IIB, args: ['cdkTextareaAutosize',] }],
    placeholder: [{ type: core/* Input */.IIB }],
    _noopInputHandler: [{ type: core/* HostListener */.L6J, args: ['input',] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(CdkTextareaAutosize, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'textarea[cdkTextareaAutosize]',
                exportAs: 'cdkTextareaAutosize',
                host: {
                    'class': 'cdk-textarea-autosize',
                    // Textarea elements that have the directive applied should have a single row by default.
                    // Browsers normally show two rows by default and therefore this limits the minRows binding.
                    'rows': '1'
                }
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: platform/* Platform */.t4 }, { type: core/* NgZone */.R0b }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [common/* DOCUMENT */.K0]
            }] }]; }, { minRows: [{
            type: core/* Input */.IIB,
            args: ['cdkAutosizeMinRows']
        }], maxRows: [{
            type: core/* Input */.IIB,
            args: ['cdkAutosizeMaxRows']
        }], enabled: [{
            type: core/* Input */.IIB,
            args: ['cdkTextareaAutosize']
        }], placeholder: [{
            type: core/* Input */.IIB
        }], 
    // In Ivy the `host` metadata will be merged, whereas in ViewEngine it is overridden. In order
    // to avoid double event listeners, we need to use `HostListener`. Once Ivy is the default, we
    // can move this back into `host`.
    // tslint:disable:no-host-decorator-in-concrete
    _noopInputHandler: [{
            type: core/* HostListener */.L6J,
            args: ['input']
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class TextFieldModule {
}
TextFieldModule.ɵfac = function TextFieldModule_Factory(t) { return new (t || TextFieldModule)(); };
TextFieldModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: TextFieldModule });
TextFieldModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[platform/* PlatformModule */.ud]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(TextFieldModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                declarations: [CdkAutofill, CdkTextareaAutosize],
                imports: [platform/* PlatformModule */.ud],
                exports: [CdkAutofill, CdkTextareaAutosize]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(TextFieldModule, { declarations: function () { return [CdkAutofill, CdkTextareaAutosize]; }, imports: function () { return [platform/* PlatformModule */.ud]; }, exports: function () { return [CdkAutofill, CdkTextareaAutosize]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=text-field.js.map
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/core.js + 1 modules
var fesm2015_core = __webpack_require__(2458);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/observers.js
var observers = __webpack_require__(8553);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/bidi.js
var bidi = __webpack_require__(946);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/observable/merge.js
var merge = __webpack_require__(6682);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/startWith.js
var startWith = __webpack_require__(9761);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/take.js
var take = __webpack_require__(5257);
// EXTERNAL MODULE: ./node_modules/@angular/animations/__ivy_ngcc__/fesm2015/animations.js
var animations = __webpack_require__(7238);
// EXTERNAL MODULE: ./node_modules/@angular/platform-browser/__ivy_ngcc__/fesm2015/animations.js + 1 modules
var fesm2015_animations = __webpack_require__(6237);
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/form-field.js












/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */






const _c0 = ["underline"];
const _c1 = ["connectionContainer"];
const _c2 = ["inputContainer"];
const _c3 = ["label"];
function MatFormField_ng_container_3_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementContainerStart */.ynx(0);
    core/* ɵɵelementStart */.TgZ(1, "div", 14);
    core/* ɵɵelement */._UZ(2, "div", 15);
    core/* ɵɵelement */._UZ(3, "div", 16);
    core/* ɵɵelement */._UZ(4, "div", 17);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 18);
    core/* ɵɵelement */._UZ(6, "div", 15);
    core/* ɵɵelement */._UZ(7, "div", 16);
    core/* ɵɵelement */._UZ(8, "div", 17);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementContainerEnd */.BQk();
} }
function MatFormField_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 19);
    core/* ɵɵprojection */.Hsn(1, 1);
    core/* ɵɵelementEnd */.qZA();
} }
function MatFormField_label_9_ng_container_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementContainerStart */.ynx(0);
    core/* ɵɵprojection */.Hsn(1, 2);
    core/* ɵɵelementStart */.TgZ(2, "span");
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementContainerEnd */.BQk();
} if (rf & 2) {
    const ctx_r10 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r10._control.placeholder);
} }
function MatFormField_label_9_ng_content_3_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵprojection */.Hsn(0, 3, ["*ngSwitchCase", "true"]);
} }
function MatFormField_label_9_span_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "span", 23);
    core/* ɵɵtext */._uU(1, " *");
    core/* ɵɵelementEnd */.qZA();
} }
function MatFormField_label_9_Template(rf, ctx) { if (rf & 1) {
    const _r14 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "label", 20, 21);
    core/* ɵɵlistener */.NdJ("cdkObserveContent", function MatFormField_label_9_Template_label_cdkObserveContent_0_listener() { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r13 = core/* ɵɵnextContext */.oxw(); return ctx_r13.updateOutlineGap(); });
    core/* ɵɵtemplate */.YNc(2, MatFormField_label_9_ng_container_2_Template, 4, 1, "ng-container", 12);
    core/* ɵɵtemplate */.YNc(3, MatFormField_label_9_ng_content_3_Template, 1, 0, "ng-content", 12);
    core/* ɵɵtemplate */.YNc(4, MatFormField_label_9_span_4_Template, 2, 0, "span", 22);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵclassProp */.ekj("mat-empty", ctx_r4._control.empty && !ctx_r4._shouldAlwaysFloat())("mat-form-field-empty", ctx_r4._control.empty && !ctx_r4._shouldAlwaysFloat())("mat-accent", ctx_r4.color == "accent")("mat-warn", ctx_r4.color == "warn");
    core/* ɵɵproperty */.Q6J("cdkObserveContentDisabled", ctx_r4.appearance != "outline")("id", ctx_r4._labelId)("ngSwitch", ctx_r4._hasLabel());
    core/* ɵɵattribute */.uIk("for", ctx_r4._control.id)("aria-owns", ctx_r4._control.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngSwitchCase", false);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngSwitchCase", true);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", !ctx_r4.hideRequiredMarker && ctx_r4._control.required && !ctx_r4._control.disabled);
} }
function MatFormField_div_10_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 24);
    core/* ɵɵprojection */.Hsn(1, 4);
    core/* ɵɵelementEnd */.qZA();
} }
function MatFormField_div_11_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 25, 26);
    core/* ɵɵelement */._UZ(2, "span", 27);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵclassProp */.ekj("mat-accent", ctx_r6.color == "accent")("mat-warn", ctx_r6.color == "warn");
} }
function MatFormField_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵprojection */.Hsn(1, 5);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r7 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("@transitionMessages", ctx_r7._subscriptAnimationState);
} }
function MatFormField_div_14_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 31);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r16 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("id", ctx_r16._hintLabelId);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r16.hintLabel);
} }
function MatFormField_div_14_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 28);
    core/* ɵɵtemplate */.YNc(1, MatFormField_div_14_div_1_Template, 2, 2, "div", 29);
    core/* ɵɵprojection */.Hsn(2, 6);
    core/* ɵɵelement */._UZ(3, "div", 30);
    core/* ɵɵprojection */.Hsn(4, 7);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r8 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("@transitionMessages", ctx_r8._subscriptAnimationState);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r8.hintLabel);
} }
const _c4 = ["*", [["", "matPrefix", ""]], [["mat-placeholder"]], [["mat-label"]], [["", "matSuffix", ""]], [["mat-error"]], [["mat-hint", 3, "align", "end"]], [["mat-hint", "align", "end"]]];
const _c5 = ["*", "[matPrefix]", "mat-placeholder", "mat-label", "[matSuffix]", "mat-error", "mat-hint:not([align='end'])", "mat-hint[align='end']"];
let nextUniqueId$2 = 0;
/**
 * Injection token that can be used to reference instances of `MatError`. It serves as
 * alternative token to the actual `MatError` class which could cause unnecessary
 * retention of the class and its directive metadata.
 */
const MAT_ERROR = new core/* InjectionToken */.OlP('MatError');
/** Single error message to be shown underneath the form field. */
class MatError {
    constructor(ariaLive, elementRef) {
        this.id = `mat-error-${nextUniqueId$2++}`;
        // If no aria-live value is set add 'polite' as a default. This is preferred over setting
        // role='alert' so that screen readers do not interrupt the current task to read this aloud.
        if (!ariaLive) {
            elementRef.nativeElement.setAttribute('aria-live', 'polite');
        }
    }
}
MatError.ɵfac = function MatError_Factory(t) { return new (t || MatError)(core/* ɵɵinjectAttribute */.$8M('aria-live'), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq)); };
MatError.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatError, selectors: [["mat-error"]], hostAttrs: ["aria-atomic", "true", 1, "mat-error"], hostVars: 1, hostBindings: function MatError_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵattribute */.uIk("id", ctx.id);
    } }, inputs: { id: "id" }, features: [core/* ɵɵProvidersFeature */._Bn([{ provide: MAT_ERROR, useExisting: MatError }])] });
MatError.ctorParameters = () => [
    { type: String, decorators: [{ type: core/* Attribute */.ahi, args: ['aria-live',] }] },
    { type: core/* ElementRef */.SBq }
];
MatError.propDecorators = {
    id: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatError, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-error',
                host: {
                    'class': 'mat-error',
                    '[attr.id]': 'id',
                    'aria-atomic': 'true'
                },
                providers: [{ provide: MAT_ERROR, useExisting: MatError }]
            }]
    }], function () { return [{ type: String, decorators: [{
                type: core/* Attribute */.ahi,
                args: ['aria-live']
            }] }, { type: core/* ElementRef */.SBq }]; }, { id: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Animations used by the MatFormField.
 * @docs-private
 */
const matFormFieldAnimations = {
    /** Animation that transitions the form field's error and hint messages. */
    transitionMessages: (0,animations/* trigger */.X$)('transitionMessages', [
        // TODO(mmalerba): Use angular animations for label animation as well.
        (0,animations/* state */.SB)('enter', (0,animations/* style */.oB)({ opacity: 1, transform: 'translateY(0%)' })),
        (0,animations/* transition */.eR)('void => enter', [
            (0,animations/* style */.oB)({ opacity: 0, transform: 'translateY(-5px)' }),
            (0,animations/* animate */.jt)('300ms cubic-bezier(0.55, 0, 0.55, 0.2)'),
        ]),
    ])
};

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** An interface which allows a control to work inside of a `MatFormField`. */
class MatFormFieldControl {
}
MatFormFieldControl.ɵfac = function MatFormFieldControl_Factory(t) { return new (t || MatFormFieldControl)(); };
MatFormFieldControl.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatFormFieldControl });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatFormFieldControl, [{
        type: core/* Directive */.Xek
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** @docs-private */
function getMatFormFieldPlaceholderConflictError() {
    return Error('Placeholder attribute and child element were both specified.');
}
/** @docs-private */
function getMatFormFieldDuplicatedHintError(align) {
    return Error(`A hint was already declared for 'align="${align}"'.`);
}
/** @docs-private */
function getMatFormFieldMissingControlError() {
    return Error('mat-form-field must contain a MatFormFieldControl.');
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
let nextUniqueId$1 = 0;
/**
 * Injection token that can be used to reference instances of `MatHint`. It serves as
 * alternative token to the actual `MatHint` class which could cause unnecessary
 * retention of the class and its directive metadata.
 *
 * *Note*: This is not part of the public API as the MDC-based form-field will not
 * need a lightweight token for `MatHint` and we want to reduce breaking changes.
 */
const _MAT_HINT = new core/* InjectionToken */.OlP('MatHint');
/** Hint text to be shown underneath the form field control. */
class MatHint {
    constructor() {
        /** Whether to align the hint label at the start or end of the line. */
        this.align = 'start';
        /** Unique ID for the hint. Used for the aria-describedby on the form field control. */
        this.id = `mat-hint-${nextUniqueId$1++}`;
    }
}
MatHint.ɵfac = function MatHint_Factory(t) { return new (t || MatHint)(); };
MatHint.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatHint, selectors: [["mat-hint"]], hostAttrs: [1, "mat-hint"], hostVars: 4, hostBindings: function MatHint_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵattribute */.uIk("id", ctx.id)("align", null);
        core/* ɵɵclassProp */.ekj("mat-form-field-hint-end", ctx.align === "end");
    } }, inputs: { align: "align", id: "id" }, features: [core/* ɵɵProvidersFeature */._Bn([{ provide: _MAT_HINT, useExisting: MatHint }])] });
MatHint.propDecorators = {
    align: [{ type: core/* Input */.IIB }],
    id: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatHint, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-hint',
                host: {
                    'class': 'mat-hint',
                    '[class.mat-form-field-hint-end]': 'align === "end"',
                    '[attr.id]': 'id',
                    // Remove align attribute to prevent it from interfering with layout.
                    '[attr.align]': 'null'
                },
                providers: [{ provide: _MAT_HINT, useExisting: MatHint }]
            }]
    }], function () { return []; }, { align: [{
            type: core/* Input */.IIB
        }], id: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** The floating label for a `mat-form-field`. */
class MatLabel {
}
MatLabel.ɵfac = function MatLabel_Factory(t) { return new (t || MatLabel)(); };
MatLabel.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatLabel, selectors: [["mat-label"]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatLabel, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-label'
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * The placeholder text for an `MatFormField`.
 * @deprecated Use `<mat-label>` to specify the label and the `placeholder` attribute to specify the
 *     placeholder.
 * @breaking-change 8.0.0
 */
class MatPlaceholder {
}
MatPlaceholder.ɵfac = function MatPlaceholder_Factory(t) { return new (t || MatPlaceholder)(); };
MatPlaceholder.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatPlaceholder, selectors: [["mat-placeholder"]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatPlaceholder, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-placeholder'
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Injection token that can be used to reference instances of `MatPrefix`. It serves as
 * alternative token to the actual `MatPrefix` class which could cause unnecessary
 * retention of the class and its directive metadata.
 */
const MAT_PREFIX = new core/* InjectionToken */.OlP('MatPrefix');
/** Prefix to be placed in front of the form field. */
class MatPrefix {
}
MatPrefix.ɵfac = function MatPrefix_Factory(t) { return new (t || MatPrefix)(); };
MatPrefix.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatPrefix, selectors: [["", "matPrefix", ""]], features: [core/* ɵɵProvidersFeature */._Bn([{ provide: MAT_PREFIX, useExisting: MatPrefix }])] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatPrefix, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[matPrefix]',
                providers: [{ provide: MAT_PREFIX, useExisting: MatPrefix }]
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Injection token that can be used to reference instances of `MatSuffix`. It serves as
 * alternative token to the actual `MatSuffix` class which could cause unnecessary
 * retention of the class and its directive metadata.
 */
const MAT_SUFFIX = new core/* InjectionToken */.OlP('MatSuffix');
/** Suffix to be placed at the end of the form field. */
class MatSuffix {
}
MatSuffix.ɵfac = function MatSuffix_Factory(t) { return new (t || MatSuffix)(); };
MatSuffix.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatSuffix, selectors: [["", "matSuffix", ""]], features: [core/* ɵɵProvidersFeature */._Bn([{ provide: MAT_SUFFIX, useExisting: MatSuffix }])] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSuffix, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[matSuffix]',
                providers: [{ provide: MAT_SUFFIX, useExisting: MatSuffix }]
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
let nextUniqueId = 0;
const floatingLabelScale = 0.75;
const outlineGapPadding = 5;
/**
 * Boilerplate for applying mixins to MatFormField.
 * @docs-private
 */
const _MatFormFieldBase = (0,fesm2015_core/* mixinColor */.pj)(class {
    constructor(_elementRef) {
        this._elementRef = _elementRef;
    }
}, 'primary');
/**
 * Injection token that can be used to configure the
 * default options for all form field within an app.
 */
const MAT_FORM_FIELD_DEFAULT_OPTIONS = new core/* InjectionToken */.OlP('MAT_FORM_FIELD_DEFAULT_OPTIONS');
/**
 * Injection token that can be used to inject an instances of `MatFormField`. It serves
 * as alternative token to the actual `MatFormField` class which would cause unnecessary
 * retention of the `MatFormField` class and its component metadata.
 */
const MAT_FORM_FIELD = new core/* InjectionToken */.OlP('MatFormField');
/** Container for form controls that applies Material Design styling and behavior. */
class MatFormField extends _MatFormFieldBase {
    constructor(elementRef, _changeDetectorRef, 
    /**
     * @deprecated `_labelOptions` parameter no longer being used. To be removed.
     * @breaking-change 12.0.0
     */
    // Use `ElementRef` here so Angular has something to inject.
    _labelOptions, _dir, _defaults, _platform, _ngZone, _animationMode) {
        super(elementRef);
        this._changeDetectorRef = _changeDetectorRef;
        this._dir = _dir;
        this._defaults = _defaults;
        this._platform = _platform;
        this._ngZone = _ngZone;
        /**
         * Whether the outline gap needs to be calculated
         * immediately on the next change detection run.
         */
        this._outlineGapCalculationNeededImmediately = false;
        /** Whether the outline gap needs to be calculated next time the zone has stabilized. */
        this._outlineGapCalculationNeededOnStable = false;
        this._destroyed = new Subject/* Subject */.xQ();
        /** Override for the logic that disables the label animation in certain cases. */
        this._showAlwaysAnimate = false;
        /** State of the mat-hint and mat-error animations. */
        this._subscriptAnimationState = '';
        this._hintLabel = '';
        // Unique id for the hint label.
        this._hintLabelId = `mat-hint-${nextUniqueId++}`;
        // Unique id for the label element.
        this._labelId = `mat-form-field-label-${nextUniqueId++}`;
        this.floatLabel = this._getDefaultFloatLabelState();
        this._animationsEnabled = _animationMode !== 'NoopAnimations';
        // Set the default through here so we invoke the setter on the first run.
        this.appearance = (_defaults && _defaults.appearance) ? _defaults.appearance : 'legacy';
        this._hideRequiredMarker = (_defaults && _defaults.hideRequiredMarker != null) ?
            _defaults.hideRequiredMarker : false;
    }
    /** The form-field appearance style. */
    get appearance() { return this._appearance; }
    set appearance(value) {
        const oldValue = this._appearance;
        this._appearance = value || (this._defaults && this._defaults.appearance) || 'legacy';
        if (this._appearance === 'outline' && oldValue !== value) {
            this._outlineGapCalculationNeededOnStable = true;
        }
    }
    /** Whether the required marker should be hidden. */
    get hideRequiredMarker() { return this._hideRequiredMarker; }
    set hideRequiredMarker(value) {
        this._hideRequiredMarker = (0,coercion/* coerceBooleanProperty */.Ig)(value);
    }
    /** Whether the floating label should always float or not. */
    _shouldAlwaysFloat() {
        return this.floatLabel === 'always' && !this._showAlwaysAnimate;
    }
    /** Whether the label can float or not. */
    _canLabelFloat() { return this.floatLabel !== 'never'; }
    /** Text for the form field hint. */
    get hintLabel() { return this._hintLabel; }
    set hintLabel(value) {
        this._hintLabel = value;
        this._processHints();
    }
    /**
     * Whether the label should always float, never float or float as the user types.
     *
     * Note: only the legacy appearance supports the `never` option. `never` was originally added as a
     * way to make the floating label emulate the behavior of a standard input placeholder. However
     * the form field now supports both floating labels and placeholders. Therefore in the non-legacy
     * appearances the `never` option has been disabled in favor of just using the placeholder.
     */
    get floatLabel() {
        return this.appearance !== 'legacy' && this._floatLabel === 'never' ? 'auto' : this._floatLabel;
    }
    set floatLabel(value) {
        if (value !== this._floatLabel) {
            this._floatLabel = value || this._getDefaultFloatLabelState();
            this._changeDetectorRef.markForCheck();
        }
    }
    get _control() {
        // TODO(crisbeto): we need this workaround in order to support both Ivy and ViewEngine.
        //  We should clean this up once Ivy is the default renderer.
        return this._explicitFormFieldControl || this._controlNonStatic || this._controlStatic;
    }
    set _control(value) {
        this._explicitFormFieldControl = value;
    }
    /**
     * Gets the id of the label element. If no label is present, returns `null`.
     */
    getLabelId() {
        return this._hasFloatingLabel() ? this._labelId : null;
    }
    /**
     * Gets an ElementRef for the element that a overlay attached to the form-field should be
     * positioned relative to.
     */
    getConnectedOverlayOrigin() {
        return this._connectionContainerRef || this._elementRef;
    }
    ngAfterContentInit() {
        this._validateControlChild();
        const control = this._control;
        if (control.controlType) {
            this._elementRef.nativeElement.classList.add(`mat-form-field-type-${control.controlType}`);
        }
        // Subscribe to changes in the child control state in order to update the form field UI.
        control.stateChanges.pipe((0,startWith/* startWith */.O)(null)).subscribe(() => {
            this._validatePlaceholders();
            this._syncDescribedByIds();
            this._changeDetectorRef.markForCheck();
        });
        // Run change detection if the value changes.
        if (control.ngControl && control.ngControl.valueChanges) {
            control.ngControl.valueChanges
                .pipe((0,takeUntil/* takeUntil */.R)(this._destroyed))
                .subscribe(() => this._changeDetectorRef.markForCheck());
        }
        // Note that we have to run outside of the `NgZone` explicitly,
        // in order to avoid throwing users into an infinite loop
        // if `zone-patch-rxjs` is included.
        this._ngZone.runOutsideAngular(() => {
            this._ngZone.onStable.pipe((0,takeUntil/* takeUntil */.R)(this._destroyed)).subscribe(() => {
                if (this._outlineGapCalculationNeededOnStable) {
                    this.updateOutlineGap();
                }
            });
        });
        // Run change detection and update the outline if the suffix or prefix changes.
        (0,merge/* merge */.T)(this._prefixChildren.changes, this._suffixChildren.changes).subscribe(() => {
            this._outlineGapCalculationNeededOnStable = true;
            this._changeDetectorRef.markForCheck();
        });
        // Re-validate when the number of hints changes.
        this._hintChildren.changes.pipe((0,startWith/* startWith */.O)(null)).subscribe(() => {
            this._processHints();
            this._changeDetectorRef.markForCheck();
        });
        // Update the aria-described by when the number of errors changes.
        this._errorChildren.changes.pipe((0,startWith/* startWith */.O)(null)).subscribe(() => {
            this._syncDescribedByIds();
            this._changeDetectorRef.markForCheck();
        });
        if (this._dir) {
            this._dir.change.pipe((0,takeUntil/* takeUntil */.R)(this._destroyed)).subscribe(() => {
                if (typeof requestAnimationFrame === 'function') {
                    this._ngZone.runOutsideAngular(() => {
                        requestAnimationFrame(() => this.updateOutlineGap());
                    });
                }
                else {
                    this.updateOutlineGap();
                }
            });
        }
    }
    ngAfterContentChecked() {
        this._validateControlChild();
        if (this._outlineGapCalculationNeededImmediately) {
            this.updateOutlineGap();
        }
    }
    ngAfterViewInit() {
        // Avoid animations on load.
        this._subscriptAnimationState = 'enter';
        this._changeDetectorRef.detectChanges();
    }
    ngOnDestroy() {
        this._destroyed.next();
        this._destroyed.complete();
    }
    /** Determines whether a class from the NgControl should be forwarded to the host element. */
    _shouldForward(prop) {
        const ngControl = this._control ? this._control.ngControl : null;
        return ngControl && ngControl[prop];
    }
    _hasPlaceholder() {
        return !!(this._control && this._control.placeholder || this._placeholderChild);
    }
    _hasLabel() {
        return !!(this._labelChildNonStatic || this._labelChildStatic);
    }
    _shouldLabelFloat() {
        return this._canLabelFloat() &&
            ((this._control && this._control.shouldLabelFloat) || this._shouldAlwaysFloat());
    }
    _hideControlPlaceholder() {
        // In the legacy appearance the placeholder is promoted to a label if no label is given.
        return this.appearance === 'legacy' && !this._hasLabel() ||
            this._hasLabel() && !this._shouldLabelFloat();
    }
    _hasFloatingLabel() {
        // In the legacy appearance the placeholder is promoted to a label if no label is given.
        return this._hasLabel() || this.appearance === 'legacy' && this._hasPlaceholder();
    }
    /** Determines whether to display hints or errors. */
    _getDisplayedMessages() {
        return (this._errorChildren && this._errorChildren.length > 0 &&
            this._control.errorState) ? 'error' : 'hint';
    }
    /** Animates the placeholder up and locks it in position. */
    _animateAndLockLabel() {
        if (this._hasFloatingLabel() && this._canLabelFloat()) {
            // If animations are disabled, we shouldn't go in here,
            // because the `transitionend` will never fire.
            if (this._animationsEnabled && this._label) {
                this._showAlwaysAnimate = true;
                (0,fromEvent/* fromEvent */.R)(this._label.nativeElement, 'transitionend').pipe((0,take/* take */.q)(1)).subscribe(() => {
                    this._showAlwaysAnimate = false;
                });
            }
            this.floatLabel = 'always';
            this._changeDetectorRef.markForCheck();
        }
    }
    /**
     * Ensure that there is only one placeholder (either `placeholder` attribute on the child control
     * or child element with the `mat-placeholder` directive).
     */
    _validatePlaceholders() {
        if (this._control.placeholder && this._placeholderChild &&
            (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getMatFormFieldPlaceholderConflictError();
        }
    }
    /** Does any extra processing that is required when handling the hints. */
    _processHints() {
        this._validateHints();
        this._syncDescribedByIds();
    }
    /**
     * Ensure that there is a maximum of one of each `<mat-hint>` alignment specified, with the
     * attribute being considered as `align="start"`.
     */
    _validateHints() {
        if (this._hintChildren && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            let startHint;
            let endHint;
            this._hintChildren.forEach((hint) => {
                if (hint.align === 'start') {
                    if (startHint || this.hintLabel) {
                        throw getMatFormFieldDuplicatedHintError('start');
                    }
                    startHint = hint;
                }
                else if (hint.align === 'end') {
                    if (endHint) {
                        throw getMatFormFieldDuplicatedHintError('end');
                    }
                    endHint = hint;
                }
            });
        }
    }
    /** Gets the default float label state. */
    _getDefaultFloatLabelState() {
        return (this._defaults && this._defaults.floatLabel) || 'auto';
    }
    /**
     * Sets the list of element IDs that describe the child control. This allows the control to update
     * its `aria-describedby` attribute accordingly.
     */
    _syncDescribedByIds() {
        if (this._control) {
            let ids = [];
            // TODO(wagnermaciel): Remove the type check when we find the root cause of this bug.
            if (this._control.userAriaDescribedBy &&
                typeof this._control.userAriaDescribedBy === 'string') {
                ids.push(...this._control.userAriaDescribedBy.split(' '));
            }
            if (this._getDisplayedMessages() === 'hint') {
                const startHint = this._hintChildren ?
                    this._hintChildren.find(hint => hint.align === 'start') : null;
                const endHint = this._hintChildren ?
                    this._hintChildren.find(hint => hint.align === 'end') : null;
                if (startHint) {
                    ids.push(startHint.id);
                }
                else if (this._hintLabel) {
                    ids.push(this._hintLabelId);
                }
                if (endHint) {
                    ids.push(endHint.id);
                }
            }
            else if (this._errorChildren) {
                ids.push(...this._errorChildren.map(error => error.id));
            }
            this._control.setDescribedByIds(ids);
        }
    }
    /** Throws an error if the form field's control is missing. */
    _validateControlChild() {
        if (!this._control && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getMatFormFieldMissingControlError();
        }
    }
    /**
     * Updates the width and position of the gap in the outline. Only relevant for the outline
     * appearance.
     */
    updateOutlineGap() {
        const labelEl = this._label ? this._label.nativeElement : null;
        if (this.appearance !== 'outline' || !labelEl || !labelEl.children.length ||
            !labelEl.textContent.trim()) {
            return;
        }
        if (!this._platform.isBrowser) {
            // getBoundingClientRect isn't available on the server.
            return;
        }
        // If the element is not present in the DOM, the outline gap will need to be calculated
        // the next time it is checked and in the DOM.
        if (!this._isAttachedToDOM()) {
            this._outlineGapCalculationNeededImmediately = true;
            return;
        }
        let startWidth = 0;
        let gapWidth = 0;
        const container = this._connectionContainerRef.nativeElement;
        const startEls = container.querySelectorAll('.mat-form-field-outline-start');
        const gapEls = container.querySelectorAll('.mat-form-field-outline-gap');
        if (this._label && this._label.nativeElement.children.length) {
            const containerRect = container.getBoundingClientRect();
            // If the container's width and height are zero, it means that the element is
            // invisible and we can't calculate the outline gap. Mark the element as needing
            // to be checked the next time the zone stabilizes. We can't do this immediately
            // on the next change detection, because even if the element becomes visible,
            // the `ClientRect` won't be reclaculated immediately. We reset the
            // `_outlineGapCalculationNeededImmediately` flag some we don't run the checks twice.
            if (containerRect.width === 0 && containerRect.height === 0) {
                this._outlineGapCalculationNeededOnStable = true;
                this._outlineGapCalculationNeededImmediately = false;
                return;
            }
            const containerStart = this._getStartEnd(containerRect);
            const labelChildren = labelEl.children;
            const labelStart = this._getStartEnd(labelChildren[0].getBoundingClientRect());
            let labelWidth = 0;
            for (let i = 0; i < labelChildren.length; i++) {
                labelWidth += labelChildren[i].offsetWidth;
            }
            startWidth = Math.abs(labelStart - containerStart) - outlineGapPadding;
            gapWidth = labelWidth > 0 ? labelWidth * floatingLabelScale + outlineGapPadding * 2 : 0;
        }
        for (let i = 0; i < startEls.length; i++) {
            startEls[i].style.width = `${startWidth}px`;
        }
        for (let i = 0; i < gapEls.length; i++) {
            gapEls[i].style.width = `${gapWidth}px`;
        }
        this._outlineGapCalculationNeededOnStable =
            this._outlineGapCalculationNeededImmediately = false;
    }
    /** Gets the start end of the rect considering the current directionality. */
    _getStartEnd(rect) {
        return (this._dir && this._dir.value === 'rtl') ? rect.right : rect.left;
    }
    /** Checks whether the form field is attached to the DOM. */
    _isAttachedToDOM() {
        const element = this._elementRef.nativeElement;
        if (element.getRootNode) {
            const rootNode = element.getRootNode();
            // If the element is inside the DOM the root node will be either the document
            // or the closest shadow root, otherwise it'll be the element itself.
            return rootNode && rootNode !== element;
        }
        // Otherwise fall back to checking if it's in the document. This doesn't account for
        // shadow DOM, however browser that support shadow DOM should support `getRootNode` as well.
        return document.documentElement.contains(element);
    }
}
MatFormField.ɵfac = function MatFormField_Factory(t) { return new (t || MatFormField)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(bidi/* Directionality */.Is, 8), core/* ɵɵdirectiveInject */.Y36(MAT_FORM_FIELD_DEFAULT_OPTIONS, 8), core/* ɵɵdirectiveInject */.Y36(platform/* Platform */.t4), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb, 8)); };
MatFormField.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatFormField, selectors: [["mat-form-field"]], contentQueries: function MatFormField_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatFormFieldControl, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatFormFieldControl, 7);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatLabel, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatLabel, 7);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatPlaceholder, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MAT_ERROR, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, _MAT_HINT, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MAT_PREFIX, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, MAT_SUFFIX, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._controlNonStatic = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._controlStatic = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._labelChildNonStatic = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._labelChildStatic = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._placeholderChild = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._errorChildren = _t);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._hintChildren = _t);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._prefixChildren = _t);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._suffixChildren = _t);
    } }, viewQuery: function MatFormField_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(_c0, 5);
        core/* ɵɵviewQuery */.Gf(_c1, 7);
        core/* ɵɵviewQuery */.Gf(_c2, 5);
        core/* ɵɵviewQuery */.Gf(_c3, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.underlineRef = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._connectionContainerRef = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._inputContainerRef = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._label = _t.first);
    } }, hostAttrs: [1, "mat-form-field"], hostVars: 40, hostBindings: function MatFormField_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵclassProp */.ekj("mat-form-field-appearance-standard", ctx.appearance == "standard")("mat-form-field-appearance-fill", ctx.appearance == "fill")("mat-form-field-appearance-outline", ctx.appearance == "outline")("mat-form-field-appearance-legacy", ctx.appearance == "legacy")("mat-form-field-invalid", ctx._control.errorState)("mat-form-field-can-float", ctx._canLabelFloat())("mat-form-field-should-float", ctx._shouldLabelFloat())("mat-form-field-has-label", ctx._hasFloatingLabel())("mat-form-field-hide-placeholder", ctx._hideControlPlaceholder())("mat-form-field-disabled", ctx._control.disabled)("mat-form-field-autofilled", ctx._control.autofilled)("mat-focused", ctx._control.focused)("ng-untouched", ctx._shouldForward("untouched"))("ng-touched", ctx._shouldForward("touched"))("ng-pristine", ctx._shouldForward("pristine"))("ng-dirty", ctx._shouldForward("dirty"))("ng-valid", ctx._shouldForward("valid"))("ng-invalid", ctx._shouldForward("invalid"))("ng-pending", ctx._shouldForward("pending"))("_mat-animation-noopable", !ctx._animationsEnabled);
    } }, inputs: { color: "color", floatLabel: "floatLabel", appearance: "appearance", hideRequiredMarker: "hideRequiredMarker", hintLabel: "hintLabel" }, exportAs: ["matFormField"], features: [core/* ɵɵProvidersFeature */._Bn([
            { provide: MAT_FORM_FIELD, useExisting: MatFormField },
        ]), core/* ɵɵInheritDefinitionFeature */.qOj], ngContentSelectors: _c5, decls: 15, vars: 8, consts: [[1, "mat-form-field-wrapper"], [1, "mat-form-field-flex", 3, "click"], ["connectionContainer", ""], [4, "ngIf"], ["class", "mat-form-field-prefix", 4, "ngIf"], [1, "mat-form-field-infix"], ["inputContainer", ""], [1, "mat-form-field-label-wrapper"], ["class", "mat-form-field-label", 3, "cdkObserveContentDisabled", "id", "mat-empty", "mat-form-field-empty", "mat-accent", "mat-warn", "ngSwitch", "cdkObserveContent", 4, "ngIf"], ["class", "mat-form-field-suffix", 4, "ngIf"], ["class", "mat-form-field-underline", 4, "ngIf"], [1, "mat-form-field-subscript-wrapper", 3, "ngSwitch"], [4, "ngSwitchCase"], ["class", "mat-form-field-hint-wrapper", 4, "ngSwitchCase"], [1, "mat-form-field-outline"], [1, "mat-form-field-outline-start"], [1, "mat-form-field-outline-gap"], [1, "mat-form-field-outline-end"], [1, "mat-form-field-outline", "mat-form-field-outline-thick"], [1, "mat-form-field-prefix"], [1, "mat-form-field-label", 3, "cdkObserveContentDisabled", "id", "ngSwitch", "cdkObserveContent"], ["label", ""], ["class", "mat-placeholder-required mat-form-field-required-marker", "aria-hidden", "true", 4, "ngIf"], ["aria-hidden", "true", 1, "mat-placeholder-required", "mat-form-field-required-marker"], [1, "mat-form-field-suffix"], [1, "mat-form-field-underline"], ["underline", ""], [1, "mat-form-field-ripple"], [1, "mat-form-field-hint-wrapper"], ["class", "mat-hint", 3, "id", 4, "ngIf"], [1, "mat-form-field-hint-spacer"], [1, "mat-hint", 3, "id"]], template: function MatFormField_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(_c4);
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1, 2);
        core/* ɵɵlistener */.NdJ("click", function MatFormField_Template_div_click_1_listener($event) { return ctx._control.onContainerClick && ctx._control.onContainerClick($event); });
        core/* ɵɵtemplate */.YNc(3, MatFormField_ng_container_3_Template, 9, 0, "ng-container", 3);
        core/* ɵɵtemplate */.YNc(4, MatFormField_div_4_Template, 2, 0, "div", 4);
        core/* ɵɵelementStart */.TgZ(5, "div", 5, 6);
        core/* ɵɵprojection */.Hsn(7);
        core/* ɵɵelementStart */.TgZ(8, "span", 7);
        core/* ɵɵtemplate */.YNc(9, MatFormField_label_9_Template, 5, 16, "label", 8);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(10, MatFormField_div_10_Template, 2, 0, "div", 9);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(11, MatFormField_div_11_Template, 3, 4, "div", 10);
        core/* ɵɵelementStart */.TgZ(12, "div", 11);
        core/* ɵɵtemplate */.YNc(13, MatFormField_div_13_Template, 2, 1, "div", 12);
        core/* ɵɵtemplate */.YNc(14, MatFormField_div_14_Template, 5, 2, "div", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.appearance == "outline");
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx._prefixChildren.length);
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngIf", ctx._hasFloatingLabel());
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx._suffixChildren.length);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.appearance != "outline");
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngSwitch", ctx._getDisplayedMessages());
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngSwitchCase", "error");
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngSwitchCase", "hint");
    } }, directives: [common/* NgIf */.O5, common/* NgSwitch */.RF, common/* NgSwitchCase */.n9, observers/* CdkObserveContent */.wD], styles: [".mat-form-field{display:inline-block;position:relative;text-align:left}[dir=rtl] .mat-form-field{text-align:right}.mat-form-field-wrapper{position:relative}.mat-form-field-flex{display:inline-flex;align-items:baseline;box-sizing:border-box;width:100%}.mat-form-field-prefix,.mat-form-field-suffix{white-space:nowrap;flex:none;position:relative}.mat-form-field-infix{display:block;position:relative;flex:auto;min-width:0;width:180px}.cdk-high-contrast-active .mat-form-field-infix{border-image:linear-gradient(transparent, transparent)}.mat-form-field-label-wrapper{position:absolute;left:0;box-sizing:content-box;width:100%;height:100%;overflow:hidden;pointer-events:none}[dir=rtl] .mat-form-field-label-wrapper{left:auto;right:0}.mat-form-field-label{position:absolute;left:0;font:inherit;pointer-events:none;width:100%;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;transform-origin:0 0;transition:transform 400ms cubic-bezier(0.25, 0.8, 0.25, 1),color 400ms cubic-bezier(0.25, 0.8, 0.25, 1),width 400ms cubic-bezier(0.25, 0.8, 0.25, 1);display:none}[dir=rtl] .mat-form-field-label{transform-origin:100% 0;left:auto;right:0}.mat-form-field-empty.mat-form-field-label,.mat-form-field-can-float.mat-form-field-should-float .mat-form-field-label{display:block}.mat-form-field-autofill-control:-webkit-autofill+.mat-form-field-label-wrapper .mat-form-field-label{display:none}.mat-form-field-can-float .mat-form-field-autofill-control:-webkit-autofill+.mat-form-field-label-wrapper .mat-form-field-label{display:block;transition:none}.mat-input-server:focus+.mat-form-field-label-wrapper .mat-form-field-label,.mat-input-server[placeholder]:not(:placeholder-shown)+.mat-form-field-label-wrapper .mat-form-field-label{display:none}.mat-form-field-can-float .mat-input-server:focus+.mat-form-field-label-wrapper .mat-form-field-label,.mat-form-field-can-float .mat-input-server[placeholder]:not(:placeholder-shown)+.mat-form-field-label-wrapper .mat-form-field-label{display:block}.mat-form-field-label:not(.mat-form-field-empty){transition:none}.mat-form-field-underline{position:absolute;width:100%;pointer-events:none;transform:scale3d(1, 1.0001, 1)}.mat-form-field-ripple{position:absolute;left:0;width:100%;transform-origin:50%;transform:scaleX(0.5);opacity:0;transition:background-color 300ms cubic-bezier(0.55, 0, 0.55, 0.2)}.mat-form-field.mat-focused .mat-form-field-ripple,.mat-form-field.mat-form-field-invalid .mat-form-field-ripple{opacity:1;transform:none;transition:transform 300ms cubic-bezier(0.25, 0.8, 0.25, 1),opacity 100ms cubic-bezier(0.25, 0.8, 0.25, 1),background-color 300ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-subscript-wrapper{position:absolute;box-sizing:border-box;width:100%;overflow:hidden}.mat-form-field-subscript-wrapper .mat-icon,.mat-form-field-label-wrapper .mat-icon{width:1em;height:1em;font-size:inherit;vertical-align:baseline}.mat-form-field-hint-wrapper{display:flex}.mat-form-field-hint-spacer{flex:1 0 1em}.mat-error{display:block}.mat-form-field-control-wrapper{position:relative}.mat-form-field-hint-end{order:1}.mat-form-field._mat-animation-noopable .mat-form-field-label,.mat-form-field._mat-animation-noopable .mat-form-field-ripple{transition:none}\n", ".mat-form-field-appearance-fill .mat-form-field-flex{border-radius:4px 4px 0 0;padding:.75em .75em 0 .75em}.cdk-high-contrast-active .mat-form-field-appearance-fill .mat-form-field-flex{outline:solid 1px}.cdk-high-contrast-active .mat-form-field-appearance-fill.mat-focused .mat-form-field-flex{outline:dashed 3px}.mat-form-field-appearance-fill .mat-form-field-underline::before{content:\"\";display:block;position:absolute;bottom:0;height:1px;width:100%}.mat-form-field-appearance-fill .mat-form-field-ripple{bottom:0;height:2px}.cdk-high-contrast-active .mat-form-field-appearance-fill .mat-form-field-ripple{height:0}.mat-form-field-appearance-fill:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{opacity:1;transform:none;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-fill._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{transition:none}.mat-form-field-appearance-fill .mat-form-field-subscript-wrapper{padding:0 1em}\n", ".mat-input-element{font:inherit;background:transparent;color:currentColor;border:none;outline:none;padding:0;margin:0;width:100%;max-width:100%;vertical-align:bottom;text-align:inherit;box-sizing:content-box}.mat-input-element:-moz-ui-invalid{box-shadow:none}.mat-input-element::-ms-clear,.mat-input-element::-ms-reveal{display:none}.mat-input-element,.mat-input-element::-webkit-search-cancel-button,.mat-input-element::-webkit-search-decoration,.mat-input-element::-webkit-search-results-button,.mat-input-element::-webkit-search-results-decoration{-webkit-appearance:none}.mat-input-element::-webkit-contacts-auto-fill-button,.mat-input-element::-webkit-caps-lock-indicator,.mat-input-element::-webkit-credentials-auto-fill-button{visibility:hidden}.mat-input-element[type=date],.mat-input-element[type=datetime],.mat-input-element[type=datetime-local],.mat-input-element[type=month],.mat-input-element[type=week],.mat-input-element[type=time]{line-height:1}.mat-input-element[type=date]::after,.mat-input-element[type=datetime]::after,.mat-input-element[type=datetime-local]::after,.mat-input-element[type=month]::after,.mat-input-element[type=week]::after,.mat-input-element[type=time]::after{content:\" \";white-space:pre;width:1px}.mat-input-element::-webkit-inner-spin-button,.mat-input-element::-webkit-calendar-picker-indicator,.mat-input-element::-webkit-clear-button{font-size:.75em}.mat-input-element::placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element::-moz-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::-moz-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element::-webkit-input-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::-webkit-input-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element:-ms-input-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element:-ms-input-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-form-field-hide-placeholder .mat-input-element::placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element::-moz-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::-moz-placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element::-webkit-input-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::-webkit-input-placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element:-ms-input-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element:-ms-input-placeholder{opacity:0}textarea.mat-input-element{resize:vertical;overflow:auto}textarea.mat-input-element.cdk-textarea-autosize{resize:none}textarea.mat-input-element{padding:2px 0;margin:-2px 0}select.mat-input-element{-moz-appearance:none;-webkit-appearance:none;position:relative;background-color:transparent;display:inline-flex;box-sizing:border-box;padding-top:1em;top:-1em;margin-bottom:-1em}select.mat-input-element::-ms-expand{display:none}select.mat-input-element::-moz-focus-inner{border:0}select.mat-input-element:not(:disabled){cursor:pointer}select.mat-input-element::-ms-value{color:inherit;background:none}.mat-focused .cdk-high-contrast-active select.mat-input-element::-ms-value{color:inherit}.mat-form-field-type-mat-native-select .mat-form-field-infix::after{content:\"\";width:0;height:0;border-left:5px solid transparent;border-right:5px solid transparent;border-top:5px solid;position:absolute;top:50%;right:0;margin-top:-2.5px;pointer-events:none}[dir=rtl] .mat-form-field-type-mat-native-select .mat-form-field-infix::after{right:auto;left:0}.mat-form-field-type-mat-native-select .mat-input-element{padding-right:15px}[dir=rtl] .mat-form-field-type-mat-native-select .mat-input-element{padding-right:0;padding-left:15px}.mat-form-field-type-mat-native-select .mat-form-field-label-wrapper{max-width:calc(100% - 10px)}.mat-form-field-type-mat-native-select.mat-form-field-appearance-outline .mat-form-field-infix::after{margin-top:-5px}.mat-form-field-type-mat-native-select.mat-form-field-appearance-fill .mat-form-field-infix::after{margin-top:-10px}\n", ".mat-form-field-appearance-legacy .mat-form-field-label{transform:perspective(100px);-ms-transform:none}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon{width:1em}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon-button,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon-button{font:inherit;vertical-align:baseline}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon-button .mat-icon,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon-button .mat-icon{font-size:inherit}.mat-form-field-appearance-legacy .mat-form-field-underline{height:1px}.cdk-high-contrast-active .mat-form-field-appearance-legacy .mat-form-field-underline{height:0;border-top:solid 1px}.mat-form-field-appearance-legacy .mat-form-field-ripple{top:0;height:2px;overflow:hidden}.cdk-high-contrast-active .mat-form-field-appearance-legacy .mat-form-field-ripple{height:0;border-top:solid 2px}.mat-form-field-appearance-legacy.mat-form-field-disabled .mat-form-field-underline{background-position:0;background-color:transparent}.cdk-high-contrast-active .mat-form-field-appearance-legacy.mat-form-field-disabled .mat-form-field-underline{border-top-style:dotted;border-top-width:2px}.mat-form-field-appearance-legacy.mat-form-field-invalid:not(.mat-focused) .mat-form-field-ripple{height:1px}\n", ".mat-form-field-appearance-outline .mat-form-field-wrapper{margin:.25em 0}.mat-form-field-appearance-outline .mat-form-field-flex{padding:0 .75em 0 .75em;margin-top:-0.25em;position:relative}.mat-form-field-appearance-outline .mat-form-field-prefix,.mat-form-field-appearance-outline .mat-form-field-suffix{top:.25em}.mat-form-field-appearance-outline .mat-form-field-outline{display:flex;position:absolute;top:.25em;left:0;right:0;bottom:0;pointer-events:none}.mat-form-field-appearance-outline .mat-form-field-outline-start,.mat-form-field-appearance-outline .mat-form-field-outline-end{border:1px solid currentColor;min-width:5px}.mat-form-field-appearance-outline .mat-form-field-outline-start{border-radius:5px 0 0 5px;border-right-style:none}[dir=rtl] .mat-form-field-appearance-outline .mat-form-field-outline-start{border-right-style:solid;border-left-style:none;border-radius:0 5px 5px 0}.mat-form-field-appearance-outline .mat-form-field-outline-end{border-radius:0 5px 5px 0;border-left-style:none;flex-grow:1}[dir=rtl] .mat-form-field-appearance-outline .mat-form-field-outline-end{border-left-style:solid;border-right-style:none;border-radius:5px 0 0 5px}.mat-form-field-appearance-outline .mat-form-field-outline-gap{border-radius:.000001px;border:1px solid currentColor;border-left-style:none;border-right-style:none}.mat-form-field-appearance-outline.mat-form-field-can-float.mat-form-field-should-float .mat-form-field-outline-gap{border-top-color:transparent}.mat-form-field-appearance-outline .mat-form-field-outline-thick{opacity:0}.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-start,.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-end,.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-gap{border-width:2px}.mat-form-field-appearance-outline.mat-focused .mat-form-field-outline,.mat-form-field-appearance-outline.mat-form-field-invalid .mat-form-field-outline{opacity:0;transition:opacity 100ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-outline.mat-focused .mat-form-field-outline-thick,.mat-form-field-appearance-outline.mat-form-field-invalid .mat-form-field-outline-thick{opacity:1}.cdk-high-contrast-active .mat-form-field-appearance-outline.mat-focused .mat-form-field-outline-thick{border:3px dashed}.mat-form-field-appearance-outline:not(.mat-form-field-disabled) .mat-form-field-flex:hover .mat-form-field-outline{opacity:0;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-outline:not(.mat-form-field-disabled) .mat-form-field-flex:hover .mat-form-field-outline-thick{opacity:1}.mat-form-field-appearance-outline .mat-form-field-subscript-wrapper{padding:0 1em}.mat-form-field-appearance-outline._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-outline,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-start,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-end,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-gap{transition:none}\n", ".mat-form-field-appearance-standard .mat-form-field-flex{padding-top:.75em}.mat-form-field-appearance-standard .mat-form-field-underline{height:1px}.cdk-high-contrast-active .mat-form-field-appearance-standard .mat-form-field-underline{height:0;border-top:solid 1px}.mat-form-field-appearance-standard .mat-form-field-ripple{bottom:0;height:2px}.cdk-high-contrast-active .mat-form-field-appearance-standard .mat-form-field-ripple{height:0;border-top:solid 2px}.mat-form-field-appearance-standard.mat-form-field-disabled .mat-form-field-underline{background-position:0;background-color:transparent}.cdk-high-contrast-active .mat-form-field-appearance-standard.mat-form-field-disabled .mat-form-field-underline{border-top-style:dotted;border-top-width:2px}.mat-form-field-appearance-standard:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{opacity:1;transform:none;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-standard._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{transition:none}\n"], encapsulation: 2, data: { animation: [matFormFieldAnimations.transitionMessages] }, changeDetection: 0 });
MatFormField.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: core/* ChangeDetectorRef */.sBO },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [core/* ElementRef */.SBq,] }] },
    { type: bidi/* Directionality */.Is, decorators: [{ type: core/* Optional */.FiY }] },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_FORM_FIELD_DEFAULT_OPTIONS,] }] },
    { type: platform/* Platform */.t4 },
    { type: core/* NgZone */.R0b },
    { type: String, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb,] }] }
];
MatFormField.propDecorators = {
    appearance: [{ type: core/* Input */.IIB }],
    hideRequiredMarker: [{ type: core/* Input */.IIB }],
    hintLabel: [{ type: core/* Input */.IIB }],
    floatLabel: [{ type: core/* Input */.IIB }],
    underlineRef: [{ type: core/* ViewChild */.i9L, args: ['underline',] }],
    _connectionContainerRef: [{ type: core/* ViewChild */.i9L, args: ['connectionContainer', { static: true },] }],
    _inputContainerRef: [{ type: core/* ViewChild */.i9L, args: ['inputContainer',] }],
    _label: [{ type: core/* ViewChild */.i9L, args: ['label',] }],
    _controlNonStatic: [{ type: core/* ContentChild */.aQ5, args: [MatFormFieldControl,] }],
    _controlStatic: [{ type: core/* ContentChild */.aQ5, args: [MatFormFieldControl, { static: true },] }],
    _labelChildNonStatic: [{ type: core/* ContentChild */.aQ5, args: [MatLabel,] }],
    _labelChildStatic: [{ type: core/* ContentChild */.aQ5, args: [MatLabel, { static: true },] }],
    _placeholderChild: [{ type: core/* ContentChild */.aQ5, args: [MatPlaceholder,] }],
    _errorChildren: [{ type: core/* ContentChildren */.AcB, args: [MAT_ERROR, { descendants: true },] }],
    _hintChildren: [{ type: core/* ContentChildren */.AcB, args: [_MAT_HINT, { descendants: true },] }],
    _prefixChildren: [{ type: core/* ContentChildren */.AcB, args: [MAT_PREFIX, { descendants: true },] }],
    _suffixChildren: [{ type: core/* ContentChildren */.AcB, args: [MAT_SUFFIX, { descendants: true },] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatFormField, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-form-field',
                exportAs: 'matFormField',
                template: "<div class=\"mat-form-field-wrapper\">\n  <div class=\"mat-form-field-flex\" #connectionContainer\n       (click)=\"_control.onContainerClick && _control.onContainerClick($event)\">\n\n    <!-- Outline used for outline appearance. -->\n    <ng-container *ngIf=\"appearance == 'outline'\">\n      <div class=\"mat-form-field-outline\">\n        <div class=\"mat-form-field-outline-start\"></div>\n        <div class=\"mat-form-field-outline-gap\"></div>\n        <div class=\"mat-form-field-outline-end\"></div>\n      </div>\n      <div class=\"mat-form-field-outline mat-form-field-outline-thick\">\n        <div class=\"mat-form-field-outline-start\"></div>\n        <div class=\"mat-form-field-outline-gap\"></div>\n        <div class=\"mat-form-field-outline-end\"></div>\n      </div>\n    </ng-container>\n\n    <div class=\"mat-form-field-prefix\" *ngIf=\"_prefixChildren.length\">\n      <ng-content select=\"[matPrefix]\"></ng-content>\n    </div>\n\n    <div class=\"mat-form-field-infix\" #inputContainer>\n      <ng-content></ng-content>\n\n      <span class=\"mat-form-field-label-wrapper\">\n        <!-- We add aria-owns as a workaround for an issue in JAWS & NVDA where the label isn't\n             read if it comes before the control in the DOM. -->\n        <label class=\"mat-form-field-label\"\n               (cdkObserveContent)=\"updateOutlineGap()\"\n               [cdkObserveContentDisabled]=\"appearance != 'outline'\"\n               [id]=\"_labelId\"\n               [attr.for]=\"_control.id\"\n               [attr.aria-owns]=\"_control.id\"\n               [class.mat-empty]=\"_control.empty && !_shouldAlwaysFloat()\"\n               [class.mat-form-field-empty]=\"_control.empty && !_shouldAlwaysFloat()\"\n               [class.mat-accent]=\"color == 'accent'\"\n               [class.mat-warn]=\"color == 'warn'\"\n               #label\n               *ngIf=\"_hasFloatingLabel()\"\n               [ngSwitch]=\"_hasLabel()\">\n\n          <!-- @breaking-change 8.0.0 remove in favor of mat-label element an placeholder attr. -->\n          <ng-container *ngSwitchCase=\"false\">\n            <ng-content select=\"mat-placeholder\"></ng-content>\n            <span>{{_control.placeholder}}</span>\n          </ng-container>\n\n          <ng-content select=\"mat-label\" *ngSwitchCase=\"true\"></ng-content>\n\n          <!-- @breaking-change 8.0.0 remove `mat-placeholder-required` class -->\n          <span\n            class=\"mat-placeholder-required mat-form-field-required-marker\"\n            aria-hidden=\"true\"\n            *ngIf=\"!hideRequiredMarker && _control.required && !_control.disabled\">&#32;*</span>\n        </label>\n      </span>\n    </div>\n\n    <div class=\"mat-form-field-suffix\" *ngIf=\"_suffixChildren.length\">\n      <ng-content select=\"[matSuffix]\"></ng-content>\n    </div>\n  </div>\n\n  <!-- Underline used for legacy, standard, and box appearances. -->\n  <div class=\"mat-form-field-underline\" #underline\n       *ngIf=\"appearance != 'outline'\">\n    <span class=\"mat-form-field-ripple\"\n          [class.mat-accent]=\"color == 'accent'\"\n          [class.mat-warn]=\"color == 'warn'\"></span>\n  </div>\n\n  <div class=\"mat-form-field-subscript-wrapper\"\n       [ngSwitch]=\"_getDisplayedMessages()\">\n    <div *ngSwitchCase=\"'error'\" [@transitionMessages]=\"_subscriptAnimationState\">\n      <ng-content select=\"mat-error\"></ng-content>\n    </div>\n\n    <div class=\"mat-form-field-hint-wrapper\" *ngSwitchCase=\"'hint'\"\n      [@transitionMessages]=\"_subscriptAnimationState\">\n      <!-- TODO(mmalerba): use an actual <mat-hint> once all selectors are switched to mat-* -->\n      <div *ngIf=\"hintLabel\" [id]=\"_hintLabelId\" class=\"mat-hint\">{{hintLabel}}</div>\n      <ng-content select=\"mat-hint:not([align='end'])\"></ng-content>\n      <div class=\"mat-form-field-hint-spacer\"></div>\n      <ng-content select=\"mat-hint[align='end']\"></ng-content>\n    </div>\n  </div>\n</div>\n",
                animations: [matFormFieldAnimations.transitionMessages],
                host: {
                    'class': 'mat-form-field',
                    '[class.mat-form-field-appearance-standard]': 'appearance == "standard"',
                    '[class.mat-form-field-appearance-fill]': 'appearance == "fill"',
                    '[class.mat-form-field-appearance-outline]': 'appearance == "outline"',
                    '[class.mat-form-field-appearance-legacy]': 'appearance == "legacy"',
                    '[class.mat-form-field-invalid]': '_control.errorState',
                    '[class.mat-form-field-can-float]': '_canLabelFloat()',
                    '[class.mat-form-field-should-float]': '_shouldLabelFloat()',
                    '[class.mat-form-field-has-label]': '_hasFloatingLabel()',
                    '[class.mat-form-field-hide-placeholder]': '_hideControlPlaceholder()',
                    '[class.mat-form-field-disabled]': '_control.disabled',
                    '[class.mat-form-field-autofilled]': '_control.autofilled',
                    '[class.mat-focused]': '_control.focused',
                    '[class.ng-untouched]': '_shouldForward("untouched")',
                    '[class.ng-touched]': '_shouldForward("touched")',
                    '[class.ng-pristine]': '_shouldForward("pristine")',
                    '[class.ng-dirty]': '_shouldForward("dirty")',
                    '[class.ng-valid]': '_shouldForward("valid")',
                    '[class.ng-invalid]': '_shouldForward("invalid")',
                    '[class.ng-pending]': '_shouldForward("pending")',
                    '[class._mat-animation-noopable]': '!_animationsEnabled'
                },
                inputs: ['color'],
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                providers: [
                    { provide: MAT_FORM_FIELD, useExisting: MatFormField },
                ],
                styles: [".mat-form-field{display:inline-block;position:relative;text-align:left}[dir=rtl] .mat-form-field{text-align:right}.mat-form-field-wrapper{position:relative}.mat-form-field-flex{display:inline-flex;align-items:baseline;box-sizing:border-box;width:100%}.mat-form-field-prefix,.mat-form-field-suffix{white-space:nowrap;flex:none;position:relative}.mat-form-field-infix{display:block;position:relative;flex:auto;min-width:0;width:180px}.cdk-high-contrast-active .mat-form-field-infix{border-image:linear-gradient(transparent, transparent)}.mat-form-field-label-wrapper{position:absolute;left:0;box-sizing:content-box;width:100%;height:100%;overflow:hidden;pointer-events:none}[dir=rtl] .mat-form-field-label-wrapper{left:auto;right:0}.mat-form-field-label{position:absolute;left:0;font:inherit;pointer-events:none;width:100%;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;transform-origin:0 0;transition:transform 400ms cubic-bezier(0.25, 0.8, 0.25, 1),color 400ms cubic-bezier(0.25, 0.8, 0.25, 1),width 400ms cubic-bezier(0.25, 0.8, 0.25, 1);display:none}[dir=rtl] .mat-form-field-label{transform-origin:100% 0;left:auto;right:0}.mat-form-field-empty.mat-form-field-label,.mat-form-field-can-float.mat-form-field-should-float .mat-form-field-label{display:block}.mat-form-field-autofill-control:-webkit-autofill+.mat-form-field-label-wrapper .mat-form-field-label{display:none}.mat-form-field-can-float .mat-form-field-autofill-control:-webkit-autofill+.mat-form-field-label-wrapper .mat-form-field-label{display:block;transition:none}.mat-input-server:focus+.mat-form-field-label-wrapper .mat-form-field-label,.mat-input-server[placeholder]:not(:placeholder-shown)+.mat-form-field-label-wrapper .mat-form-field-label{display:none}.mat-form-field-can-float .mat-input-server:focus+.mat-form-field-label-wrapper .mat-form-field-label,.mat-form-field-can-float .mat-input-server[placeholder]:not(:placeholder-shown)+.mat-form-field-label-wrapper .mat-form-field-label{display:block}.mat-form-field-label:not(.mat-form-field-empty){transition:none}.mat-form-field-underline{position:absolute;width:100%;pointer-events:none;transform:scale3d(1, 1.0001, 1)}.mat-form-field-ripple{position:absolute;left:0;width:100%;transform-origin:50%;transform:scaleX(0.5);opacity:0;transition:background-color 300ms cubic-bezier(0.55, 0, 0.55, 0.2)}.mat-form-field.mat-focused .mat-form-field-ripple,.mat-form-field.mat-form-field-invalid .mat-form-field-ripple{opacity:1;transform:none;transition:transform 300ms cubic-bezier(0.25, 0.8, 0.25, 1),opacity 100ms cubic-bezier(0.25, 0.8, 0.25, 1),background-color 300ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-subscript-wrapper{position:absolute;box-sizing:border-box;width:100%;overflow:hidden}.mat-form-field-subscript-wrapper .mat-icon,.mat-form-field-label-wrapper .mat-icon{width:1em;height:1em;font-size:inherit;vertical-align:baseline}.mat-form-field-hint-wrapper{display:flex}.mat-form-field-hint-spacer{flex:1 0 1em}.mat-error{display:block}.mat-form-field-control-wrapper{position:relative}.mat-form-field-hint-end{order:1}.mat-form-field._mat-animation-noopable .mat-form-field-label,.mat-form-field._mat-animation-noopable .mat-form-field-ripple{transition:none}\n", ".mat-form-field-appearance-fill .mat-form-field-flex{border-radius:4px 4px 0 0;padding:.75em .75em 0 .75em}.cdk-high-contrast-active .mat-form-field-appearance-fill .mat-form-field-flex{outline:solid 1px}.cdk-high-contrast-active .mat-form-field-appearance-fill.mat-focused .mat-form-field-flex{outline:dashed 3px}.mat-form-field-appearance-fill .mat-form-field-underline::before{content:\"\";display:block;position:absolute;bottom:0;height:1px;width:100%}.mat-form-field-appearance-fill .mat-form-field-ripple{bottom:0;height:2px}.cdk-high-contrast-active .mat-form-field-appearance-fill .mat-form-field-ripple{height:0}.mat-form-field-appearance-fill:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{opacity:1;transform:none;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-fill._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{transition:none}.mat-form-field-appearance-fill .mat-form-field-subscript-wrapper{padding:0 1em}\n", ".mat-input-element{font:inherit;background:transparent;color:currentColor;border:none;outline:none;padding:0;margin:0;width:100%;max-width:100%;vertical-align:bottom;text-align:inherit;box-sizing:content-box}.mat-input-element:-moz-ui-invalid{box-shadow:none}.mat-input-element::-ms-clear,.mat-input-element::-ms-reveal{display:none}.mat-input-element,.mat-input-element::-webkit-search-cancel-button,.mat-input-element::-webkit-search-decoration,.mat-input-element::-webkit-search-results-button,.mat-input-element::-webkit-search-results-decoration{-webkit-appearance:none}.mat-input-element::-webkit-contacts-auto-fill-button,.mat-input-element::-webkit-caps-lock-indicator,.mat-input-element::-webkit-credentials-auto-fill-button{visibility:hidden}.mat-input-element[type=date],.mat-input-element[type=datetime],.mat-input-element[type=datetime-local],.mat-input-element[type=month],.mat-input-element[type=week],.mat-input-element[type=time]{line-height:1}.mat-input-element[type=date]::after,.mat-input-element[type=datetime]::after,.mat-input-element[type=datetime-local]::after,.mat-input-element[type=month]::after,.mat-input-element[type=week]::after,.mat-input-element[type=time]::after{content:\" \";white-space:pre;width:1px}.mat-input-element::-webkit-inner-spin-button,.mat-input-element::-webkit-calendar-picker-indicator,.mat-input-element::-webkit-clear-button{font-size:.75em}.mat-input-element::placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element::-moz-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::-moz-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element::-webkit-input-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element::-webkit-input-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-input-element:-ms-input-placeholder{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-input-element:-ms-input-placeholder:-ms-input-placeholder{-ms-user-select:text}.mat-form-field-hide-placeholder .mat-input-element::placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element::-moz-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::-moz-placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element::-webkit-input-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element::-webkit-input-placeholder{opacity:0}.mat-form-field-hide-placeholder .mat-input-element:-ms-input-placeholder{color:transparent !important;-webkit-text-fill-color:transparent;transition:none}.cdk-high-contrast-active .mat-form-field-hide-placeholder .mat-input-element:-ms-input-placeholder{opacity:0}textarea.mat-input-element{resize:vertical;overflow:auto}textarea.mat-input-element.cdk-textarea-autosize{resize:none}textarea.mat-input-element{padding:2px 0;margin:-2px 0}select.mat-input-element{-moz-appearance:none;-webkit-appearance:none;position:relative;background-color:transparent;display:inline-flex;box-sizing:border-box;padding-top:1em;top:-1em;margin-bottom:-1em}select.mat-input-element::-ms-expand{display:none}select.mat-input-element::-moz-focus-inner{border:0}select.mat-input-element:not(:disabled){cursor:pointer}select.mat-input-element::-ms-value{color:inherit;background:none}.mat-focused .cdk-high-contrast-active select.mat-input-element::-ms-value{color:inherit}.mat-form-field-type-mat-native-select .mat-form-field-infix::after{content:\"\";width:0;height:0;border-left:5px solid transparent;border-right:5px solid transparent;border-top:5px solid;position:absolute;top:50%;right:0;margin-top:-2.5px;pointer-events:none}[dir=rtl] .mat-form-field-type-mat-native-select .mat-form-field-infix::after{right:auto;left:0}.mat-form-field-type-mat-native-select .mat-input-element{padding-right:15px}[dir=rtl] .mat-form-field-type-mat-native-select .mat-input-element{padding-right:0;padding-left:15px}.mat-form-field-type-mat-native-select .mat-form-field-label-wrapper{max-width:calc(100% - 10px)}.mat-form-field-type-mat-native-select.mat-form-field-appearance-outline .mat-form-field-infix::after{margin-top:-5px}.mat-form-field-type-mat-native-select.mat-form-field-appearance-fill .mat-form-field-infix::after{margin-top:-10px}\n", ".mat-form-field-appearance-legacy .mat-form-field-label{transform:perspective(100px);-ms-transform:none}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon{width:1em}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon-button,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon-button{font:inherit;vertical-align:baseline}.mat-form-field-appearance-legacy .mat-form-field-prefix .mat-icon-button .mat-icon,.mat-form-field-appearance-legacy .mat-form-field-suffix .mat-icon-button .mat-icon{font-size:inherit}.mat-form-field-appearance-legacy .mat-form-field-underline{height:1px}.cdk-high-contrast-active .mat-form-field-appearance-legacy .mat-form-field-underline{height:0;border-top:solid 1px}.mat-form-field-appearance-legacy .mat-form-field-ripple{top:0;height:2px;overflow:hidden}.cdk-high-contrast-active .mat-form-field-appearance-legacy .mat-form-field-ripple{height:0;border-top:solid 2px}.mat-form-field-appearance-legacy.mat-form-field-disabled .mat-form-field-underline{background-position:0;background-color:transparent}.cdk-high-contrast-active .mat-form-field-appearance-legacy.mat-form-field-disabled .mat-form-field-underline{border-top-style:dotted;border-top-width:2px}.mat-form-field-appearance-legacy.mat-form-field-invalid:not(.mat-focused) .mat-form-field-ripple{height:1px}\n", ".mat-form-field-appearance-outline .mat-form-field-wrapper{margin:.25em 0}.mat-form-field-appearance-outline .mat-form-field-flex{padding:0 .75em 0 .75em;margin-top:-0.25em;position:relative}.mat-form-field-appearance-outline .mat-form-field-prefix,.mat-form-field-appearance-outline .mat-form-field-suffix{top:.25em}.mat-form-field-appearance-outline .mat-form-field-outline{display:flex;position:absolute;top:.25em;left:0;right:0;bottom:0;pointer-events:none}.mat-form-field-appearance-outline .mat-form-field-outline-start,.mat-form-field-appearance-outline .mat-form-field-outline-end{border:1px solid currentColor;min-width:5px}.mat-form-field-appearance-outline .mat-form-field-outline-start{border-radius:5px 0 0 5px;border-right-style:none}[dir=rtl] .mat-form-field-appearance-outline .mat-form-field-outline-start{border-right-style:solid;border-left-style:none;border-radius:0 5px 5px 0}.mat-form-field-appearance-outline .mat-form-field-outline-end{border-radius:0 5px 5px 0;border-left-style:none;flex-grow:1}[dir=rtl] .mat-form-field-appearance-outline .mat-form-field-outline-end{border-left-style:solid;border-right-style:none;border-radius:5px 0 0 5px}.mat-form-field-appearance-outline .mat-form-field-outline-gap{border-radius:.000001px;border:1px solid currentColor;border-left-style:none;border-right-style:none}.mat-form-field-appearance-outline.mat-form-field-can-float.mat-form-field-should-float .mat-form-field-outline-gap{border-top-color:transparent}.mat-form-field-appearance-outline .mat-form-field-outline-thick{opacity:0}.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-start,.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-end,.mat-form-field-appearance-outline .mat-form-field-outline-thick .mat-form-field-outline-gap{border-width:2px}.mat-form-field-appearance-outline.mat-focused .mat-form-field-outline,.mat-form-field-appearance-outline.mat-form-field-invalid .mat-form-field-outline{opacity:0;transition:opacity 100ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-outline.mat-focused .mat-form-field-outline-thick,.mat-form-field-appearance-outline.mat-form-field-invalid .mat-form-field-outline-thick{opacity:1}.cdk-high-contrast-active .mat-form-field-appearance-outline.mat-focused .mat-form-field-outline-thick{border:3px dashed}.mat-form-field-appearance-outline:not(.mat-form-field-disabled) .mat-form-field-flex:hover .mat-form-field-outline{opacity:0;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-outline:not(.mat-form-field-disabled) .mat-form-field-flex:hover .mat-form-field-outline-thick{opacity:1}.mat-form-field-appearance-outline .mat-form-field-subscript-wrapper{padding:0 1em}.mat-form-field-appearance-outline._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-outline,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-start,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-end,.mat-form-field-appearance-outline._mat-animation-noopable .mat-form-field-outline-gap{transition:none}\n", ".mat-form-field-appearance-standard .mat-form-field-flex{padding-top:.75em}.mat-form-field-appearance-standard .mat-form-field-underline{height:1px}.cdk-high-contrast-active .mat-form-field-appearance-standard .mat-form-field-underline{height:0;border-top:solid 1px}.mat-form-field-appearance-standard .mat-form-field-ripple{bottom:0;height:2px}.cdk-high-contrast-active .mat-form-field-appearance-standard .mat-form-field-ripple{height:0;border-top:solid 2px}.mat-form-field-appearance-standard.mat-form-field-disabled .mat-form-field-underline{background-position:0;background-color:transparent}.cdk-high-contrast-active .mat-form-field-appearance-standard.mat-form-field-disabled .mat-form-field-underline{border-top-style:dotted;border-top-width:2px}.mat-form-field-appearance-standard:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{opacity:1;transform:none;transition:opacity 600ms cubic-bezier(0.25, 0.8, 0.25, 1)}.mat-form-field-appearance-standard._mat-animation-noopable:not(.mat-form-field-disabled) .mat-form-field-flex:hover~.mat-form-field-underline .mat-form-field-ripple{transition:none}\n"]
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: core/* ChangeDetectorRef */.sBO }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [core/* ElementRef */.SBq]
            }] }, { type: bidi/* Directionality */.Is, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_FORM_FIELD_DEFAULT_OPTIONS]
            }] }, { type: platform/* Platform */.t4 }, { type: core/* NgZone */.R0b }, { type: String, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb]
            }] }]; }, { floatLabel: [{
            type: core/* Input */.IIB
        }], appearance: [{
            type: core/* Input */.IIB
        }], hideRequiredMarker: [{
            type: core/* Input */.IIB
        }], hintLabel: [{
            type: core/* Input */.IIB
        }], underlineRef: [{
            type: core/* ViewChild */.i9L,
            args: ['underline']
        }], _connectionContainerRef: [{
            type: core/* ViewChild */.i9L,
            args: ['connectionContainer', { static: true }]
        }], _inputContainerRef: [{
            type: core/* ViewChild */.i9L,
            args: ['inputContainer']
        }], _label: [{
            type: core/* ViewChild */.i9L,
            args: ['label']
        }], _controlNonStatic: [{
            type: core/* ContentChild */.aQ5,
            args: [MatFormFieldControl]
        }], _controlStatic: [{
            type: core/* ContentChild */.aQ5,
            args: [MatFormFieldControl, { static: true }]
        }], _labelChildNonStatic: [{
            type: core/* ContentChild */.aQ5,
            args: [MatLabel]
        }], _labelChildStatic: [{
            type: core/* ContentChild */.aQ5,
            args: [MatLabel, { static: true }]
        }], _placeholderChild: [{
            type: core/* ContentChild */.aQ5,
            args: [MatPlaceholder]
        }], _errorChildren: [{
            type: core/* ContentChildren */.AcB,
            args: [MAT_ERROR, { descendants: true }]
        }], _hintChildren: [{
            type: core/* ContentChildren */.AcB,
            args: [_MAT_HINT, { descendants: true }]
        }], _prefixChildren: [{
            type: core/* ContentChildren */.AcB,
            args: [MAT_PREFIX, { descendants: true }]
        }], _suffixChildren: [{
            type: core/* ContentChildren */.AcB,
            args: [MAT_SUFFIX, { descendants: true }]
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatFormFieldModule {
}
MatFormFieldModule.ɵfac = function MatFormFieldModule_Factory(t) { return new (t || MatFormFieldModule)(); };
MatFormFieldModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatFormFieldModule });
MatFormFieldModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[
            common/* CommonModule */.ez,
            fesm2015_core/* MatCommonModule */.BQ,
            observers/* ObserversModule */.Q8,
        ], fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatFormFieldModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                declarations: [
                    MatError,
                    MatFormField,
                    MatHint,
                    MatLabel,
                    MatPlaceholder,
                    MatPrefix,
                    MatSuffix,
                ],
                imports: [
                    common/* CommonModule */.ez,
                    fesm2015_core/* MatCommonModule */.BQ,
                    observers/* ObserversModule */.Q8,
                ],
                exports: [
                    fesm2015_core/* MatCommonModule */.BQ,
                    MatError,
                    MatFormField,
                    MatHint,
                    MatLabel,
                    MatPlaceholder,
                    MatPrefix,
                    MatSuffix,
                ]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatFormFieldModule, { declarations: function () { return [MatError, MatFormField, MatHint, MatLabel, MatPlaceholder, MatPrefix, MatSuffix]; }, imports: function () { return [common/* CommonModule */.ez,
        fesm2015_core/* MatCommonModule */.BQ,
        observers/* ObserversModule */.Q8]; }, exports: function () { return [fesm2015_core/* MatCommonModule */.BQ, MatError, MatFormField, MatHint, MatLabel, MatPlaceholder, MatPrefix, MatSuffix]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=form-field.js.map
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/input.js









/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Directive to automatically resize a textarea to fit its content.
 * @deprecated Use `cdkTextareaAutosize` from `@angular/cdk/text-field` instead.
 * @breaking-change 8.0.0
 */






class MatTextareaAutosize extends CdkTextareaAutosize {
    get matAutosizeMinRows() { return this.minRows; }
    set matAutosizeMinRows(value) { this.minRows = value; }
    get matAutosizeMaxRows() { return this.maxRows; }
    set matAutosizeMaxRows(value) { this.maxRows = value; }
    get matAutosize() { return this.enabled; }
    set matAutosize(value) { this.enabled = value; }
    get matTextareaAutosize() { return this.enabled; }
    set matTextareaAutosize(value) { this.enabled = value; }
}
MatTextareaAutosize.ɵfac = /*@__PURE__*/ function () { let ɵMatTextareaAutosize_BaseFactory; return function MatTextareaAutosize_Factory(t) { return (ɵMatTextareaAutosize_BaseFactory || (ɵMatTextareaAutosize_BaseFactory = core/* ɵɵgetInheritedFactory */.n5z(MatTextareaAutosize)))(t || MatTextareaAutosize); }; }();
MatTextareaAutosize.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatTextareaAutosize, selectors: [["textarea", "mat-autosize", ""], ["textarea", "matTextareaAutosize", ""]], hostAttrs: ["rows", "1", 1, "cdk-textarea-autosize", "mat-autosize"], inputs: { cdkAutosizeMinRows: "cdkAutosizeMinRows", cdkAutosizeMaxRows: "cdkAutosizeMaxRows", matAutosizeMinRows: "matAutosizeMinRows", matAutosizeMaxRows: "matAutosizeMaxRows", matAutosize: ["mat-autosize", "matAutosize"], matTextareaAutosize: "matTextareaAutosize" }, exportAs: ["matTextareaAutosize"], features: [core/* ɵɵInheritDefinitionFeature */.qOj] });
MatTextareaAutosize.propDecorators = {
    matAutosizeMinRows: [{ type: core/* Input */.IIB }],
    matAutosizeMaxRows: [{ type: core/* Input */.IIB }],
    matAutosize: [{ type: core/* Input */.IIB, args: ['mat-autosize',] }],
    matTextareaAutosize: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatTextareaAutosize, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'textarea[mat-autosize], textarea[matTextareaAutosize]',
                exportAs: 'matTextareaAutosize',
                inputs: ['cdkAutosizeMinRows', 'cdkAutosizeMaxRows'],
                host: {
                    'class': 'cdk-textarea-autosize mat-autosize',
                    // Textarea elements that have the directive applied should have a single row by default.
                    // Browsers normally show two rows by default and therefore this limits the minRows binding.
                    'rows': '1'
                }
            }]
    }], null, { matAutosizeMinRows: [{
            type: core/* Input */.IIB
        }], matAutosizeMaxRows: [{
            type: core/* Input */.IIB
        }], matAutosize: [{
            type: core/* Input */.IIB,
            args: ['mat-autosize']
        }], matTextareaAutosize: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** @docs-private */
function getMatInputUnsupportedTypeError(type) {
    return Error(`Input type "${type}" isn't supported by matInput.`);
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * This token is used to inject the object whose value should be set into `MatInput`. If none is
 * provided, the native `HTMLInputElement` is used. Directives like `MatDatepickerInput` can provide
 * themselves for this token, in order to make `MatInput` delegate the getting and setting of the
 * value to them.
 */
const MAT_INPUT_VALUE_ACCESSOR = new core/* InjectionToken */.OlP('MAT_INPUT_VALUE_ACCESSOR');

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
// Invalid input type. Using one of these will throw an MatInputUnsupportedTypeError.
const MAT_INPUT_INVALID_TYPES = [
    'button',
    'checkbox',
    'file',
    'hidden',
    'image',
    'radio',
    'range',
    'reset',
    'submit'
];
let input_nextUniqueId = 0;
// Boilerplate for applying mixins to MatInput.
/** @docs-private */
const _MatInputBase = (0,fesm2015_core/* mixinErrorState */.FD)(class {
    constructor(_defaultErrorStateMatcher, _parentForm, _parentFormGroup, 
    /** @docs-private */
    ngControl) {
        this._defaultErrorStateMatcher = _defaultErrorStateMatcher;
        this._parentForm = _parentForm;
        this._parentFormGroup = _parentFormGroup;
        this.ngControl = ngControl;
    }
});
/** Directive that allows a native input to work inside a `MatFormField`. */
class MatInput extends _MatInputBase {
    constructor(_elementRef, _platform, ngControl, _parentForm, _parentFormGroup, _defaultErrorStateMatcher, inputValueAccessor, _autofillMonitor, ngZone, 
    // TODO: Remove this once the legacy appearance has been removed. We only need
    // to inject the form-field for determining whether the placeholder has been promoted.
    _formField) {
        super(_defaultErrorStateMatcher, _parentForm, _parentFormGroup, ngControl);
        this._elementRef = _elementRef;
        this._platform = _platform;
        this._autofillMonitor = _autofillMonitor;
        this._formField = _formField;
        this._uid = `mat-input-${input_nextUniqueId++}`;
        /**
         * Implemented as part of MatFormFieldControl.
         * @docs-private
         */
        this.focused = false;
        /**
         * Implemented as part of MatFormFieldControl.
         * @docs-private
         */
        this.stateChanges = new Subject/* Subject */.xQ();
        /**
         * Implemented as part of MatFormFieldControl.
         * @docs-private
         */
        this.controlType = 'mat-input';
        /**
         * Implemented as part of MatFormFieldControl.
         * @docs-private
         */
        this.autofilled = false;
        this._disabled = false;
        this._required = false;
        this._type = 'text';
        this._readonly = false;
        this._neverEmptyInputTypes = [
            'date',
            'datetime',
            'datetime-local',
            'month',
            'time',
            'week'
        ].filter(t => (0,platform/* getSupportedInputTypes */.qK)().has(t));
        const element = this._elementRef.nativeElement;
        const nodeName = element.nodeName.toLowerCase();
        // If no input value accessor was explicitly specified, use the element as the input value
        // accessor.
        this._inputValueAccessor = inputValueAccessor || element;
        this._previousNativeValue = this.value;
        // Force setter to be called in case id was not specified.
        this.id = this.id;
        // On some versions of iOS the caret gets stuck in the wrong place when holding down the delete
        // key. In order to get around this we need to "jiggle" the caret loose. Since this bug only
        // exists on iOS, we only bother to install the listener on iOS.
        if (_platform.IOS) {
            ngZone.runOutsideAngular(() => {
                _elementRef.nativeElement.addEventListener('keyup', (event) => {
                    const el = event.target;
                    // Note: We specifically check for 0, rather than `!el.selectionStart`, because the two
                    // indicate different things. If the value is 0, it means that the caret is at the start
                    // of the input, whereas a value of `null` means that the input doesn't support
                    // manipulating the selection range. Inputs that don't support setting the selection range
                    // will throw an error so we want to avoid calling `setSelectionRange` on them. See:
                    // https://html.spec.whatwg.org/multipage/input.html#do-not-apply
                    if (!el.value && el.selectionStart === 0 && el.selectionEnd === 0) {
                        // Note: Just setting `0, 0` doesn't fix the issue. Setting
                        // `1, 1` fixes it for the first time that you type text and
                        // then hold delete. Toggling to `1, 1` and then back to
                        // `0, 0` seems to completely fix it.
                        el.setSelectionRange(1, 1);
                        el.setSelectionRange(0, 0);
                    }
                });
            });
        }
        this._isServer = !this._platform.isBrowser;
        this._isNativeSelect = nodeName === 'select';
        this._isTextarea = nodeName === 'textarea';
        this._isInFormField = !!_formField;
        if (this._isNativeSelect) {
            this.controlType = element.multiple ? 'mat-native-select-multiple' :
                'mat-native-select';
        }
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get disabled() {
        if (this.ngControl && this.ngControl.disabled !== null) {
            return this.ngControl.disabled;
        }
        return this._disabled;
    }
    set disabled(value) {
        this._disabled = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        // Browsers may not fire the blur event if the input is disabled too quickly.
        // Reset from here to ensure that the element doesn't become stuck.
        if (this.focused) {
            this.focused = false;
            this.stateChanges.next();
        }
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get id() { return this._id; }
    set id(value) { this._id = value || this._uid; }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get required() { return this._required; }
    set required(value) { this._required = (0,coercion/* coerceBooleanProperty */.Ig)(value); }
    /** Input type of the element. */
    get type() { return this._type; }
    set type(value) {
        this._type = value || 'text';
        this._validateType();
        // When using Angular inputs, developers are no longer able to set the properties on the native
        // input element. To ensure that bindings for `type` work, we need to sync the setter
        // with the native property. Textarea elements don't support the type property or attribute.
        if (!this._isTextarea && (0,platform/* getSupportedInputTypes */.qK)().has(this._type)) {
            this._elementRef.nativeElement.type = this._type;
        }
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get value() { return this._inputValueAccessor.value; }
    set value(value) {
        if (value !== this.value) {
            this._inputValueAccessor.value = value;
            this.stateChanges.next();
        }
    }
    /** Whether the element is readonly. */
    get readonly() { return this._readonly; }
    set readonly(value) { this._readonly = (0,coercion/* coerceBooleanProperty */.Ig)(value); }
    ngAfterViewInit() {
        if (this._platform.isBrowser) {
            this._autofillMonitor.monitor(this._elementRef.nativeElement).subscribe(event => {
                this.autofilled = event.isAutofilled;
                this.stateChanges.next();
            });
        }
    }
    ngOnChanges() {
        this.stateChanges.next();
    }
    ngOnDestroy() {
        this.stateChanges.complete();
        if (this._platform.isBrowser) {
            this._autofillMonitor.stopMonitoring(this._elementRef.nativeElement);
        }
    }
    ngDoCheck() {
        if (this.ngControl) {
            // We need to re-evaluate this on every change detection cycle, because there are some
            // error triggers that we can't subscribe to (e.g. parent form submissions). This means
            // that whatever logic is in here has to be super lean or we risk destroying the performance.
            this.updateErrorState();
        }
        // We need to dirty-check the native element's value, because there are some cases where
        // we won't be notified when it changes (e.g. the consumer isn't using forms or they're
        // updating the value using `emitEvent: false`).
        this._dirtyCheckNativeValue();
        // We need to dirty-check and set the placeholder attribute ourselves, because whether it's
        // present or not depends on a query which is prone to "changed after checked" errors.
        this._dirtyCheckPlaceholder();
    }
    /** Focuses the input. */
    focus(options) {
        this._elementRef.nativeElement.focus(options);
    }
    // We have to use a `HostListener` here in order to support both Ivy and ViewEngine.
    // In Ivy the `host` bindings will be merged when this class is extended, whereas in
    // ViewEngine they're overwritten.
    // TODO(crisbeto): we move this back into `host` once Ivy is turned on by default.
    /** Callback for the cases where the focused state of the input changes. */
    // tslint:disable:no-host-decorator-in-concrete
    // tslint:enable:no-host-decorator-in-concrete
    _focusChanged(isFocused) {
        if (isFocused !== this.focused) {
            this.focused = isFocused;
            this.stateChanges.next();
        }
    }
    // We have to use a `HostListener` here in order to support both Ivy and ViewEngine.
    // In Ivy the `host` bindings will be merged when this class is extended, whereas in
    // ViewEngine they're overwritten.
    // TODO(crisbeto): we move this back into `host` once Ivy is turned on by default.
    // tslint:disable-next-line:no-host-decorator-in-concrete
    _onInput() {
        // This is a noop function and is used to let Angular know whenever the value changes.
        // Angular will run a new change detection each time the `input` event has been dispatched.
        // It's necessary that Angular recognizes the value change, because when floatingLabel
        // is set to false and Angular forms aren't used, the placeholder won't recognize the
        // value changes and will not disappear.
        // Listening to the input event wouldn't be necessary when the input is using the
        // FormsModule or ReactiveFormsModule, because Angular forms also listens to input events.
    }
    /** Does some manual dirty checking on the native input `placeholder` attribute. */
    _dirtyCheckPlaceholder() {
        var _a, _b;
        // If we're hiding the native placeholder, it should also be cleared from the DOM, otherwise
        // screen readers will read it out twice: once from the label and once from the attribute.
        // TODO: can be removed once we get rid of the `legacy` style for the form field, because it's
        // the only one that supports promoting the placeholder to a label.
        const placeholder = ((_b = (_a = this._formField) === null || _a === void 0 ? void 0 : _a._hideControlPlaceholder) === null || _b === void 0 ? void 0 : _b.call(_a)) ? null : this.placeholder;
        if (placeholder !== this._previousPlaceholder) {
            const element = this._elementRef.nativeElement;
            this._previousPlaceholder = placeholder;
            placeholder ?
                element.setAttribute('placeholder', placeholder) : element.removeAttribute('placeholder');
        }
    }
    /** Does some manual dirty checking on the native input `value` property. */
    _dirtyCheckNativeValue() {
        const newValue = this._elementRef.nativeElement.value;
        if (this._previousNativeValue !== newValue) {
            this._previousNativeValue = newValue;
            this.stateChanges.next();
        }
    }
    /** Make sure the input is a supported type. */
    _validateType() {
        if (MAT_INPUT_INVALID_TYPES.indexOf(this._type) > -1 &&
            (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getMatInputUnsupportedTypeError(this._type);
        }
    }
    /** Checks whether the input type is one of the types that are never empty. */
    _isNeverEmpty() {
        return this._neverEmptyInputTypes.indexOf(this._type) > -1;
    }
    /** Checks whether the input is invalid based on the native validation. */
    _isBadInput() {
        // The `validity` property won't be present on platform-server.
        let validity = this._elementRef.nativeElement.validity;
        return validity && validity.badInput;
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get empty() {
        return !this._isNeverEmpty() && !this._elementRef.nativeElement.value && !this._isBadInput() &&
            !this.autofilled;
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get shouldLabelFloat() {
        if (this._isNativeSelect) {
            // For a single-selection `<select>`, the label should float when the selected option has
            // a non-empty display value. For a `<select multiple>`, the label *always* floats to avoid
            // overlapping the label with the options.
            const selectElement = this._elementRef.nativeElement;
            const firstOption = selectElement.options[0];
            // On most browsers the `selectedIndex` will always be 0, however on IE and Edge it'll be
            // -1 if the `value` is set to something, that isn't in the list of options, at a later point.
            return this.focused || selectElement.multiple || !this.empty ||
                !!(selectElement.selectedIndex > -1 && firstOption && firstOption.label);
        }
        else {
            return this.focused || !this.empty;
        }
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    setDescribedByIds(ids) {
        if (ids.length) {
            this._elementRef.nativeElement.setAttribute('aria-describedby', ids.join(' '));
        }
        else {
            this._elementRef.nativeElement.removeAttribute('aria-describedby');
        }
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    onContainerClick() {
        // Do not re-focus the input element if the element is already focused. Otherwise it can happen
        // that someone clicks on a time input and the cursor resets to the "hours" field while the
        // "minutes" field was actually clicked. See: https://github.com/angular/components/issues/12849
        if (!this.focused) {
            this.focus();
        }
    }
}
MatInput.ɵfac = function MatInput_Factory(t) { return new (t || MatInput)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(platform/* Platform */.t4), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* NgControl */.a5, 10), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* NgForm */.F, 8), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* FormGroupDirective */.sg, 8), core/* ɵɵdirectiveInject */.Y36(fesm2015_core/* ErrorStateMatcher */.rD), core/* ɵɵdirectiveInject */.Y36(MAT_INPUT_VALUE_ACCESSOR, 10), core/* ɵɵdirectiveInject */.Y36(AutofillMonitor), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(MAT_FORM_FIELD, 8)); };
MatInput.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatInput, selectors: [["input", "matInput", ""], ["textarea", "matInput", ""], ["select", "matNativeControl", ""], ["input", "matNativeControl", ""], ["textarea", "matNativeControl", ""]], hostAttrs: [1, "mat-input-element", "mat-form-field-autofill-control"], hostVars: 9, hostBindings: function MatInput_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("focus", function MatInput_focus_HostBindingHandler() { return ctx._focusChanged(true); })("blur", function MatInput_blur_HostBindingHandler() { return ctx._focusChanged(false); })("input", function MatInput_input_HostBindingHandler() { return ctx._onInput(); });
    } if (rf & 2) {
        core/* ɵɵhostProperty */.Ikx("disabled", ctx.disabled)("required", ctx.required);
        core/* ɵɵattribute */.uIk("id", ctx.id)("data-placeholder", ctx.placeholder)("readonly", ctx.readonly && !ctx._isNativeSelect || null)("aria-invalid", ctx.empty && ctx.required ? null : ctx.errorState)("aria-required", ctx.required);
        core/* ɵɵclassProp */.ekj("mat-input-server", ctx._isServer);
    } }, inputs: { id: "id", disabled: "disabled", required: "required", type: "type", value: "value", readonly: "readonly", placeholder: "placeholder", errorStateMatcher: "errorStateMatcher", userAriaDescribedBy: ["aria-describedby", "userAriaDescribedBy"] }, exportAs: ["matInput"], features: [core/* ɵɵProvidersFeature */._Bn([{ provide: MatFormFieldControl, useExisting: MatInput }]), core/* ɵɵInheritDefinitionFeature */.qOj, core/* ɵɵNgOnChangesFeature */.TTD] });
MatInput.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: platform/* Platform */.t4 },
    { type: fesm2015_forms/* NgControl */.a5, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Self */.PiD }] },
    { type: fesm2015_forms/* NgForm */.F, decorators: [{ type: core/* Optional */.FiY }] },
    { type: fesm2015_forms/* FormGroupDirective */.sg, decorators: [{ type: core/* Optional */.FiY }] },
    { type: fesm2015_core/* ErrorStateMatcher */.rD },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Self */.PiD }, { type: core/* Inject */.tBr, args: [MAT_INPUT_VALUE_ACCESSOR,] }] },
    { type: AutofillMonitor },
    { type: core/* NgZone */.R0b },
    { type: MatFormField, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_FORM_FIELD,] }] }
];
MatInput.propDecorators = {
    disabled: [{ type: core/* Input */.IIB }],
    id: [{ type: core/* Input */.IIB }],
    placeholder: [{ type: core/* Input */.IIB }],
    required: [{ type: core/* Input */.IIB }],
    type: [{ type: core/* Input */.IIB }],
    errorStateMatcher: [{ type: core/* Input */.IIB }],
    userAriaDescribedBy: [{ type: core/* Input */.IIB, args: ['aria-describedby',] }],
    value: [{ type: core/* Input */.IIB }],
    readonly: [{ type: core/* Input */.IIB }],
    _focusChanged: [{ type: core/* HostListener */.L6J, args: ['focus', ['true'],] }, { type: core/* HostListener */.L6J, args: ['blur', ['false'],] }],
    _onInput: [{ type: core/* HostListener */.L6J, args: ['input',] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatInput, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: `input[matInput], textarea[matInput], select[matNativeControl],
      input[matNativeControl], textarea[matNativeControl]`,
                exportAs: 'matInput',
                host: {
                    /**
                     * @breaking-change 8.0.0 remove .mat-form-field-autofill-control in favor of AutofillMonitor.
                     */
                    'class': 'mat-input-element mat-form-field-autofill-control',
                    '[class.mat-input-server]': '_isServer',
                    // Native input properties that are overwritten by Angular inputs need to be synced with
                    // the native input element. Otherwise property bindings for those don't work.
                    '[attr.id]': 'id',
                    // At the time of writing, we have a lot of customer tests that look up the input based on its
                    // placeholder. Since we sometimes omit the placeholder attribute from the DOM to prevent screen
                    // readers from reading it twice, we have to keep it somewhere in the DOM for the lookup.
                    '[attr.data-placeholder]': 'placeholder',
                    '[disabled]': 'disabled',
                    '[required]': 'required',
                    '[attr.readonly]': 'readonly && !_isNativeSelect || null',
                    // Only mark the input as invalid for assistive technology if it has a value since the
                    // state usually overlaps with `aria-required` when the input is empty and can be redundant.
                    '[attr.aria-invalid]': '(empty && required) ? null : errorState',
                    '[attr.aria-required]': 'required'
                },
                providers: [{ provide: MatFormFieldControl, useExisting: MatInput }]
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: platform/* Platform */.t4 }, { type: fesm2015_forms/* NgControl */.a5, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Self */.PiD
            }] }, { type: fesm2015_forms/* NgForm */.F, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: fesm2015_forms/* FormGroupDirective */.sg, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: fesm2015_core/* ErrorStateMatcher */.rD }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Self */.PiD
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_INPUT_VALUE_ACCESSOR]
            }] }, { type: AutofillMonitor }, { type: core/* NgZone */.R0b }, { type: MatFormField, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_FORM_FIELD]
            }] }]; }, { id: [{
            type: core/* Input */.IIB
        }], disabled: [{
            type: core/* Input */.IIB
        }], required: [{
            type: core/* Input */.IIB
        }], type: [{
            type: core/* Input */.IIB
        }], value: [{
            type: core/* Input */.IIB
        }], readonly: [{
            type: core/* Input */.IIB
        }], 
    // We have to use a `HostListener` here in order to support both Ivy and ViewEngine.
    // In Ivy the `host` bindings will be merged when this class is extended, whereas in
    // ViewEngine they're overwritten.
    // TODO(crisbeto): we move this back into `host` once Ivy is turned on by default.
    /** Callback for the cases where the focused state of the input changes. */
    // tslint:disable:no-host-decorator-in-concrete
    // tslint:enable:no-host-decorator-in-concrete
    _focusChanged: [{
            type: core/* HostListener */.L6J,
            args: ['focus', ['true']]
        }, {
            type: core/* HostListener */.L6J,
            args: ['blur', ['false']]
        }], 
    // We have to use a `HostListener` here in order to support both Ivy and ViewEngine.
    // In Ivy the `host` bindings will be merged when this class is extended, whereas in
    // ViewEngine they're overwritten.
    // TODO(crisbeto): we move this back into `host` once Ivy is turned on by default.
    // tslint:disable-next-line:no-host-decorator-in-concrete
    _onInput: [{
            type: core/* HostListener */.L6J,
            args: ['input']
        }], placeholder: [{
            type: core/* Input */.IIB
        }], errorStateMatcher: [{
            type: core/* Input */.IIB
        }], userAriaDescribedBy: [{
            type: core/* Input */.IIB,
            args: ['aria-describedby']
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatInputModule {
}
MatInputModule.ɵfac = function MatInputModule_Factory(t) { return new (t || MatInputModule)(); };
MatInputModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatInputModule });
MatInputModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [fesm2015_core/* ErrorStateMatcher */.rD], imports: [[
            TextFieldModule,
            MatFormFieldModule,
            fesm2015_core/* MatCommonModule */.BQ,
        ], TextFieldModule,
        // We re-export the `MatFormFieldModule` since `MatInput` will almost always
        // be used together with `MatFormField`.
        MatFormFieldModule] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatInputModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                declarations: [MatInput, MatTextareaAutosize],
                imports: [
                    TextFieldModule,
                    MatFormFieldModule,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                exports: [
                    TextFieldModule,
                    // We re-export the `MatFormFieldModule` since `MatInput` will almost always
                    // be used together with `MatFormField`.
                    MatFormFieldModule,
                    MatInput,
                    MatTextareaAutosize,
                ],
                providers: [fesm2015_core/* ErrorStateMatcher */.rD]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatInputModule, { declarations: function () { return [MatInput, MatTextareaAutosize]; }, imports: function () { return [TextFieldModule,
        MatFormFieldModule,
        fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [TextFieldModule,
        // We re-export the `MatFormFieldModule` since `MatInput` will almost always
        // be used together with `MatFormField`.
        MatFormFieldModule, MatInput, MatTextareaAutosize]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=input.js.map
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/button.js
var fesm2015_button = __webpack_require__(1095);
;// CONCATENATED MODULE: ./src/app/pages/contact-info/contact-info.component.ts










function ContactInfoComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 41);
    core/* ɵɵelementStart */.TgZ(2, "div", 42);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 43);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 44);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r2 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r2.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r2.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r2.comment);
} }
class ContactInfoComponent {
    constructor(restService, utilityService) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.email = '';
        this.messageText = '';
        this.utilityService.setTitle('About');
        this.utilityService.reloadPosts(8);
    }
    sendMessage() {
        if (this.messageText == '') {
        }
        else {
            let message = {
                id: null,
                page: null,
                nickname: localStorage.getItem('f1-chosen-nickname') + ' - ' + this.email,
                comment: this.messageText,
                timestamp: null
            };
            let observableStandings = this.restService.sendMessage(message);
            observableStandings.subscribe({
                next: data => {
                    this.email = '';
                    this.messageText = '';
                    this.utilityService.pushToastrMessage("Message sent.");
                    return data;
                },
                error: error => {
                    this.utilityService.pushToastrMessage("ERROR OCCURED. Message wasn't sent.");
                    console.error('There was an error!', error);
                }
            });
        }
    }
}
ContactInfoComponent.ɵfac = function ContactInfoComponent_Factory(t) { return new (t || ContactInfoComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
ContactInfoComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: ContactInfoComponent, selectors: [["contact-info-cmp"]], decls: 123, vars: 5, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], [1, "div-padded-left-2", "div-padded-top-1", "col-lg-3", "col-md-6", "col-sm-6"], [1, "card", "card-stats"], [1, "card-header"], [1, "implying"], [1, "card-body"], [1, "col-12", "col-lg-2", "no-right-padding"], [1, "icon-big", "text-center", "icon-warning"], [1, "nc-icon", "nc-zoom-split", "text-warning"], [1, "col-12", "col-lg-10"], [1, "card-body-padded-mobile"], [1, "col-12", "col-lg-12"], [1, "col-lg-4", "col-md-6", "div-padded-top-1", "col-sm-6"], ["list-style-type", "circle"], [1, "nc-icon", "nc-minimal-up", "text-warning"], [1, "div-padded-right-2", "div-padded-top-1", "col-lg-5", "col-md-6", "col-sm-6"], [1, "col-12", "col-lg-7", "no-right-padding"], ["src", "assets/img/splitter-monty_python.jpg"], [1, "col-12", "col-lg-5"], [1, "nc-icon", "nc-money-coins", "text-warning"], [1, "col-lg-12", "col-md-6", "col-sm-6"], [1, "bg-secondary", "row"], [1, "col-7", "col-md-9"], [1, "text-white"], ["placeholder", "e-mail", "type", "text", 1, "form-control", 3, "ngModel", "ngModelChange"], [1, "form-group"], ["matInput", "", "placeholder", "type a message", 1, "form-control", "message-text-area-150", 3, "ngModel", "ngModelChange"], [1, "col-md-2", "no-right-padding"], [1, "nc-icon", "nc-email-85", "text-warning"], ["mat-raised-button", "", 1, "bg-success", "text-white", 3, "click"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"]], template: function ContactInfoComponent_Template(rf, ctx) { if (rf & 1) {
        const _r3 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function ContactInfoComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function ContactInfoComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function ContactInfoComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(8); });
        core/* ɵɵtext */._uU(10, "Post");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function ContactInfoComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(8); });
        core/* ɵɵtext */._uU(12, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, ContactInfoComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function ContactInfoComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r3); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function ContactInfoComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r3); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 3);
        core/* ɵɵelementStart */.TgZ(21, "div", 12);
        core/* ɵɵelementStart */.TgZ(22, "div", 13);
        core/* ɵɵelementStart */.TgZ(23, "div", 14);
        core/* ɵɵelementStart */.TgZ(24, "span", 15);
        core/* ɵɵtext */._uU(25, "> Who are you? ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(26, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(27, "div", 16);
        core/* ɵɵelementStart */.TgZ(28, "div", 3);
        core/* ɵɵelementStart */.TgZ(29, "div", 17);
        core/* ɵɵelementStart */.TgZ(30, "div", 18);
        core/* ɵɵelement */._UZ(31, "i", 19);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(32, "div", 20);
        core/* ɵɵelementStart */.TgZ(33, "div", 21);
        core/* ɵɵelementStart */.TgZ(34, "p");
        core/* ɵɵtext */._uU(35, "Posts written by Sorim#115096 are mine");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(36, "div", 13);
        core/* ɵɵelementStart */.TgZ(37, "div", 14);
        core/* ɵɵelementStart */.TgZ(38, "span", 15);
        core/* ɵɵtext */._uU(39, "> What is your plan? ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(40, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(41, "div", 16);
        core/* ɵɵelementStart */.TgZ(42, "div", 3);
        core/* ɵɵelementStart */.TgZ(43, "div", 22);
        core/* ɵɵelementStart */.TgZ(44, "div");
        core/* ɵɵelementStart */.TgZ(45, "ul");
        core/* ɵɵtext */._uU(46, " PressPass ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(47, "div", 23);
        core/* ɵɵelementStart */.TgZ(48, "div", 13);
        core/* ɵɵelementStart */.TgZ(49, "div", 14);
        core/* ɵɵelementStart */.TgZ(50, "span", 15);
        core/* ɵɵtext */._uU(51, ">I have a suggestion/complaint");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(52, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(53, "div", 16);
        core/* ɵɵelementStart */.TgZ(54, "div", 3);
        core/* ɵɵelementStart */.TgZ(55, "div", 22);
        core/* ɵɵelementStart */.TgZ(56, "div");
        core/* ɵɵelementStart */.TgZ(57, "ul", 24);
        core/* ɵɵtext */._uU(58, " - post it in the comments anywhere ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(59, "ul");
        core/* ɵɵtext */._uU(60, " - or send private message below. ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(61, "ul");
        core/* ɵɵtext */._uU(62, " - or e-mail admin@f1exposure.com ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(63, "div", 13);
        core/* ɵɵelementStart */.TgZ(64, "div", 14);
        core/* ɵɵelementStart */.TgZ(65, "span", 15);
        core/* ɵɵtext */._uU(66, "> Is there jigsaw puzzle functionality? ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(67, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(68, "div", 16);
        core/* ɵɵelementStart */.TgZ(69, "div", 3);
        core/* ɵɵelementStart */.TgZ(70, "div", 17);
        core/* ɵɵelementStart */.TgZ(71, "div", 18);
        core/* ɵɵelement */._UZ(72, "i", 25);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(73, "div", 20);
        core/* ɵɵelementStart */.TgZ(74, "div", 21);
        core/* ɵɵelementStart */.TgZ(75, "p", 16);
        core/* ɵɵtext */._uU(76, "there is no jigsaw puzzle functionality ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(77, "div", 26);
        core/* ɵɵelementStart */.TgZ(78, "div", 13);
        core/* ɵɵelementStart */.TgZ(79, "div", 14);
        core/* ɵɵelementStart */.TgZ(80, "span", 15);
        core/* ɵɵtext */._uU(81, "> SPLITTER! ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(82, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(83, "div", 16);
        core/* ɵɵelementStart */.TgZ(84, "div", 3);
        core/* ɵɵelementStart */.TgZ(85, "div", 27);
        core/* ɵɵelementStart */.TgZ(86, "div", 18);
        core/* ɵɵelement */._UZ(87, "img", 28);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(88, "div", 29);
        core/* ɵɵelementStart */.TgZ(89, "div", 21);
        core/* ɵɵelementStart */.TgZ(90, "p", 16);
        core/* ɵɵtext */._uU(91, "I don't know what you mean ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(92, "div", 13);
        core/* ɵɵelementStart */.TgZ(93, "div", 16);
        core/* ɵɵelementStart */.TgZ(94, "div", 3);
        core/* ɵɵelementStart */.TgZ(95, "div", 17);
        core/* ɵɵelementStart */.TgZ(96, "div", 18);
        core/* ɵɵelement */._UZ(97, "i", 30);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(98, "div", 20);
        core/* ɵɵelementStart */.TgZ(99, "div", 21);
        core/* ɵɵelementStart */.TgZ(100, "p", 16);
        core/* ɵɵtext */._uU(101, "F1Exposure.com is a participant in the Amazon Services LLC Associates Program, an affiliate advertising program designed to provide a means for sites to earn advertising fees by advertising and linking to Amazon.com. ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(102, "p");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(103, "div", 3);
        core/* ɵɵelementStart */.TgZ(104, "div", 31);
        core/* ɵɵelementStart */.TgZ(105, "div", 13);
        core/* ɵɵelementStart */.TgZ(106, "div", 16);
        core/* ɵɵelementStart */.TgZ(107, "div", 32);
        core/* ɵɵelementStart */.TgZ(108, "div", 33);
        core/* ɵɵelementStart */.TgZ(109, "div", 4);
        core/* ɵɵelementStart */.TgZ(110, "label", 34);
        core/* ɵɵtext */._uU(111, "Your contact e-mail (not required)");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(112, "input", 35);
        core/* ɵɵlistener */.NdJ("ngModelChange", function ContactInfoComponent_Template_input_ngModelChange_112_listener($event) { return ctx.email = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(113, "div", 36);
        core/* ɵɵelementStart */.TgZ(114, "label", 34);
        core/* ɵɵtext */._uU(115, "Message");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(116, "textarea", 37);
        core/* ɵɵlistener */.NdJ("ngModelChange", function ContactInfoComponent_Template_textarea_ngModelChange_116_listener($event) { return ctx.messageText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(117, "div", 38);
        core/* ɵɵelementStart */.TgZ(118, "div", 18);
        core/* ɵɵelement */._UZ(119, "i", 39);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(120, "div", 3);
        core/* ɵɵelementStart */.TgZ(121, "button", 40);
        core/* ɵɵlistener */.NdJ("click", function ContactInfoComponent_Template_button_click_121_listener() { return ctx.sendMessage(); });
        core/* ɵɵtext */._uU(122, " Send message ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
        core/* ɵɵadvance */.xp6(99);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.email);
        core/* ɵɵadvance */.xp6(4);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.messageText);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/typography/typography.component.ts

class TypographyComponent {
}
TypographyComponent.ɵfac = function TypographyComponent_Factory(t) { return new (t || TypographyComponent)(); };
TypographyComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: TypographyComponent, selectors: [["typography-cmp"]], decls: 92, vars: 0, consts: [[1, "row"], [1, "col-md-12"], [1, "card"], [1, "card-header"], [1, "title"], [1, "category"], [1, "card-body"], [1, "typography-line"], [1, "blockquote", "blockquote-primary"], [1, "text-muted"], [1, "text-primary"], [1, "text-info"], [1, "text-success"], [1, "text-warning"], [1, "text-danger"]], template: function TypographyComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1);
        core/* ɵɵelementStart */.TgZ(2, "div", 2);
        core/* ɵɵelementStart */.TgZ(3, "div", 3);
        core/* ɵɵelementStart */.TgZ(4, "h5", 4);
        core/* ɵɵtext */._uU(5, "Paper Table Heading");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(6, "p", 5);
        core/* ɵɵtext */._uU(7, "Created using Montserrat Font Family");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "div", 7);
        core/* ɵɵelementStart */.TgZ(10, "h1");
        core/* ɵɵelementStart */.TgZ(11, "span");
        core/* ɵɵtext */._uU(12, "Header 1");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(13, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(14, "div", 7);
        core/* ɵɵelementStart */.TgZ(15, "h2");
        core/* ɵɵelementStart */.TgZ(16, "span");
        core/* ɵɵtext */._uU(17, "Header 2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(18, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(19, "div", 7);
        core/* ɵɵelementStart */.TgZ(20, "h3");
        core/* ɵɵelementStart */.TgZ(21, "span");
        core/* ɵɵtext */._uU(22, "Header 3");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(23, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "div", 7);
        core/* ɵɵelementStart */.TgZ(25, "h4");
        core/* ɵɵelementStart */.TgZ(26, "span");
        core/* ɵɵtext */._uU(27, "Header 4");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(28, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(29, "div", 7);
        core/* ɵɵelementStart */.TgZ(30, "h5");
        core/* ɵɵelementStart */.TgZ(31, "span");
        core/* ɵɵtext */._uU(32, "Header 5");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(33, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(34, "div", 7);
        core/* ɵɵelementStart */.TgZ(35, "h6");
        core/* ɵɵelementStart */.TgZ(36, "span");
        core/* ɵɵtext */._uU(37, "Header 6");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(38, "The Life of Paper Dashboard ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(39, "div", 7);
        core/* ɵɵelementStart */.TgZ(40, "p");
        core/* ɵɵelementStart */.TgZ(41, "span");
        core/* ɵɵtext */._uU(42, "Paragraph");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(43, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers. I understand culture. I am the nucleus. I think that\u2019s a responsibility that I have, to push possibilities, to show people, this is the level that things could be at. ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(44, "div", 7);
        core/* ɵɵelementStart */.TgZ(45, "span");
        core/* ɵɵtext */._uU(46, "Quote");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(47, "blockquote");
        core/* ɵɵelementStart */.TgZ(48, "p", 8);
        core/* ɵɵtext */._uU(49, " \"I will be the leader of a company that ends up being worth billions of dollars, because I got the answers. I understand culture. I am the nucleus. I think that\u2019s a responsibility that I have, to push possibilities, to show people, this is the level that things could be at.\" ");
        core/* ɵɵelement */._UZ(50, "br");
        core/* ɵɵelement */._UZ(51, "br");
        core/* ɵɵelementStart */.TgZ(52, "small");
        core/* ɵɵtext */._uU(53, " - Noaa ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(54, "div", 7);
        core/* ɵɵelementStart */.TgZ(55, "span");
        core/* ɵɵtext */._uU(56, "Muted Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(57, "p", 9);
        core/* ɵɵtext */._uU(58, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers... ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(59, "div", 7);
        core/* ɵɵelementStart */.TgZ(60, "span");
        core/* ɵɵtext */._uU(61, "Primary Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(62, "p", 10);
        core/* ɵɵtext */._uU(63, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers...");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(64, "div", 7);
        core/* ɵɵelementStart */.TgZ(65, "span");
        core/* ɵɵtext */._uU(66, "Info Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(67, "p", 11);
        core/* ɵɵtext */._uU(68, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers... ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(69, "div", 7);
        core/* ɵɵelementStart */.TgZ(70, "span");
        core/* ɵɵtext */._uU(71, "Success Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(72, "p", 12);
        core/* ɵɵtext */._uU(73, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers... ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(74, "div", 7);
        core/* ɵɵelementStart */.TgZ(75, "span");
        core/* ɵɵtext */._uU(76, "Warning Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(77, "p", 13);
        core/* ɵɵtext */._uU(78, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers... ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(79, "div", 7);
        core/* ɵɵelementStart */.TgZ(80, "span");
        core/* ɵɵtext */._uU(81, "Danger Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(82, "p", 14);
        core/* ɵɵtext */._uU(83, " I will be the leader of a company that ends up being worth billions of dollars, because I got the answers... ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(84, "div", 7);
        core/* ɵɵelementStart */.TgZ(85, "h2");
        core/* ɵɵelementStart */.TgZ(86, "span");
        core/* ɵɵtext */._uU(87, "Small Tag");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(88, " Header with small subtitle ");
        core/* ɵɵelement */._UZ(89, "br");
        core/* ɵɵelementStart */.TgZ(90, "small");
        core/* ɵɵtext */._uU(91, "Use \"small\" tag for the headers");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } }, encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/icons/icons.component.ts

class IconsComponent {
}
IconsComponent.ɵfac = function IconsComponent_Factory(t) { return new (t || IconsComponent)(); };
IconsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: IconsComponent, selectors: [["icons-cmp"]], decls: 414, vars: 0, consts: [[1, "row"], [1, "col-md-12"], [1, "card", "demo-icons"], [1, "card-header"], [1, "card-title"], [1, "card-category"], ["href", "https://nucleoapp.com/?ref=1712"], [1, "card-body", "all-icons"], ["id", "icons-wrapper"], [1, "nc-icon", "nc-air-baloon"], [1, "nc-icon", "nc-album-2"], [1, "nc-icon", "nc-alert-circle-i"], [1, "nc-icon", "nc-align-center"], [1, "nc-icon", "nc-align-left-2"], [1, "nc-icon", "nc-ambulance"], [1, "nc-icon", "nc-app"], [1, "nc-icon", "nc-atom"], [1, "nc-icon", "nc-badge"], [1, "nc-icon", "nc-bag-16"], [1, "nc-icon", "nc-bank"], [1, "nc-icon", "nc-basket"], [1, "nc-icon", "nc-bell-55"], [1, "nc-icon", "nc-bold"], [1, "nc-icon", "nc-book-bookmark"], [1, "nc-icon", "nc-bookmark-2"], [1, "nc-icon", "nc-box-2"], [1, "nc-icon", "nc-box"], [1, "nc-icon", "nc-briefcase-24"], [1, "nc-icon", "nc-bulb-63"], [1, "nc-icon", "nc-bullet-list-67"], [1, "nc-icon", "nc-bus-front-12"], [1, "nc-icon", "nc-button-pause"], [1, "nc-icon", "nc-button-play"], [1, "nc-icon", "nc-button-power"], [1, "nc-icon", "nc-calendar-60"], [1, "nc-icon", "nc-camera-compact"], [1, "nc-icon", "nc-caps-small"], [1, "nc-icon", "nc-cart-simple"], [1, "nc-icon", "nc-chart-bar-32"], [1, "nc-icon", "nc-chart-pie-36"], [1, "nc-icon", "nc-chat-33"], [1, "nc-icon", "nc-check-2"], [1, "nc-icon", "nc-circle-10"], [1, "nc-icon", "nc-cloud-download-93"], [1, "nc-icon", "nc-cloud-upload-94"], [1, "nc-icon", "nc-compass-05"], [1, "nc-icon", "nc-controller-modern"], [1, "nc-icon", "nc-credit-card"], [1, "nc-icon", "nc-delivery-fast"], [1, "nc-icon", "nc-diamond"], [1, "nc-icon", "nc-email-85"], [1, "nc-icon", "nc-favourite-28"], [1, "nc-icon", "nc-glasses-2"], [1, "nc-icon", "nc-globe-2"], [1, "nc-icon", "nc-globe"], [1, "nc-icon", "nc-hat-3"], [1, "nc-icon", "nc-headphones"], [1, "nc-icon", "nc-html5"], [1, "nc-icon", "nc-image"], [1, "nc-icon", "nc-istanbul"], [1, "nc-icon", "nc-key-25"], [1, "nc-icon", "nc-laptop"], [1, "nc-icon", "nc-layout-11"], [1, "nc-icon", "nc-lock-circle-open"], [1, "nc-icon", "nc-map-big"], [1, "nc-icon", "nc-minimal-down"], [1, "nc-icon", "nc-minimal-left"], [1, "nc-icon", "nc-minimal-right"], [1, "nc-icon", "nc-minimal-up"], [1, "nc-icon", "nc-mobile"], [1, "nc-icon", "nc-money-coins"], [1, "nc-icon", "nc-note-03"], [1, "nc-icon", "nc-palette"], [1, "nc-icon", "nc-paper"], [1, "nc-icon", "nc-pin-3"], [1, "nc-icon", "nc-planet"], [1, "nc-icon", "nc-refresh-69"], [1, "nc-icon", "nc-ruler-pencil"], [1, "nc-icon", "nc-satisfied"], [1, "nc-icon", "nc-scissors"], [1, "nc-icon", "nc-send"], [1, "nc-icon", "nc-settings-gear-65"], [1, "nc-icon", "nc-settings"], [1, "nc-icon", "nc-share-66"], [1, "nc-icon", "nc-shop"], [1, "nc-icon", "nc-simple-add"], [1, "nc-icon", "nc-simple-delete"], [1, "nc-icon", "nc-simple-remove"], [1, "nc-icon", "nc-single-02"], [1, "nc-icon", "nc-single-copy-04"], [1, "nc-icon", "nc-sound-wave"], [1, "nc-icon", "nc-spaceship"], [1, "nc-icon", "nc-sun-fog-29"], [1, "nc-icon", "nc-support-17"], [1, "nc-icon", "nc-tablet-2"], [1, "nc-icon", "nc-tag-content"], [1, "nc-icon", "nc-tap-01"], [1, "nc-icon", "nc-tie-bow"], [1, "nc-icon", "nc-tile-56"], [1, "nc-icon", "nc-time-alarm"], [1, "nc-icon", "nc-touch-id"], [1, "nc-icon", "nc-trophy"], [1, "nc-icon", "nc-tv-2"], [1, "nc-icon", "nc-umbrella-13"], [1, "nc-icon", "nc-user-run"], [1, "nc-icon", "nc-vector"], [1, "nc-icon", "nc-watch-time"], [1, "nc-icon", "nc-world-2"], [1, "nc-icon", "nc-zoom-split"]], template: function IconsComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1);
        core/* ɵɵelementStart */.TgZ(2, "div", 2);
        core/* ɵɵelementStart */.TgZ(3, "div", 3);
        core/* ɵɵelementStart */.TgZ(4, "h5", 4);
        core/* ɵɵtext */._uU(5, "100 Awesome Nucleo Icons");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(6, "p", 5);
        core/* ɵɵtext */._uU(7, "Handcrafted by our friends from ");
        core/* ɵɵelementStart */.TgZ(8, "a", 6);
        core/* ɵɵtext */._uU(9, "NucleoApp");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(10, "div", 7);
        core/* ɵɵelementStart */.TgZ(11, "div", 8);
        core/* ɵɵelementStart */.TgZ(12, "section");
        core/* ɵɵelementStart */.TgZ(13, "ul");
        core/* ɵɵelementStart */.TgZ(14, "li");
        core/* ɵɵelement */._UZ(15, "i", 9);
        core/* ɵɵelementStart */.TgZ(16, "p");
        core/* ɵɵtext */._uU(17, "nc-air-baloon");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(18, "li");
        core/* ɵɵelement */._UZ(19, "i", 10);
        core/* ɵɵelementStart */.TgZ(20, "p");
        core/* ɵɵtext */._uU(21, "nc-album-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(22, "li");
        core/* ɵɵelement */._UZ(23, "i", 11);
        core/* ɵɵelementStart */.TgZ(24, "p");
        core/* ɵɵtext */._uU(25, "nc-alert-circle-i");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(26, "li");
        core/* ɵɵelement */._UZ(27, "i", 12);
        core/* ɵɵelementStart */.TgZ(28, "p");
        core/* ɵɵtext */._uU(29, "nc-align-center");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(30, "li");
        core/* ɵɵelement */._UZ(31, "i", 13);
        core/* ɵɵelementStart */.TgZ(32, "p");
        core/* ɵɵtext */._uU(33, "nc-align-left-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(34, "li");
        core/* ɵɵelement */._UZ(35, "i", 14);
        core/* ɵɵelementStart */.TgZ(36, "p");
        core/* ɵɵtext */._uU(37, "nc-ambulance");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(38, "li");
        core/* ɵɵelement */._UZ(39, "i", 15);
        core/* ɵɵelementStart */.TgZ(40, "p");
        core/* ɵɵtext */._uU(41, "nc-app");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(42, "li");
        core/* ɵɵelement */._UZ(43, "i", 16);
        core/* ɵɵelementStart */.TgZ(44, "p");
        core/* ɵɵtext */._uU(45, "nc-atom");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(46, "li");
        core/* ɵɵelement */._UZ(47, "i", 17);
        core/* ɵɵelementStart */.TgZ(48, "p");
        core/* ɵɵtext */._uU(49, "nc-badge");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(50, "li");
        core/* ɵɵelement */._UZ(51, "i", 18);
        core/* ɵɵelementStart */.TgZ(52, "p");
        core/* ɵɵtext */._uU(53, "nc-bag-16");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(54, "li");
        core/* ɵɵelement */._UZ(55, "i", 19);
        core/* ɵɵelementStart */.TgZ(56, "p");
        core/* ɵɵtext */._uU(57, "nc-bank");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(58, "li");
        core/* ɵɵelement */._UZ(59, "i", 20);
        core/* ɵɵelementStart */.TgZ(60, "p");
        core/* ɵɵtext */._uU(61, "nc-basket");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(62, "li");
        core/* ɵɵelement */._UZ(63, "i", 21);
        core/* ɵɵelementStart */.TgZ(64, "p");
        core/* ɵɵtext */._uU(65, "nc-bell-55");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(66, "li");
        core/* ɵɵelement */._UZ(67, "i", 22);
        core/* ɵɵelementStart */.TgZ(68, "p");
        core/* ɵɵtext */._uU(69, "nc-bold");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(70, "li");
        core/* ɵɵelement */._UZ(71, "i", 23);
        core/* ɵɵelementStart */.TgZ(72, "p");
        core/* ɵɵtext */._uU(73, "nc-book-bookmark");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(74, "li");
        core/* ɵɵelement */._UZ(75, "i", 24);
        core/* ɵɵelementStart */.TgZ(76, "p");
        core/* ɵɵtext */._uU(77, "nc-bookmark-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(78, "li");
        core/* ɵɵelement */._UZ(79, "i", 25);
        core/* ɵɵelementStart */.TgZ(80, "p");
        core/* ɵɵtext */._uU(81, "nc-box-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(82, "li");
        core/* ɵɵelement */._UZ(83, "i", 26);
        core/* ɵɵelementStart */.TgZ(84, "p");
        core/* ɵɵtext */._uU(85, "nc-box");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(86, "li");
        core/* ɵɵelement */._UZ(87, "i", 27);
        core/* ɵɵelementStart */.TgZ(88, "p");
        core/* ɵɵtext */._uU(89, "nc-briefcase-24");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(90, "li");
        core/* ɵɵelement */._UZ(91, "i", 28);
        core/* ɵɵelementStart */.TgZ(92, "p");
        core/* ɵɵtext */._uU(93, "nc-bulb-63");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(94, "li");
        core/* ɵɵelement */._UZ(95, "i", 29);
        core/* ɵɵelementStart */.TgZ(96, "p");
        core/* ɵɵtext */._uU(97, "nc-bullet-list-67");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(98, "li");
        core/* ɵɵelement */._UZ(99, "i", 30);
        core/* ɵɵelementStart */.TgZ(100, "p");
        core/* ɵɵtext */._uU(101, "nc-bus-front-12");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(102, "li");
        core/* ɵɵelement */._UZ(103, "i", 31);
        core/* ɵɵelementStart */.TgZ(104, "p");
        core/* ɵɵtext */._uU(105, "nc-button-pause");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(106, "li");
        core/* ɵɵelement */._UZ(107, "i", 32);
        core/* ɵɵelementStart */.TgZ(108, "p");
        core/* ɵɵtext */._uU(109, "nc-button-play");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(110, "li");
        core/* ɵɵelement */._UZ(111, "i", 33);
        core/* ɵɵelementStart */.TgZ(112, "p");
        core/* ɵɵtext */._uU(113, "nc-button-power");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(114, "li");
        core/* ɵɵelement */._UZ(115, "i", 34);
        core/* ɵɵelementStart */.TgZ(116, "p");
        core/* ɵɵtext */._uU(117, "nc-calendar-60");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(118, "li");
        core/* ɵɵelement */._UZ(119, "i", 35);
        core/* ɵɵelementStart */.TgZ(120, "p");
        core/* ɵɵtext */._uU(121, "nc-camera-compact");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(122, "li");
        core/* ɵɵelement */._UZ(123, "i", 36);
        core/* ɵɵelementStart */.TgZ(124, "p");
        core/* ɵɵtext */._uU(125, "nc-caps-small");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(126, "li");
        core/* ɵɵelement */._UZ(127, "i", 37);
        core/* ɵɵelementStart */.TgZ(128, "p");
        core/* ɵɵtext */._uU(129, "nc-cart-simple");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(130, "li");
        core/* ɵɵelement */._UZ(131, "i", 38);
        core/* ɵɵelementStart */.TgZ(132, "p");
        core/* ɵɵtext */._uU(133, "nc-chart-bar-32");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(134, "li");
        core/* ɵɵelement */._UZ(135, "i", 39);
        core/* ɵɵelementStart */.TgZ(136, "p");
        core/* ɵɵtext */._uU(137, "nc-chart-pie-36");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(138, "li");
        core/* ɵɵelement */._UZ(139, "i", 40);
        core/* ɵɵelementStart */.TgZ(140, "p");
        core/* ɵɵtext */._uU(141, "nc-chat-33");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(142, "li");
        core/* ɵɵelement */._UZ(143, "i", 41);
        core/* ɵɵelementStart */.TgZ(144, "p");
        core/* ɵɵtext */._uU(145, "nc-check-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(146, "li");
        core/* ɵɵelement */._UZ(147, "i", 42);
        core/* ɵɵelementStart */.TgZ(148, "p");
        core/* ɵɵtext */._uU(149, "nc-circle-10");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(150, "li");
        core/* ɵɵelement */._UZ(151, "i", 43);
        core/* ɵɵelementStart */.TgZ(152, "p");
        core/* ɵɵtext */._uU(153, "nc-cloud-download-93");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(154, "li");
        core/* ɵɵelement */._UZ(155, "i", 44);
        core/* ɵɵelementStart */.TgZ(156, "p");
        core/* ɵɵtext */._uU(157, "nc-cloud-upload-94");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(158, "li");
        core/* ɵɵelement */._UZ(159, "i", 45);
        core/* ɵɵelementStart */.TgZ(160, "p");
        core/* ɵɵtext */._uU(161, "nc-compass-05");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(162, "li");
        core/* ɵɵelement */._UZ(163, "i", 46);
        core/* ɵɵelementStart */.TgZ(164, "p");
        core/* ɵɵtext */._uU(165, "nc-controller-modern");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(166, "li");
        core/* ɵɵelement */._UZ(167, "i", 47);
        core/* ɵɵelementStart */.TgZ(168, "p");
        core/* ɵɵtext */._uU(169, "nc-credit-card");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(170, "li");
        core/* ɵɵelement */._UZ(171, "i", 48);
        core/* ɵɵelementStart */.TgZ(172, "p");
        core/* ɵɵtext */._uU(173, "nc-delivery-fast");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(174, "li");
        core/* ɵɵelement */._UZ(175, "i", 49);
        core/* ɵɵelementStart */.TgZ(176, "p");
        core/* ɵɵtext */._uU(177, "nc-diamond");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(178, "li");
        core/* ɵɵelement */._UZ(179, "i", 50);
        core/* ɵɵelementStart */.TgZ(180, "p");
        core/* ɵɵtext */._uU(181, "nc-email-85");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(182, "li");
        core/* ɵɵelement */._UZ(183, "i", 51);
        core/* ɵɵelementStart */.TgZ(184, "p");
        core/* ɵɵtext */._uU(185, "nc-favourite-28");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(186, "li");
        core/* ɵɵelement */._UZ(187, "i", 52);
        core/* ɵɵelementStart */.TgZ(188, "p");
        core/* ɵɵtext */._uU(189, "nc-glasses-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(190, "li");
        core/* ɵɵelement */._UZ(191, "i", 53);
        core/* ɵɵelementStart */.TgZ(192, "p");
        core/* ɵɵtext */._uU(193, "nc-globe-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(194, "li");
        core/* ɵɵelement */._UZ(195, "i", 54);
        core/* ɵɵelementStart */.TgZ(196, "p");
        core/* ɵɵtext */._uU(197, "nc-globe");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(198, "li");
        core/* ɵɵelement */._UZ(199, "i", 55);
        core/* ɵɵelementStart */.TgZ(200, "p");
        core/* ɵɵtext */._uU(201, "nc-hat-3");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(202, "li");
        core/* ɵɵelement */._UZ(203, "i", 56);
        core/* ɵɵelementStart */.TgZ(204, "p");
        core/* ɵɵtext */._uU(205, "nc-headphones");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(206, "li");
        core/* ɵɵelement */._UZ(207, "i", 57);
        core/* ɵɵelementStart */.TgZ(208, "p");
        core/* ɵɵtext */._uU(209, "nc-html5");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(210, "li");
        core/* ɵɵelement */._UZ(211, "i", 58);
        core/* ɵɵelementStart */.TgZ(212, "p");
        core/* ɵɵtext */._uU(213, "nc-image");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(214, "li");
        core/* ɵɵelement */._UZ(215, "i", 59);
        core/* ɵɵelementStart */.TgZ(216, "p");
        core/* ɵɵtext */._uU(217, "nc-istanbul");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(218, "li");
        core/* ɵɵelement */._UZ(219, "i", 60);
        core/* ɵɵelementStart */.TgZ(220, "p");
        core/* ɵɵtext */._uU(221, "nc-key-25");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(222, "li");
        core/* ɵɵelement */._UZ(223, "i", 61);
        core/* ɵɵelementStart */.TgZ(224, "p");
        core/* ɵɵtext */._uU(225, "nc-laptop");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(226, "li");
        core/* ɵɵelement */._UZ(227, "i", 62);
        core/* ɵɵelementStart */.TgZ(228, "p");
        core/* ɵɵtext */._uU(229, "nc-layout-11");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(230, "li");
        core/* ɵɵelement */._UZ(231, "i", 63);
        core/* ɵɵelementStart */.TgZ(232, "p");
        core/* ɵɵtext */._uU(233, "nc-lock-circle-open");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(234, "li");
        core/* ɵɵelement */._UZ(235, "i", 64);
        core/* ɵɵelementStart */.TgZ(236, "p");
        core/* ɵɵtext */._uU(237, "nc-map-big");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(238, "li");
        core/* ɵɵelement */._UZ(239, "i", 65);
        core/* ɵɵelementStart */.TgZ(240, "p");
        core/* ɵɵtext */._uU(241, "nc-minimal-down");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(242, "li");
        core/* ɵɵelement */._UZ(243, "i", 66);
        core/* ɵɵelementStart */.TgZ(244, "p");
        core/* ɵɵtext */._uU(245, "nc-minimal-left");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(246, "li");
        core/* ɵɵelement */._UZ(247, "i", 67);
        core/* ɵɵelementStart */.TgZ(248, "p");
        core/* ɵɵtext */._uU(249, "nc-minimal-right");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(250, "li");
        core/* ɵɵelement */._UZ(251, "i", 68);
        core/* ɵɵelementStart */.TgZ(252, "p");
        core/* ɵɵtext */._uU(253, "nc-minimal-up");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(254, "li");
        core/* ɵɵelement */._UZ(255, "i", 69);
        core/* ɵɵelementStart */.TgZ(256, "p");
        core/* ɵɵtext */._uU(257, "nc-mobile");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(258, "li");
        core/* ɵɵelement */._UZ(259, "i", 70);
        core/* ɵɵelementStart */.TgZ(260, "p");
        core/* ɵɵtext */._uU(261, "nc-money-coins");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(262, "li");
        core/* ɵɵelement */._UZ(263, "i", 71);
        core/* ɵɵelementStart */.TgZ(264, "p");
        core/* ɵɵtext */._uU(265, "nc-note-03");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(266, "li");
        core/* ɵɵelement */._UZ(267, "i", 72);
        core/* ɵɵelementStart */.TgZ(268, "p");
        core/* ɵɵtext */._uU(269, "nc-palette");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(270, "li");
        core/* ɵɵelement */._UZ(271, "i", 73);
        core/* ɵɵelementStart */.TgZ(272, "p");
        core/* ɵɵtext */._uU(273, "nc-paper");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(274, "li");
        core/* ɵɵelement */._UZ(275, "i", 74);
        core/* ɵɵelementStart */.TgZ(276, "p");
        core/* ɵɵtext */._uU(277, "nc-pin-3");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(278, "li");
        core/* ɵɵelement */._UZ(279, "i", 75);
        core/* ɵɵelementStart */.TgZ(280, "p");
        core/* ɵɵtext */._uU(281, "nc-planet");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(282, "li");
        core/* ɵɵelement */._UZ(283, "i", 76);
        core/* ɵɵelementStart */.TgZ(284, "p");
        core/* ɵɵtext */._uU(285, "nc-refresh-69");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(286, "li");
        core/* ɵɵelement */._UZ(287, "i", 77);
        core/* ɵɵelementStart */.TgZ(288, "p");
        core/* ɵɵtext */._uU(289, "nc-ruler-pencil");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(290, "li");
        core/* ɵɵelement */._UZ(291, "i", 78);
        core/* ɵɵelementStart */.TgZ(292, "p");
        core/* ɵɵtext */._uU(293, "nc-satisfied");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(294, "li");
        core/* ɵɵelement */._UZ(295, "i", 79);
        core/* ɵɵelementStart */.TgZ(296, "p");
        core/* ɵɵtext */._uU(297, "nc-scissors");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(298, "li");
        core/* ɵɵelement */._UZ(299, "i", 80);
        core/* ɵɵelementStart */.TgZ(300, "p");
        core/* ɵɵtext */._uU(301, "nc-send");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(302, "li");
        core/* ɵɵelement */._UZ(303, "i", 81);
        core/* ɵɵelementStart */.TgZ(304, "p");
        core/* ɵɵtext */._uU(305, "nc-settings-gear-65");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(306, "li");
        core/* ɵɵelement */._UZ(307, "i", 82);
        core/* ɵɵelementStart */.TgZ(308, "p");
        core/* ɵɵtext */._uU(309, "nc-settings");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(310, "li");
        core/* ɵɵelement */._UZ(311, "i", 83);
        core/* ɵɵelementStart */.TgZ(312, "p");
        core/* ɵɵtext */._uU(313, "nc-share-66");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(314, "li");
        core/* ɵɵelement */._UZ(315, "i", 84);
        core/* ɵɵelementStart */.TgZ(316, "p");
        core/* ɵɵtext */._uU(317, "nc-shop");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(318, "li");
        core/* ɵɵelement */._UZ(319, "i", 85);
        core/* ɵɵelementStart */.TgZ(320, "p");
        core/* ɵɵtext */._uU(321, "nc-simple-add");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(322, "li");
        core/* ɵɵelement */._UZ(323, "i", 86);
        core/* ɵɵelementStart */.TgZ(324, "p");
        core/* ɵɵtext */._uU(325, "nc-simple-delete");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(326, "li");
        core/* ɵɵelement */._UZ(327, "i", 87);
        core/* ɵɵelementStart */.TgZ(328, "p");
        core/* ɵɵtext */._uU(329, "nc-simple-remove");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(330, "li");
        core/* ɵɵelement */._UZ(331, "i", 88);
        core/* ɵɵelementStart */.TgZ(332, "p");
        core/* ɵɵtext */._uU(333, "nc-single-02");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(334, "li");
        core/* ɵɵelement */._UZ(335, "i", 89);
        core/* ɵɵelementStart */.TgZ(336, "p");
        core/* ɵɵtext */._uU(337, "nc-single-copy-04");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(338, "li");
        core/* ɵɵelement */._UZ(339, "i", 90);
        core/* ɵɵelementStart */.TgZ(340, "p");
        core/* ɵɵtext */._uU(341, "nc-sound-wave");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(342, "li");
        core/* ɵɵelement */._UZ(343, "i", 91);
        core/* ɵɵelementStart */.TgZ(344, "p");
        core/* ɵɵtext */._uU(345, "nc-spaceship");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(346, "li");
        core/* ɵɵelement */._UZ(347, "i", 92);
        core/* ɵɵelementStart */.TgZ(348, "p");
        core/* ɵɵtext */._uU(349, "nc-sun-fog-29");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(350, "li");
        core/* ɵɵelement */._UZ(351, "i", 93);
        core/* ɵɵelementStart */.TgZ(352, "p");
        core/* ɵɵtext */._uU(353, "nc-support-17");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(354, "li");
        core/* ɵɵelement */._UZ(355, "i", 94);
        core/* ɵɵelementStart */.TgZ(356, "p");
        core/* ɵɵtext */._uU(357, "nc-tablet-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(358, "li");
        core/* ɵɵelement */._UZ(359, "i", 95);
        core/* ɵɵelementStart */.TgZ(360, "p");
        core/* ɵɵtext */._uU(361, "nc-tag-content");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(362, "li");
        core/* ɵɵelement */._UZ(363, "i", 96);
        core/* ɵɵelementStart */.TgZ(364, "p");
        core/* ɵɵtext */._uU(365, "nc-tap-01");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(366, "li");
        core/* ɵɵelement */._UZ(367, "i", 97);
        core/* ɵɵelementStart */.TgZ(368, "p");
        core/* ɵɵtext */._uU(369, "nc-tie-bow");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(370, "li");
        core/* ɵɵelement */._UZ(371, "i", 98);
        core/* ɵɵelementStart */.TgZ(372, "p");
        core/* ɵɵtext */._uU(373, "nc-tile-56");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(374, "li");
        core/* ɵɵelement */._UZ(375, "i", 99);
        core/* ɵɵelementStart */.TgZ(376, "p");
        core/* ɵɵtext */._uU(377, "nc-time-alarm");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(378, "li");
        core/* ɵɵelement */._UZ(379, "i", 100);
        core/* ɵɵelementStart */.TgZ(380, "p");
        core/* ɵɵtext */._uU(381, "nc-touch-id");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(382, "li");
        core/* ɵɵelement */._UZ(383, "i", 101);
        core/* ɵɵelementStart */.TgZ(384, "p");
        core/* ɵɵtext */._uU(385, "nc-trophy");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(386, "li");
        core/* ɵɵelement */._UZ(387, "i", 102);
        core/* ɵɵelementStart */.TgZ(388, "p");
        core/* ɵɵtext */._uU(389, "nc-tv-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(390, "li");
        core/* ɵɵelement */._UZ(391, "i", 103);
        core/* ɵɵelementStart */.TgZ(392, "p");
        core/* ɵɵtext */._uU(393, "nc-umbrella-13");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(394, "li");
        core/* ɵɵelement */._UZ(395, "i", 104);
        core/* ɵɵelementStart */.TgZ(396, "p");
        core/* ɵɵtext */._uU(397, "nc-user-run");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(398, "li");
        core/* ɵɵelement */._UZ(399, "i", 105);
        core/* ɵɵelementStart */.TgZ(400, "p");
        core/* ɵɵtext */._uU(401, "nc-vector");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(402, "li");
        core/* ɵɵelement */._UZ(403, "i", 106);
        core/* ɵɵelementStart */.TgZ(404, "p");
        core/* ɵɵtext */._uU(405, "nc-watch-time");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(406, "li");
        core/* ɵɵelement */._UZ(407, "i", 107);
        core/* ɵɵelementStart */.TgZ(408, "p");
        core/* ɵɵtext */._uU(409, "nc-world-2");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(410, "li");
        core/* ɵɵelement */._UZ(411, "i", 108);
        core/* ɵɵelementStart */.TgZ(412, "p");
        core/* ɵɵtext */._uU(413, "nc-zoom-split");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } }, encapsulation: 2 });

// EXTERNAL MODULE: ./node_modules/ngx-toastr/__ivy_ngcc__/fesm2015/ngx-toastr.js
var ngx_toastr = __webpack_require__(9344);
;// CONCATENATED MODULE: ./src/app/pages/notifications/notifications.component.ts



class NotificationsComponent {
    constructor(toastr) {
        this.toastr = toastr;
    }
    showNotification(from, align) {
        const color = Math.floor(Math.random() * 5 + 1);
        switch (color) {
            case 1:
                this.toastr.info('<span data-notify="icon" class="nc-icon nc-bell-55"></span><span data-notify="message">Welcome to <b>Paper Dashboard Angular</b> - a beautiful bootstrap dashboard for every web developer.</span>', '', {
                    timeOut: 4000,
                    closeButton: true,
                    enableHtml: true,
                    toastClass: 'alert alert-info alert-with-icon',
                    positionClass: 'toast-' + from + '-' + align
                });
                break;
            case 2:
                this.toastr.success('<span data-notify="icon" class="nc-icon nc-bell-55"></span><span data-notify="message">Welcome to <b>Paper Dashboard Angular</b> - a beautiful bootstrap dashboard for every web developer.</span>', '', {
                    timeOut: 4000,
                    closeButton: true,
                    enableHtml: true,
                    toastClass: 'alert alert-success alert-with-icon',
                    positionClass: 'toast-' + from + '-' + align
                });
                break;
            case 3:
                this.toastr.warning('<span data-notify="icon" class="nc-icon nc-bell-55"></span><span data-notify="message">Welcome to <b>Paper Dashboard Angular</b> - a beautiful bootstrap dashboard for every web developer.</span>', '', {
                    timeOut: 4000,
                    closeButton: true,
                    enableHtml: true,
                    toastClass: 'alert alert-warning alert-with-icon',
                    positionClass: 'toast-' + from + '-' + align
                });
                break;
            case 4:
                this.toastr.error('<span data-notify="icon" class="nc-icon nc-bell-55"></span><span data-notify="message">Welcome to <b>Paper Dashboard Angular</b> - a beautiful bootstrap dashboard for every web developer.</span>', '', {
                    timeOut: 4000,
                    enableHtml: true,
                    closeButton: true,
                    toastClass: 'alert alert-danger alert-with-icon',
                    positionClass: 'toast-' + from + '-' + align
                });
                break;
            case 5:
                this.toastr.show('<span data-notify="icon" class="nc-icon nc-bell-55"></span><span data-notify="message">Welcome to <b>Paper Dashboard Angular</b> - a beautiful bootstrap dashboard for every web developer.</span>', '', {
                    timeOut: 4000,
                    closeButton: true,
                    enableHtml: true,
                    toastClass: 'alert alert-primary alert-with-icon',
                    positionClass: 'toast-' + from + '-' + align
                });
                break;
            default:
                break;
        }
    }
}
NotificationsComponent.ɵfac = function NotificationsComponent_Factory(t) { return new (t || NotificationsComponent)(core/* ɵɵdirectiveInject */.Y36(ngx_toastr/* ToastrService */._W)); };
NotificationsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: NotificationsComponent, selectors: [["notifications-cmp"]], decls: 117, vars: 0, consts: [[1, "row"], [1, "col-md-12"], [1, "card"], [1, "card-header"], [1, "card-title"], [1, "card-category"], ["href", "https://github.com/mouse0270", "target", "_blank"], ["href", "http://bootstrap-notify.remabledesigns.com/", "target", "_blank"], [1, "card-body"], [1, "col-md-6"], [1, "card", "card-plain"], [1, "alert", "alert-info"], [1, "alert", "alert-info", "alert-dismissible", "fade", "show"], ["aria-hidden", "true", "aria-label", "Close", "data-dismiss", "alert", "type", "button", 1, "close"], [1, "nc-icon", "nc-simple-remove"], ["data-notify", "container", 1, "alert", "alert-info", "alert-with-icon", "alert-dismissible", "fade", "show"], ["data-notify", "icon", 1, "nc-icon", "nc-bell-55"], ["data-notify", "message"], ["data-notify", "icon", 1, "nc-icon", "nc-chart-pie-36"], [1, "alert", "alert-primary", "alert-dismissible", "fade", "show"], [1, "alert", "alert-success", "alert-dismissible", "fade", "show"], [1, "alert", "alert-warning", "alert-dismissible", "fade", "show"], [1, "alert", "alert-danger", "alert-dismissible", "fade", "show"], [1, "places-buttons"], [1, "col-md-6", "ml-auto", "mr-auto", "text-center"], [1, "category"], [1, "col-lg-8", "ml-auto", "mr-auto"], [1, "col-md-4"], [1, "btn", "btn-primary", "btn-block", 3, "click"]], template: function NotificationsComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1);
        core/* ɵɵelementStart */.TgZ(2, "div", 2);
        core/* ɵɵelementStart */.TgZ(3, "div", 3);
        core/* ɵɵelementStart */.TgZ(4, "h5", 4);
        core/* ɵɵtext */._uU(5, "Notifications");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(6, "p", 5);
        core/* ɵɵtext */._uU(7, "Handcrafted by our friend ");
        core/* ɵɵelementStart */.TgZ(8, "a", 6);
        core/* ɵɵtext */._uU(9, "Robert McIntosh");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(10, ". Please checkout the ");
        core/* ɵɵelementStart */.TgZ(11, "a", 7);
        core/* ɵɵtext */._uU(12, "full documentation.");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(13, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 0);
        core/* ɵɵelementStart */.TgZ(15, "div", 9);
        core/* ɵɵelementStart */.TgZ(16, "div", 10);
        core/* ɵɵelementStart */.TgZ(17, "div", 3);
        core/* ɵɵelementStart */.TgZ(18, "h5", 4);
        core/* ɵɵtext */._uU(19, "Notifications Style");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 8);
        core/* ɵɵelementStart */.TgZ(21, "div", 11);
        core/* ɵɵelementStart */.TgZ(22, "span");
        core/* ɵɵtext */._uU(23, "This is a plain notification");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "div", 12);
        core/* ɵɵelementStart */.TgZ(25, "button", 13);
        core/* ɵɵelement */._UZ(26, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(27, "span");
        core/* ɵɵtext */._uU(28, "This is a notification with close button.");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(29, "div", 15);
        core/* ɵɵelementStart */.TgZ(30, "button", 13);
        core/* ɵɵelement */._UZ(31, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(32, "span", 16);
        core/* ɵɵelementStart */.TgZ(33, "span", 17);
        core/* ɵɵtext */._uU(34, "This is a notification with close button and icon.");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(35, "div", 15);
        core/* ɵɵelementStart */.TgZ(36, "button", 13);
        core/* ɵɵelement */._UZ(37, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(38, "span", 18);
        core/* ɵɵelementStart */.TgZ(39, "span", 17);
        core/* ɵɵtext */._uU(40, "This is a notification with close button and icon and have many lines. You can see that the icon and the close button are always vertically aligned. This is a beautiful notification. So you don't have to worry about the style.");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(41, "div", 9);
        core/* ɵɵelementStart */.TgZ(42, "div", 10);
        core/* ɵɵelementStart */.TgZ(43, "div", 3);
        core/* ɵɵelementStart */.TgZ(44, "h5", 4);
        core/* ɵɵtext */._uU(45, "Notification states");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(46, "div", 8);
        core/* ɵɵelementStart */.TgZ(47, "div", 19);
        core/* ɵɵelementStart */.TgZ(48, "button", 13);
        core/* ɵɵelement */._UZ(49, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(50, "span");
        core/* ɵɵelementStart */.TgZ(51, "b");
        core/* ɵɵtext */._uU(52, " Primary - ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(53, " This is a regular notification made with \".alert-primary\"");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(54, "div", 12);
        core/* ɵɵelementStart */.TgZ(55, "button", 13);
        core/* ɵɵelement */._UZ(56, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(57, "span");
        core/* ɵɵelementStart */.TgZ(58, "b");
        core/* ɵɵtext */._uU(59, " Info - ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(60, " This is a regular notification made with \".alert-info\"");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(61, "div", 20);
        core/* ɵɵelementStart */.TgZ(62, "button", 13);
        core/* ɵɵelement */._UZ(63, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(64, "span");
        core/* ɵɵelementStart */.TgZ(65, "b");
        core/* ɵɵtext */._uU(66, " Success - ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(67, " This is a regular notification made with \".alert-success\"");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(68, "div", 21);
        core/* ɵɵelementStart */.TgZ(69, "button", 13);
        core/* ɵɵelement */._UZ(70, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(71, "span");
        core/* ɵɵelementStart */.TgZ(72, "b");
        core/* ɵɵtext */._uU(73, " Warning - ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(74, " This is a regular notification made with \".alert-warning\"");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(75, "div", 22);
        core/* ɵɵelementStart */.TgZ(76, "button", 13);
        core/* ɵɵelement */._UZ(77, "i", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(78, "span");
        core/* ɵɵelementStart */.TgZ(79, "b");
        core/* ɵɵtext */._uU(80, " Danger - ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtext */._uU(81, " This is a regular notification made with \".alert-danger\"");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(82, "div", 0);
        core/* ɵɵelementStart */.TgZ(83, "div", 1);
        core/* ɵɵelementStart */.TgZ(84, "div", 2);
        core/* ɵɵelementStart */.TgZ(85, "div", 8);
        core/* ɵɵelementStart */.TgZ(86, "div", 23);
        core/* ɵɵelementStart */.TgZ(87, "div", 0);
        core/* ɵɵelementStart */.TgZ(88, "div", 24);
        core/* ɵɵelementStart */.TgZ(89, "h4", 4);
        core/* ɵɵtext */._uU(90, " Notifications Places ");
        core/* ɵɵelementStart */.TgZ(91, "p", 25);
        core/* ɵɵtext */._uU(92, "Click to view notifications");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(93, "div", 0);
        core/* ɵɵelementStart */.TgZ(94, "div", 26);
        core/* ɵɵelementStart */.TgZ(95, "div", 0);
        core/* ɵɵelementStart */.TgZ(96, "div", 27);
        core/* ɵɵelementStart */.TgZ(97, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_97_listener() { return ctx.showNotification("top", "left"); });
        core/* ɵɵtext */._uU(98, "Top Left");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(99, "div", 27);
        core/* ɵɵelementStart */.TgZ(100, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_100_listener() { return ctx.showNotification("top", "center"); });
        core/* ɵɵtext */._uU(101, "Top Center ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(102, "div", 27);
        core/* ɵɵelementStart */.TgZ(103, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_103_listener() { return ctx.showNotification("top", "right"); });
        core/* ɵɵtext */._uU(104, "Top Right");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(105, "div", 0);
        core/* ɵɵelementStart */.TgZ(106, "div", 26);
        core/* ɵɵelementStart */.TgZ(107, "div", 0);
        core/* ɵɵelementStart */.TgZ(108, "div", 27);
        core/* ɵɵelementStart */.TgZ(109, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_109_listener() { return ctx.showNotification("bottom", "left"); });
        core/* ɵɵtext */._uU(110, "Bottom Left ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(111, "div", 27);
        core/* ɵɵelementStart */.TgZ(112, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_112_listener() { return ctx.showNotification("bottom", "center"); });
        core/* ɵɵtext */._uU(113, "Bottom Center ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(114, "div", 27);
        core/* ɵɵelementStart */.TgZ(115, "button", 28);
        core/* ɵɵlistener */.NdJ("click", function NotificationsComponent_Template_button_click_115_listener() { return ctx.showNotification("bottom", "right"); });
        core/* ɵɵtext */._uU(116, "Bottom Right ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } }, encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/snackbar/snackbar.component.ts



class SnackbarComponent {
    constructor(utilityService) {
        this.utilityService = utilityService;
        this.displayText = "I don't even know what to say...";
        this.displayText = localStorage.getItem('f1-vote-message');
    }
}
SnackbarComponent.ɵfac = function SnackbarComponent_Factory(t) { return new (t || SnackbarComponent)(core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
SnackbarComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: SnackbarComponent, selectors: [["snackbar-component-default"]], decls: 2, vars: 1, template: function SnackbarComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "span");
        core/* ɵɵtext */._uU(1);
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵtextInterpolate1 */.hij(" ", ctx.displayText, "\n");
    } }, styles: [".hotpink-css[_ngcontent-%COMP%]{background-color:hotpink}"] });

// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/snack-bar.js
var snack_bar = __webpack_require__(7001);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/a11y.js
var a11y = __webpack_require__(9238);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/collections.js
var collections = __webpack_require__(8345);
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/button-toggle.js







/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Injection token that can be used to configure the
 * default options for all button toggles within an app.
 */




const button_toggle_c0 = ["button"];
const button_toggle_c1 = ["*"];
const MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS = new core/* InjectionToken */.OlP('MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS');
/**
 * Injection token that can be used to reference instances of `MatButtonToggleGroup`.
 * It serves as alternative token to the actual `MatButtonToggleGroup` class which
 * could cause unnecessary retention of the class and its component metadata.
 */
const MAT_BUTTON_TOGGLE_GROUP = new core/* InjectionToken */.OlP('MatButtonToggleGroup');
/**
 * Provider Expression that allows mat-button-toggle-group to register as a ControlValueAccessor.
 * This allows it to support [(ngModel)].
 * @docs-private
 */
const MAT_BUTTON_TOGGLE_GROUP_VALUE_ACCESSOR = {
    provide: fesm2015_forms/* NG_VALUE_ACCESSOR */.JU,
    useExisting: (0,core/* forwardRef */.Gpc)(() => MatButtonToggleGroup),
    multi: true
};
// Counter used to generate unique IDs.
let uniqueIdCounter = 0;
/** Change event object emitted by MatButtonToggle. */
class MatButtonToggleChange {
    constructor(
    /** The MatButtonToggle that emits the event. */
    source, 
    /** The value assigned to the MatButtonToggle. */
    value) {
        this.source = source;
        this.value = value;
    }
}
/** Exclusive selection button toggle group that behaves like a radio-button group. */
class MatButtonToggleGroup {
    constructor(_changeDetector, defaultOptions) {
        this._changeDetector = _changeDetector;
        this._vertical = false;
        this._multiple = false;
        this._disabled = false;
        /**
         * The method to be called in order to update ngModel.
         * Now `ngModel` binding is not supported in multiple selection mode.
         */
        this._controlValueAccessorChangeFn = () => { };
        /** onTouch function registered via registerOnTouch (ControlValueAccessor). */
        this._onTouched = () => { };
        this._name = `mat-button-toggle-group-${uniqueIdCounter++}`;
        /**
         * Event that emits whenever the value of the group changes.
         * Used to facilitate two-way data binding.
         * @docs-private
         */
        this.valueChange = new core/* EventEmitter */.vpe();
        /** Event emitted when the group's value changes. */
        this.change = new core/* EventEmitter */.vpe();
        this.appearance =
            defaultOptions && defaultOptions.appearance ? defaultOptions.appearance : 'standard';
    }
    /** `name` attribute for the underlying `input` element. */
    get name() { return this._name; }
    set name(value) {
        this._name = value;
        if (this._buttonToggles) {
            this._buttonToggles.forEach(toggle => {
                toggle.name = this._name;
                toggle._markForCheck();
            });
        }
    }
    /** Whether the toggle group is vertical. */
    get vertical() { return this._vertical; }
    set vertical(value) {
        this._vertical = (0,coercion/* coerceBooleanProperty */.Ig)(value);
    }
    /** Value of the toggle group. */
    get value() {
        const selected = this._selectionModel ? this._selectionModel.selected : [];
        if (this.multiple) {
            return selected.map(toggle => toggle.value);
        }
        return selected[0] ? selected[0].value : undefined;
    }
    set value(newValue) {
        this._setSelectionByValue(newValue);
        this.valueChange.emit(this.value);
    }
    /** Selected button toggles in the group. */
    get selected() {
        const selected = this._selectionModel ? this._selectionModel.selected : [];
        return this.multiple ? selected : (selected[0] || null);
    }
    /** Whether multiple button toggles can be selected. */
    get multiple() { return this._multiple; }
    set multiple(value) {
        this._multiple = (0,coercion/* coerceBooleanProperty */.Ig)(value);
    }
    /** Whether multiple button toggle group is disabled. */
    get disabled() { return this._disabled; }
    set disabled(value) {
        this._disabled = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        if (this._buttonToggles) {
            this._buttonToggles.forEach(toggle => toggle._markForCheck());
        }
    }
    ngOnInit() {
        this._selectionModel = new collections/* SelectionModel */.Ov(this.multiple, undefined, false);
    }
    ngAfterContentInit() {
        this._selectionModel.select(...this._buttonToggles.filter(toggle => toggle.checked));
    }
    /**
     * Sets the model value. Implemented as part of ControlValueAccessor.
     * @param value Value to be set to the model.
     */
    writeValue(value) {
        this.value = value;
        this._changeDetector.markForCheck();
    }
    // Implemented as part of ControlValueAccessor.
    registerOnChange(fn) {
        this._controlValueAccessorChangeFn = fn;
    }
    // Implemented as part of ControlValueAccessor.
    registerOnTouched(fn) {
        this._onTouched = fn;
    }
    // Implemented as part of ControlValueAccessor.
    setDisabledState(isDisabled) {
        this.disabled = isDisabled;
    }
    /** Dispatch change event with current selection and group value. */
    _emitChangeEvent() {
        const selected = this.selected;
        const source = Array.isArray(selected) ? selected[selected.length - 1] : selected;
        const event = new MatButtonToggleChange(source, this.value);
        this._controlValueAccessorChangeFn(event.value);
        this.change.emit(event);
    }
    /**
     * Syncs a button toggle's selected state with the model value.
     * @param toggle Toggle to be synced.
     * @param select Whether the toggle should be selected.
     * @param isUserInput Whether the change was a result of a user interaction.
     * @param deferEvents Whether to defer emitting the change events.
     */
    _syncButtonToggle(toggle, select, isUserInput = false, deferEvents = false) {
        // Deselect the currently-selected toggle, if we're in single-selection
        // mode and the button being toggled isn't selected at the moment.
        if (!this.multiple && this.selected && !toggle.checked) {
            this.selected.checked = false;
        }
        if (this._selectionModel) {
            if (select) {
                this._selectionModel.select(toggle);
            }
            else {
                this._selectionModel.deselect(toggle);
            }
        }
        else {
            deferEvents = true;
        }
        // We need to defer in some cases in order to avoid "changed after checked errors", however
        // the side-effect is that we may end up updating the model value out of sequence in others
        // The `deferEvents` flag allows us to decide whether to do it on a case-by-case basis.
        if (deferEvents) {
            Promise.resolve().then(() => this._updateModelValue(isUserInput));
        }
        else {
            this._updateModelValue(isUserInput);
        }
    }
    /** Checks whether a button toggle is selected. */
    _isSelected(toggle) {
        return this._selectionModel && this._selectionModel.isSelected(toggle);
    }
    /** Determines whether a button toggle should be checked on init. */
    _isPrechecked(toggle) {
        if (typeof this._rawValue === 'undefined') {
            return false;
        }
        if (this.multiple && Array.isArray(this._rawValue)) {
            return this._rawValue.some(value => toggle.value != null && value === toggle.value);
        }
        return toggle.value === this._rawValue;
    }
    /** Updates the selection state of the toggles in the group based on a value. */
    _setSelectionByValue(value) {
        this._rawValue = value;
        if (!this._buttonToggles) {
            return;
        }
        if (this.multiple && value) {
            if (!Array.isArray(value) && (typeof ngDevMode === 'undefined' || ngDevMode)) {
                throw Error('Value must be an array in multiple-selection mode.');
            }
            this._clearSelection();
            value.forEach((currentValue) => this._selectValue(currentValue));
        }
        else {
            this._clearSelection();
            this._selectValue(value);
        }
    }
    /** Clears the selected toggles. */
    _clearSelection() {
        this._selectionModel.clear();
        this._buttonToggles.forEach(toggle => toggle.checked = false);
    }
    /** Selects a value if there's a toggle that corresponds to it. */
    _selectValue(value) {
        const correspondingOption = this._buttonToggles.find(toggle => {
            return toggle.value != null && toggle.value === value;
        });
        if (correspondingOption) {
            correspondingOption.checked = true;
            this._selectionModel.select(correspondingOption);
        }
    }
    /** Syncs up the group's value with the model and emits the change event. */
    _updateModelValue(isUserInput) {
        // Only emit the change event for user input.
        if (isUserInput) {
            this._emitChangeEvent();
        }
        // Note: we emit this one no matter whether it was a user interaction, because
        // it is used by Angular to sync up the two-way data binding.
        this.valueChange.emit(this.value);
    }
}
MatButtonToggleGroup.ɵfac = function MatButtonToggleGroup_Factory(t) { return new (t || MatButtonToggleGroup)(core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS, 8)); };
MatButtonToggleGroup.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatButtonToggleGroup, selectors: [["mat-button-toggle-group"]], contentQueries: function MatButtonToggleGroup_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatButtonToggle, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._buttonToggles = _t);
    } }, hostAttrs: ["role", "group", 1, "mat-button-toggle-group"], hostVars: 5, hostBindings: function MatButtonToggleGroup_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵattribute */.uIk("aria-disabled", ctx.disabled);
        core/* ɵɵclassProp */.ekj("mat-button-toggle-vertical", ctx.vertical)("mat-button-toggle-group-appearance-standard", ctx.appearance === "standard");
    } }, inputs: { appearance: "appearance", name: "name", vertical: "vertical", value: "value", multiple: "multiple", disabled: "disabled" }, outputs: { valueChange: "valueChange", change: "change" }, exportAs: ["matButtonToggleGroup"], features: [core/* ɵɵProvidersFeature */._Bn([
            MAT_BUTTON_TOGGLE_GROUP_VALUE_ACCESSOR,
            { provide: MAT_BUTTON_TOGGLE_GROUP, useExisting: MatButtonToggleGroup },
        ])] });
MatButtonToggleGroup.ctorParameters = () => [
    { type: core/* ChangeDetectorRef */.sBO },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS,] }] }
];
MatButtonToggleGroup.propDecorators = {
    _buttonToggles: [{ type: core/* ContentChildren */.AcB, args: [(0,core/* forwardRef */.Gpc)(() => MatButtonToggle), {
                    // Note that this would technically pick up toggles
                    // from nested groups, but that's not a case that we support.
                    descendants: true
                },] }],
    appearance: [{ type: core/* Input */.IIB }],
    name: [{ type: core/* Input */.IIB }],
    vertical: [{ type: core/* Input */.IIB }],
    value: [{ type: core/* Input */.IIB }],
    valueChange: [{ type: core/* Output */.r_U }],
    multiple: [{ type: core/* Input */.IIB }],
    disabled: [{ type: core/* Input */.IIB }],
    change: [{ type: core/* Output */.r_U }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatButtonToggleGroup, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-button-toggle-group',
                providers: [
                    MAT_BUTTON_TOGGLE_GROUP_VALUE_ACCESSOR,
                    { provide: MAT_BUTTON_TOGGLE_GROUP, useExisting: MatButtonToggleGroup },
                ],
                host: {
                    'role': 'group',
                    'class': 'mat-button-toggle-group',
                    '[attr.aria-disabled]': 'disabled',
                    '[class.mat-button-toggle-vertical]': 'vertical',
                    '[class.mat-button-toggle-group-appearance-standard]': 'appearance === "standard"'
                },
                exportAs: 'matButtonToggleGroup'
            }]
    }], function () { return [{ type: core/* ChangeDetectorRef */.sBO }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS]
            }] }]; }, { valueChange: [{
            type: core/* Output */.r_U
        }], change: [{
            type: core/* Output */.r_U
        }], appearance: [{
            type: core/* Input */.IIB
        }], name: [{
            type: core/* Input */.IIB
        }], vertical: [{
            type: core/* Input */.IIB
        }], value: [{
            type: core/* Input */.IIB
        }], multiple: [{
            type: core/* Input */.IIB
        }], disabled: [{
            type: core/* Input */.IIB
        }], _buttonToggles: [{
            type: core/* ContentChildren */.AcB,
            args: [(0,core/* forwardRef */.Gpc)(() => MatButtonToggle), {
                    // Note that this would technically pick up toggles
                    // from nested groups, but that's not a case that we support.
                    descendants: true
                }]
        }] }); })();
// Boilerplate for applying mixins to the MatButtonToggle class.
/** @docs-private */
const _MatButtonToggleBase = (0,fesm2015_core/* mixinDisableRipple */.Kr)(class {
});
/** Single button inside of a toggle group. */
class MatButtonToggle extends _MatButtonToggleBase {
    constructor(toggleGroup, _changeDetectorRef, _elementRef, _focusMonitor, defaultTabIndex, defaultOptions) {
        super();
        this._changeDetectorRef = _changeDetectorRef;
        this._elementRef = _elementRef;
        this._focusMonitor = _focusMonitor;
        this._isSingleSelector = false;
        this._checked = false;
        /**
         * Users can specify the `aria-labelledby` attribute which will be forwarded to the input element
         */
        this.ariaLabelledby = null;
        this._disabled = false;
        /** Event emitted when the group value changes. */
        this.change = new core/* EventEmitter */.vpe();
        const parsedTabIndex = Number(defaultTabIndex);
        this.tabIndex = (parsedTabIndex || parsedTabIndex === 0) ? parsedTabIndex : null;
        this.buttonToggleGroup = toggleGroup;
        this.appearance =
            defaultOptions && defaultOptions.appearance ? defaultOptions.appearance : 'standard';
    }
    /** Unique ID for the underlying `button` element. */
    get buttonId() { return `${this.id}-button`; }
    /** The appearance style of the button. */
    get appearance() {
        return this.buttonToggleGroup ? this.buttonToggleGroup.appearance : this._appearance;
    }
    set appearance(value) {
        this._appearance = value;
    }
    /** Whether the button is checked. */
    get checked() {
        return this.buttonToggleGroup ? this.buttonToggleGroup._isSelected(this) : this._checked;
    }
    set checked(value) {
        const newValue = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        if (newValue !== this._checked) {
            this._checked = newValue;
            if (this.buttonToggleGroup) {
                this.buttonToggleGroup._syncButtonToggle(this, this._checked);
            }
            this._changeDetectorRef.markForCheck();
        }
    }
    /** Whether the button is disabled. */
    get disabled() {
        return this._disabled || (this.buttonToggleGroup && this.buttonToggleGroup.disabled);
    }
    set disabled(value) { this._disabled = (0,coercion/* coerceBooleanProperty */.Ig)(value); }
    ngOnInit() {
        const group = this.buttonToggleGroup;
        this._isSingleSelector = group && !group.multiple;
        this.id = this.id || `mat-button-toggle-${uniqueIdCounter++}`;
        if (this._isSingleSelector) {
            this.name = group.name;
        }
        if (group) {
            if (group._isPrechecked(this)) {
                this.checked = true;
            }
            else if (group._isSelected(this) !== this._checked) {
                // As as side effect of the circular dependency between the toggle group and the button,
                // we may end up in a state where the button is supposed to be checked on init, but it
                // isn't, because the checked value was assigned too early. This can happen when Ivy
                // assigns the static input value before the `ngOnInit` has run.
                group._syncButtonToggle(this, this._checked);
            }
        }
    }
    ngAfterViewInit() {
        this._focusMonitor.monitor(this._elementRef, true);
    }
    ngOnDestroy() {
        const group = this.buttonToggleGroup;
        this._focusMonitor.stopMonitoring(this._elementRef);
        // Remove the toggle from the selection once it's destroyed. Needs to happen
        // on the next tick in order to avoid "changed after checked" errors.
        if (group && group._isSelected(this)) {
            group._syncButtonToggle(this, false, false, true);
        }
    }
    /** Focuses the button. */
    focus(options) {
        this._buttonElement.nativeElement.focus(options);
    }
    /** Checks the button toggle due to an interaction with the underlying native button. */
    _onButtonClick() {
        const newChecked = this._isSingleSelector ? true : !this._checked;
        if (newChecked !== this._checked) {
            this._checked = newChecked;
            if (this.buttonToggleGroup) {
                this.buttonToggleGroup._syncButtonToggle(this, this._checked, true);
                this.buttonToggleGroup._onTouched();
            }
        }
        // Emit a change event when it's the single selector
        this.change.emit(new MatButtonToggleChange(this, this.value));
    }
    /**
     * Marks the button toggle as needing checking for change detection.
     * This method is exposed because the parent button toggle group will directly
     * update bound properties of the radio button.
     */
    _markForCheck() {
        // When the group value changes, the button will not be notified.
        // Use `markForCheck` to explicit update button toggle's status.
        this._changeDetectorRef.markForCheck();
    }
}
MatButtonToggle.ɵfac = function MatButtonToggle_Factory(t) { return new (t || MatButtonToggle)(core/* ɵɵdirectiveInject */.Y36(MAT_BUTTON_TOGGLE_GROUP, 8), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusMonitor */.tE), core/* ɵɵinjectAttribute */.$8M('tabindex'), core/* ɵɵdirectiveInject */.Y36(MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS, 8)); };
MatButtonToggle.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatButtonToggle, selectors: [["mat-button-toggle"]], viewQuery: function MatButtonToggle_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(button_toggle_c0, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._buttonElement = _t.first);
    } }, hostAttrs: ["role", "presentation", 1, "mat-button-toggle"], hostVars: 12, hostBindings: function MatButtonToggle_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("focus", function MatButtonToggle_focus_HostBindingHandler() { return ctx.focus(); });
    } if (rf & 2) {
        core/* ɵɵattribute */.uIk("aria-label", null)("aria-labelledby", null)("id", ctx.id)("name", null);
        core/* ɵɵclassProp */.ekj("mat-button-toggle-standalone", !ctx.buttonToggleGroup)("mat-button-toggle-checked", ctx.checked)("mat-button-toggle-disabled", ctx.disabled)("mat-button-toggle-appearance-standard", ctx.appearance === "standard");
    } }, inputs: { disableRipple: "disableRipple", ariaLabelledby: ["aria-labelledby", "ariaLabelledby"], tabIndex: "tabIndex", appearance: "appearance", checked: "checked", disabled: "disabled", id: "id", name: "name", ariaLabel: ["aria-label", "ariaLabel"], value: "value" }, outputs: { change: "change" }, exportAs: ["matButtonToggle"], features: [core/* ɵɵInheritDefinitionFeature */.qOj], ngContentSelectors: button_toggle_c1, decls: 6, vars: 9, consts: [["type", "button", 1, "mat-button-toggle-button", "mat-focus-indicator", 3, "id", "disabled", "click"], ["button", ""], [1, "mat-button-toggle-label-content"], [1, "mat-button-toggle-focus-overlay"], ["matRipple", "", 1, "mat-button-toggle-ripple", 3, "matRippleTrigger", "matRippleDisabled"]], template: function MatButtonToggle_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t();
        core/* ɵɵelementStart */.TgZ(0, "button", 0, 1);
        core/* ɵɵlistener */.NdJ("click", function MatButtonToggle_Template_button_click_0_listener() { return ctx._onButtonClick(); });
        core/* ɵɵelementStart */.TgZ(2, "span", 2);
        core/* ɵɵprojection */.Hsn(3);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(4, "span", 3);
        core/* ɵɵelement */._UZ(5, "span", 4);
    } if (rf & 2) {
        const _r0 = core/* ɵɵreference */.MAs(1);
        core/* ɵɵproperty */.Q6J("id", ctx.buttonId)("disabled", ctx.disabled || null);
        core/* ɵɵattribute */.uIk("tabindex", ctx.disabled ? -1 : ctx.tabIndex)("aria-pressed", ctx.checked)("name", ctx.name || null)("aria-label", ctx.ariaLabel)("aria-labelledby", ctx.ariaLabelledby);
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("matRippleTrigger", _r0)("matRippleDisabled", ctx.disableRipple || ctx.disabled);
    } }, directives: [fesm2015_core/* MatRipple */.wG], styles: [".mat-button-toggle-standalone,.mat-button-toggle-group{position:relative;display:inline-flex;flex-direction:row;white-space:nowrap;overflow:hidden;border-radius:2px;-webkit-tap-highlight-color:transparent}.cdk-high-contrast-active .mat-button-toggle-standalone,.cdk-high-contrast-active .mat-button-toggle-group{outline:solid 1px}.mat-button-toggle-standalone.mat-button-toggle-appearance-standard,.mat-button-toggle-group-appearance-standard{border-radius:4px}.cdk-high-contrast-active .mat-button-toggle-standalone.mat-button-toggle-appearance-standard,.cdk-high-contrast-active .mat-button-toggle-group-appearance-standard{outline:0}.mat-button-toggle-vertical{flex-direction:column}.mat-button-toggle-vertical .mat-button-toggle-label-content{display:block}.mat-button-toggle{white-space:nowrap;position:relative}.mat-button-toggle .mat-icon svg{vertical-align:top}.mat-button-toggle.cdk-keyboard-focused .mat-button-toggle-focus-overlay{opacity:1}.cdk-high-contrast-active .mat-button-toggle.cdk-keyboard-focused .mat-button-toggle-focus-overlay{opacity:.5}.mat-button-toggle-appearance-standard:not(.mat-button-toggle-disabled):hover .mat-button-toggle-focus-overlay{opacity:.04}.mat-button-toggle-appearance-standard.cdk-keyboard-focused:not(.mat-button-toggle-disabled) .mat-button-toggle-focus-overlay{opacity:.12}.cdk-high-contrast-active .mat-button-toggle-appearance-standard.cdk-keyboard-focused:not(.mat-button-toggle-disabled) .mat-button-toggle-focus-overlay{opacity:.5}@media(hover: none){.mat-button-toggle-appearance-standard:not(.mat-button-toggle-disabled):hover .mat-button-toggle-focus-overlay{display:none}}.mat-button-toggle-label-content{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;display:inline-block;line-height:36px;padding:0 16px;position:relative}.mat-button-toggle-appearance-standard .mat-button-toggle-label-content{padding:0 12px}.mat-button-toggle-label-content>*{vertical-align:middle}.mat-button-toggle-focus-overlay{border-radius:inherit;pointer-events:none;opacity:0;top:0;left:0;right:0;bottom:0;position:absolute}.mat-button-toggle-checked .mat-button-toggle-focus-overlay{border-bottom:solid 36px}.cdk-high-contrast-active .mat-button-toggle-checked .mat-button-toggle-focus-overlay{opacity:.5;height:0}.cdk-high-contrast-active .mat-button-toggle-checked.mat-button-toggle-appearance-standard .mat-button-toggle-focus-overlay{border-bottom:solid 500px}.mat-button-toggle .mat-button-toggle-ripple{top:0;left:0;right:0;bottom:0;position:absolute;pointer-events:none}.mat-button-toggle-button{border:0;background:none;color:inherit;padding:0;margin:0;font:inherit;outline:none;width:100%;cursor:pointer}.mat-button-toggle-disabled .mat-button-toggle-button{cursor:default}.mat-button-toggle-button::-moz-focus-inner{border:0}\n"], encapsulation: 2, changeDetection: 0 });
MatButtonToggle.ctorParameters = () => [
    { type: MatButtonToggleGroup, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_BUTTON_TOGGLE_GROUP,] }] },
    { type: core/* ChangeDetectorRef */.sBO },
    { type: core/* ElementRef */.SBq },
    { type: a11y/* FocusMonitor */.tE },
    { type: String, decorators: [{ type: core/* Attribute */.ahi, args: ['tabindex',] }] },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS,] }] }
];
MatButtonToggle.propDecorators = {
    ariaLabel: [{ type: core/* Input */.IIB, args: ['aria-label',] }],
    ariaLabelledby: [{ type: core/* Input */.IIB, args: ['aria-labelledby',] }],
    _buttonElement: [{ type: core/* ViewChild */.i9L, args: ['button',] }],
    id: [{ type: core/* Input */.IIB }],
    name: [{ type: core/* Input */.IIB }],
    value: [{ type: core/* Input */.IIB }],
    tabIndex: [{ type: core/* Input */.IIB }],
    appearance: [{ type: core/* Input */.IIB }],
    checked: [{ type: core/* Input */.IIB }],
    disabled: [{ type: core/* Input */.IIB }],
    change: [{ type: core/* Output */.r_U }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatButtonToggle, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-button-toggle',
                template: "<button #button class=\"mat-button-toggle-button mat-focus-indicator\"\n        type=\"button\"\n        [id]=\"buttonId\"\n        [attr.tabindex]=\"disabled ? -1 : tabIndex\"\n        [attr.aria-pressed]=\"checked\"\n        [disabled]=\"disabled || null\"\n        [attr.name]=\"name || null\"\n        [attr.aria-label]=\"ariaLabel\"\n        [attr.aria-labelledby]=\"ariaLabelledby\"\n        (click)=\"_onButtonClick()\">\n  <span class=\"mat-button-toggle-label-content\">\n    <ng-content></ng-content>\n  </span>\n</button>\n\n<span class=\"mat-button-toggle-focus-overlay\"></span>\n<span class=\"mat-button-toggle-ripple\" matRipple\n     [matRippleTrigger]=\"button\"\n     [matRippleDisabled]=\"this.disableRipple || this.disabled\">\n</span>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                exportAs: 'matButtonToggle',
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                inputs: ['disableRipple'],
                host: {
                    '[class.mat-button-toggle-standalone]': '!buttonToggleGroup',
                    '[class.mat-button-toggle-checked]': 'checked',
                    '[class.mat-button-toggle-disabled]': 'disabled',
                    '[class.mat-button-toggle-appearance-standard]': 'appearance === "standard"',
                    'class': 'mat-button-toggle',
                    '[attr.aria-label]': 'null',
                    '[attr.aria-labelledby]': 'null',
                    '[attr.id]': 'id',
                    '[attr.name]': 'null',
                    '(focus)': 'focus()',
                    'role': 'presentation'
                },
                styles: [".mat-button-toggle-standalone,.mat-button-toggle-group{position:relative;display:inline-flex;flex-direction:row;white-space:nowrap;overflow:hidden;border-radius:2px;-webkit-tap-highlight-color:transparent}.cdk-high-contrast-active .mat-button-toggle-standalone,.cdk-high-contrast-active .mat-button-toggle-group{outline:solid 1px}.mat-button-toggle-standalone.mat-button-toggle-appearance-standard,.mat-button-toggle-group-appearance-standard{border-radius:4px}.cdk-high-contrast-active .mat-button-toggle-standalone.mat-button-toggle-appearance-standard,.cdk-high-contrast-active .mat-button-toggle-group-appearance-standard{outline:0}.mat-button-toggle-vertical{flex-direction:column}.mat-button-toggle-vertical .mat-button-toggle-label-content{display:block}.mat-button-toggle{white-space:nowrap;position:relative}.mat-button-toggle .mat-icon svg{vertical-align:top}.mat-button-toggle.cdk-keyboard-focused .mat-button-toggle-focus-overlay{opacity:1}.cdk-high-contrast-active .mat-button-toggle.cdk-keyboard-focused .mat-button-toggle-focus-overlay{opacity:.5}.mat-button-toggle-appearance-standard:not(.mat-button-toggle-disabled):hover .mat-button-toggle-focus-overlay{opacity:.04}.mat-button-toggle-appearance-standard.cdk-keyboard-focused:not(.mat-button-toggle-disabled) .mat-button-toggle-focus-overlay{opacity:.12}.cdk-high-contrast-active .mat-button-toggle-appearance-standard.cdk-keyboard-focused:not(.mat-button-toggle-disabled) .mat-button-toggle-focus-overlay{opacity:.5}@media(hover: none){.mat-button-toggle-appearance-standard:not(.mat-button-toggle-disabled):hover .mat-button-toggle-focus-overlay{display:none}}.mat-button-toggle-label-content{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;display:inline-block;line-height:36px;padding:0 16px;position:relative}.mat-button-toggle-appearance-standard .mat-button-toggle-label-content{padding:0 12px}.mat-button-toggle-label-content>*{vertical-align:middle}.mat-button-toggle-focus-overlay{border-radius:inherit;pointer-events:none;opacity:0;top:0;left:0;right:0;bottom:0;position:absolute}.mat-button-toggle-checked .mat-button-toggle-focus-overlay{border-bottom:solid 36px}.cdk-high-contrast-active .mat-button-toggle-checked .mat-button-toggle-focus-overlay{opacity:.5;height:0}.cdk-high-contrast-active .mat-button-toggle-checked.mat-button-toggle-appearance-standard .mat-button-toggle-focus-overlay{border-bottom:solid 500px}.mat-button-toggle .mat-button-toggle-ripple{top:0;left:0;right:0;bottom:0;position:absolute;pointer-events:none}.mat-button-toggle-button{border:0;background:none;color:inherit;padding:0;margin:0;font:inherit;outline:none;width:100%;cursor:pointer}.mat-button-toggle-disabled .mat-button-toggle-button{cursor:default}.mat-button-toggle-button::-moz-focus-inner{border:0}\n"]
            }]
    }], function () { return [{ type: MatButtonToggleGroup, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_BUTTON_TOGGLE_GROUP]
            }] }, { type: core/* ChangeDetectorRef */.sBO }, { type: core/* ElementRef */.SBq }, { type: a11y/* FocusMonitor */.tE }, { type: String, decorators: [{
                type: core/* Attribute */.ahi,
                args: ['tabindex']
            }] }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS]
            }] }]; }, { ariaLabelledby: [{
            type: core/* Input */.IIB,
            args: ['aria-labelledby']
        }], change: [{
            type: core/* Output */.r_U
        }], tabIndex: [{
            type: core/* Input */.IIB
        }], appearance: [{
            type: core/* Input */.IIB
        }], checked: [{
            type: core/* Input */.IIB
        }], disabled: [{
            type: core/* Input */.IIB
        }], id: [{
            type: core/* Input */.IIB
        }], name: [{
            type: core/* Input */.IIB
        }], ariaLabel: [{
            type: core/* Input */.IIB,
            args: ['aria-label']
        }], _buttonElement: [{
            type: core/* ViewChild */.i9L,
            args: ['button']
        }], value: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatButtonToggleModule {
}
MatButtonToggleModule.ɵfac = function MatButtonToggleModule_Factory(t) { return new (t || MatButtonToggleModule)(); };
MatButtonToggleModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatButtonToggleModule });
MatButtonToggleModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[fesm2015_core/* MatCommonModule */.BQ, fesm2015_core/* MatRippleModule */.si], fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatButtonToggleModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [fesm2015_core/* MatCommonModule */.BQ, fesm2015_core/* MatRippleModule */.si],
                exports: [fesm2015_core/* MatCommonModule */.BQ, MatButtonToggleGroup, MatButtonToggle],
                declarations: [MatButtonToggleGroup, MatButtonToggle]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatButtonToggleModule, { declarations: function () { return [MatButtonToggleGroup, MatButtonToggle]; }, imports: function () { return [fesm2015_core/* MatCommonModule */.BQ, fesm2015_core/* MatRippleModule */.si]; }, exports: function () { return [fesm2015_core/* MatCommonModule */.BQ, MatButtonToggleGroup, MatButtonToggle]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=button-toggle.js.map
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/card.js




/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Content of a card, needed as it's used as a selector in the API.
 * @docs-private
 */


const card_c0 = ["*", [["mat-card-footer"]]];
const card_c1 = ["*", "mat-card-footer"];
const card_c2 = [[["", "mat-card-avatar", ""], ["", "matCardAvatar", ""]], [["mat-card-title"], ["mat-card-subtitle"], ["", "mat-card-title", ""], ["", "mat-card-subtitle", ""], ["", "matCardTitle", ""], ["", "matCardSubtitle", ""]], "*"];
const card_c3 = ["[mat-card-avatar], [matCardAvatar]", "mat-card-title, mat-card-subtitle,\n      [mat-card-title], [mat-card-subtitle],\n      [matCardTitle], [matCardSubtitle]", "*"];
const card_c4 = [[["mat-card-title"], ["mat-card-subtitle"], ["", "mat-card-title", ""], ["", "mat-card-subtitle", ""], ["", "matCardTitle", ""], ["", "matCardSubtitle", ""]], [["img"]], "*"];
const card_c5 = ["mat-card-title, mat-card-subtitle,\n      [mat-card-title], [mat-card-subtitle],\n      [matCardTitle], [matCardSubtitle]", "img", "*"];
class MatCardContent {
}
MatCardContent.ɵfac = function MatCardContent_Factory(t) { return new (t || MatCardContent)(); };
MatCardContent.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardContent, selectors: [["mat-card-content"], ["", "mat-card-content", ""], ["", "matCardContent", ""]], hostAttrs: [1, "mat-card-content"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardContent, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-card-content, [mat-card-content], [matCardContent]',
                host: { 'class': 'mat-card-content' }
            }]
    }], null, null); })();
/**
 * Title of a card, needed as it's used as a selector in the API.
 * @docs-private
 */
class MatCardTitle {
}
MatCardTitle.ɵfac = function MatCardTitle_Factory(t) { return new (t || MatCardTitle)(); };
MatCardTitle.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardTitle, selectors: [["mat-card-title"], ["", "mat-card-title", ""], ["", "matCardTitle", ""]], hostAttrs: [1, "mat-card-title"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardTitle, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: `mat-card-title, [mat-card-title], [matCardTitle]`,
                host: {
                    'class': 'mat-card-title'
                }
            }]
    }], null, null); })();
/**
 * Sub-title of a card, needed as it's used as a selector in the API.
 * @docs-private
 */
class MatCardSubtitle {
}
MatCardSubtitle.ɵfac = function MatCardSubtitle_Factory(t) { return new (t || MatCardSubtitle)(); };
MatCardSubtitle.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardSubtitle, selectors: [["mat-card-subtitle"], ["", "mat-card-subtitle", ""], ["", "matCardSubtitle", ""]], hostAttrs: [1, "mat-card-subtitle"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardSubtitle, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: `mat-card-subtitle, [mat-card-subtitle], [matCardSubtitle]`,
                host: {
                    'class': 'mat-card-subtitle'
                }
            }]
    }], null, null); })();
/**
 * Action section of a card, needed as it's used as a selector in the API.
 * @docs-private
 */
class MatCardActions {
    constructor() {
        /** Position of the actions inside the card. */
        this.align = 'start';
    }
}
MatCardActions.ɵfac = function MatCardActions_Factory(t) { return new (t || MatCardActions)(); };
MatCardActions.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardActions, selectors: [["mat-card-actions"]], hostAttrs: [1, "mat-card-actions"], hostVars: 2, hostBindings: function MatCardActions_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵclassProp */.ekj("mat-card-actions-align-end", ctx.align === "end");
    } }, inputs: { align: "align" }, exportAs: ["matCardActions"] });
MatCardActions.propDecorators = {
    align: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardActions, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-card-actions',
                exportAs: 'matCardActions',
                host: {
                    'class': 'mat-card-actions',
                    '[class.mat-card-actions-align-end]': 'align === "end"'
                }
            }]
    }], function () { return []; }, { align: [{
            type: core/* Input */.IIB
        }] }); })();
/**
 * Footer of a card, needed as it's used as a selector in the API.
 * @docs-private
 */
class MatCardFooter {
}
MatCardFooter.ɵfac = function MatCardFooter_Factory(t) { return new (t || MatCardFooter)(); };
MatCardFooter.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardFooter, selectors: [["mat-card-footer"]], hostAttrs: [1, "mat-card-footer"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardFooter, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-card-footer',
                host: { 'class': 'mat-card-footer' }
            }]
    }], null, null); })();
/**
 * Image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardImage {
}
MatCardImage.ɵfac = function MatCardImage_Factory(t) { return new (t || MatCardImage)(); };
MatCardImage.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardImage, selectors: [["", "mat-card-image", ""], ["", "matCardImage", ""]], hostAttrs: [1, "mat-card-image"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardImage, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-image], [matCardImage]',
                host: { 'class': 'mat-card-image' }
            }]
    }], null, null); })();
/**
 * Image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardSmImage {
}
MatCardSmImage.ɵfac = function MatCardSmImage_Factory(t) { return new (t || MatCardSmImage)(); };
MatCardSmImage.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardSmImage, selectors: [["", "mat-card-sm-image", ""], ["", "matCardImageSmall", ""]], hostAttrs: [1, "mat-card-sm-image"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardSmImage, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-sm-image], [matCardImageSmall]',
                host: { 'class': 'mat-card-sm-image' }
            }]
    }], null, null); })();
/**
 * Image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardMdImage {
}
MatCardMdImage.ɵfac = function MatCardMdImage_Factory(t) { return new (t || MatCardMdImage)(); };
MatCardMdImage.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardMdImage, selectors: [["", "mat-card-md-image", ""], ["", "matCardImageMedium", ""]], hostAttrs: [1, "mat-card-md-image"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardMdImage, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-md-image], [matCardImageMedium]',
                host: { 'class': 'mat-card-md-image' }
            }]
    }], null, null); })();
/**
 * Image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardLgImage {
}
MatCardLgImage.ɵfac = function MatCardLgImage_Factory(t) { return new (t || MatCardLgImage)(); };
MatCardLgImage.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardLgImage, selectors: [["", "mat-card-lg-image", ""], ["", "matCardImageLarge", ""]], hostAttrs: [1, "mat-card-lg-image"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardLgImage, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-lg-image], [matCardImageLarge]',
                host: { 'class': 'mat-card-lg-image' }
            }]
    }], null, null); })();
/**
 * Large image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardXlImage {
}
MatCardXlImage.ɵfac = function MatCardXlImage_Factory(t) { return new (t || MatCardXlImage)(); };
MatCardXlImage.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardXlImage, selectors: [["", "mat-card-xl-image", ""], ["", "matCardImageXLarge", ""]], hostAttrs: [1, "mat-card-xl-image"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardXlImage, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-xl-image], [matCardImageXLarge]',
                host: { 'class': 'mat-card-xl-image' }
            }]
    }], null, null); })();
/**
 * Avatar image used in a card, needed to add the mat- CSS styling.
 * @docs-private
 */
class MatCardAvatar {
}
MatCardAvatar.ɵfac = function MatCardAvatar_Factory(t) { return new (t || MatCardAvatar)(); };
MatCardAvatar.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatCardAvatar, selectors: [["", "mat-card-avatar", ""], ["", "matCardAvatar", ""]], hostAttrs: [1, "mat-card-avatar"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardAvatar, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-card-avatar], [matCardAvatar]',
                host: { 'class': 'mat-card-avatar' }
            }]
    }], null, null); })();
/**
 * A basic content container component that adds the styles of a Material design card.
 *
 * While this component can be used alone, it also provides a number
 * of preset styles for common card sections, including:
 * - mat-card-title
 * - mat-card-subtitle
 * - mat-card-content
 * - mat-card-actions
 * - mat-card-footer
 */
class MatCard {
    // @breaking-change 9.0.0 `_animationMode` parameter to be made required.
    constructor(_animationMode) {
        this._animationMode = _animationMode;
    }
}
MatCard.ɵfac = function MatCard_Factory(t) { return new (t || MatCard)(core/* ɵɵdirectiveInject */.Y36(fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb, 8)); };
MatCard.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatCard, selectors: [["mat-card"]], hostAttrs: [1, "mat-card", "mat-focus-indicator"], hostVars: 2, hostBindings: function MatCard_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵclassProp */.ekj("_mat-animation-noopable", ctx._animationMode === "NoopAnimations");
    } }, exportAs: ["matCard"], ngContentSelectors: card_c1, decls: 2, vars: 0, template: function MatCard_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(card_c0);
        core/* ɵɵprojection */.Hsn(0);
        core/* ɵɵprojection */.Hsn(1, 1);
    } }, styles: [".mat-card{transition:box-shadow 280ms cubic-bezier(0.4, 0, 0.2, 1);display:block;position:relative;padding:16px;border-radius:4px}._mat-animation-noopable.mat-card{transition:none;animation:none}.mat-card .mat-divider-horizontal{position:absolute;left:0;width:100%}[dir=rtl] .mat-card .mat-divider-horizontal{left:auto;right:0}.mat-card .mat-divider-horizontal.mat-divider-inset{position:static;margin:0}[dir=rtl] .mat-card .mat-divider-horizontal.mat-divider-inset{margin-right:0}.cdk-high-contrast-active .mat-card{outline:solid 1px}.mat-card-actions,.mat-card-subtitle,.mat-card-content{display:block;margin-bottom:16px}.mat-card-title{display:block;margin-bottom:8px}.mat-card-actions{margin-left:-8px;margin-right:-8px;padding:8px 0}.mat-card-actions-align-end{display:flex;justify-content:flex-end}.mat-card-image{width:calc(100% + 32px);margin:0 -16px 16px -16px}.mat-card-footer{display:block;margin:0 -16px -16px -16px}.mat-card-actions .mat-button,.mat-card-actions .mat-raised-button,.mat-card-actions .mat-stroked-button{margin:0 8px}.mat-card-header{display:flex;flex-direction:row}.mat-card-header .mat-card-title{margin-bottom:12px}.mat-card-header-text{margin:0 16px}.mat-card-avatar{height:40px;width:40px;border-radius:50%;flex-shrink:0;object-fit:cover}.mat-card-title-group{display:flex;justify-content:space-between}.mat-card-sm-image{width:80px;height:80px}.mat-card-md-image{width:112px;height:112px}.mat-card-lg-image{width:152px;height:152px}.mat-card-xl-image{width:240px;height:240px;margin:-8px}.mat-card-title-group>.mat-card-xl-image{margin:-8px 0 8px}@media(max-width: 599px){.mat-card-title-group{margin:0}.mat-card-xl-image{margin-left:0;margin-right:0}}.mat-card>:first-child,.mat-card-content>:first-child{margin-top:0}.mat-card>:last-child:not(.mat-card-footer),.mat-card-content>:last-child:not(.mat-card-footer){margin-bottom:0}.mat-card-image:first-child{margin-top:-16px;border-top-left-radius:inherit;border-top-right-radius:inherit}.mat-card>.mat-card-actions:last-child{margin-bottom:-8px;padding-bottom:0}.mat-card-actions:not(.mat-card-actions-align-end) .mat-button:first-child,.mat-card-actions:not(.mat-card-actions-align-end) .mat-raised-button:first-child,.mat-card-actions:not(.mat-card-actions-align-end) .mat-stroked-button:first-child{margin-left:0;margin-right:0}.mat-card-actions-align-end .mat-button:last-child,.mat-card-actions-align-end .mat-raised-button:last-child,.mat-card-actions-align-end .mat-stroked-button:last-child{margin-left:0;margin-right:0}.mat-card-title:not(:first-child),.mat-card-subtitle:not(:first-child){margin-top:-4px}.mat-card-header .mat-card-subtitle:not(:first-child){margin-top:-8px}.mat-card>.mat-card-xl-image:first-child{margin-top:-8px}.mat-card>.mat-card-xl-image:last-child{margin-bottom:-8px}\n"], encapsulation: 2, changeDetection: 0 });
MatCard.ctorParameters = () => [
    { type: String, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb,] }] }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCard, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-card',
                exportAs: 'matCard',
                template: "<ng-content></ng-content>\n<ng-content select=\"mat-card-footer\"></ng-content>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                host: {
                    'class': 'mat-card mat-focus-indicator',
                    '[class._mat-animation-noopable]': '_animationMode === "NoopAnimations"'
                },
                styles: [".mat-card{transition:box-shadow 280ms cubic-bezier(0.4, 0, 0.2, 1);display:block;position:relative;padding:16px;border-radius:4px}._mat-animation-noopable.mat-card{transition:none;animation:none}.mat-card .mat-divider-horizontal{position:absolute;left:0;width:100%}[dir=rtl] .mat-card .mat-divider-horizontal{left:auto;right:0}.mat-card .mat-divider-horizontal.mat-divider-inset{position:static;margin:0}[dir=rtl] .mat-card .mat-divider-horizontal.mat-divider-inset{margin-right:0}.cdk-high-contrast-active .mat-card{outline:solid 1px}.mat-card-actions,.mat-card-subtitle,.mat-card-content{display:block;margin-bottom:16px}.mat-card-title{display:block;margin-bottom:8px}.mat-card-actions{margin-left:-8px;margin-right:-8px;padding:8px 0}.mat-card-actions-align-end{display:flex;justify-content:flex-end}.mat-card-image{width:calc(100% + 32px);margin:0 -16px 16px -16px}.mat-card-footer{display:block;margin:0 -16px -16px -16px}.mat-card-actions .mat-button,.mat-card-actions .mat-raised-button,.mat-card-actions .mat-stroked-button{margin:0 8px}.mat-card-header{display:flex;flex-direction:row}.mat-card-header .mat-card-title{margin-bottom:12px}.mat-card-header-text{margin:0 16px}.mat-card-avatar{height:40px;width:40px;border-radius:50%;flex-shrink:0;object-fit:cover}.mat-card-title-group{display:flex;justify-content:space-between}.mat-card-sm-image{width:80px;height:80px}.mat-card-md-image{width:112px;height:112px}.mat-card-lg-image{width:152px;height:152px}.mat-card-xl-image{width:240px;height:240px;margin:-8px}.mat-card-title-group>.mat-card-xl-image{margin:-8px 0 8px}@media(max-width: 599px){.mat-card-title-group{margin:0}.mat-card-xl-image{margin-left:0;margin-right:0}}.mat-card>:first-child,.mat-card-content>:first-child{margin-top:0}.mat-card>:last-child:not(.mat-card-footer),.mat-card-content>:last-child:not(.mat-card-footer){margin-bottom:0}.mat-card-image:first-child{margin-top:-16px;border-top-left-radius:inherit;border-top-right-radius:inherit}.mat-card>.mat-card-actions:last-child{margin-bottom:-8px;padding-bottom:0}.mat-card-actions:not(.mat-card-actions-align-end) .mat-button:first-child,.mat-card-actions:not(.mat-card-actions-align-end) .mat-raised-button:first-child,.mat-card-actions:not(.mat-card-actions-align-end) .mat-stroked-button:first-child{margin-left:0;margin-right:0}.mat-card-actions-align-end .mat-button:last-child,.mat-card-actions-align-end .mat-raised-button:last-child,.mat-card-actions-align-end .mat-stroked-button:last-child{margin-left:0;margin-right:0}.mat-card-title:not(:first-child),.mat-card-subtitle:not(:first-child){margin-top:-4px}.mat-card-header .mat-card-subtitle:not(:first-child){margin-top:-8px}.mat-card>.mat-card-xl-image:first-child{margin-top:-8px}.mat-card>.mat-card-xl-image:last-child{margin-bottom:-8px}\n"]
            }]
    }], function () { return [{ type: String, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [fesm2015_animations/* ANIMATION_MODULE_TYPE */.Qb]
            }] }]; }, null); })();
/**
 * Component intended to be used within the `<mat-card>` component. It adds styles for a
 * preset header section (i.e. a title, subtitle, and avatar layout).
 * @docs-private
 */
class MatCardHeader {
}
MatCardHeader.ɵfac = function MatCardHeader_Factory(t) { return new (t || MatCardHeader)(); };
MatCardHeader.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatCardHeader, selectors: [["mat-card-header"]], hostAttrs: [1, "mat-card-header"], ngContentSelectors: card_c3, decls: 4, vars: 0, consts: [[1, "mat-card-header-text"]], template: function MatCardHeader_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(card_c2);
        core/* ɵɵprojection */.Hsn(0);
        core/* ɵɵelementStart */.TgZ(1, "div", 0);
        core/* ɵɵprojection */.Hsn(2, 1);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵprojection */.Hsn(3, 2);
    } }, encapsulation: 2, changeDetection: 0 });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardHeader, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-card-header',
                template: "<ng-content select=\"[mat-card-avatar], [matCardAvatar]\"></ng-content>\n<div class=\"mat-card-header-text\">\n  <ng-content\n      select=\"mat-card-title, mat-card-subtitle,\n      [mat-card-title], [mat-card-subtitle],\n      [matCardTitle], [matCardSubtitle]\"></ng-content>\n</div>\n<ng-content></ng-content>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                host: { 'class': 'mat-card-header' }
            }]
    }], null, null); })();
/**
 * Component intended to be used within the `<mat-card>` component. It adds styles for a preset
 * layout that groups an image with a title section.
 * @docs-private
 */
class MatCardTitleGroup {
}
MatCardTitleGroup.ɵfac = function MatCardTitleGroup_Factory(t) { return new (t || MatCardTitleGroup)(); };
MatCardTitleGroup.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatCardTitleGroup, selectors: [["mat-card-title-group"]], hostAttrs: [1, "mat-card-title-group"], ngContentSelectors: card_c5, decls: 4, vars: 0, template: function MatCardTitleGroup_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(card_c4);
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵprojection */.Hsn(1);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵprojection */.Hsn(2, 1);
        core/* ɵɵprojection */.Hsn(3, 2);
    } }, encapsulation: 2, changeDetection: 0 });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardTitleGroup, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-card-title-group',
                template: "<div>\n  <ng-content\n      select=\"mat-card-title, mat-card-subtitle,\n      [mat-card-title], [mat-card-subtitle],\n      [matCardTitle], [matCardSubtitle]\"></ng-content>\n</div>\n<ng-content select=\"img\"></ng-content>\n<ng-content></ng-content>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                host: { 'class': 'mat-card-title-group' }
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatCardModule {
}
MatCardModule.ɵfac = function MatCardModule_Factory(t) { return new (t || MatCardModule)(); };
MatCardModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatCardModule });
MatCardModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[fesm2015_core/* MatCommonModule */.BQ], fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCardModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [fesm2015_core/* MatCommonModule */.BQ],
                exports: [
                    MatCard,
                    MatCardHeader,
                    MatCardTitleGroup,
                    MatCardContent,
                    MatCardTitle,
                    MatCardSubtitle,
                    MatCardActions,
                    MatCardFooter,
                    MatCardSmImage,
                    MatCardMdImage,
                    MatCardLgImage,
                    MatCardImage,
                    MatCardXlImage,
                    MatCardAvatar,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                declarations: [
                    MatCard, MatCardHeader, MatCardTitleGroup, MatCardContent, MatCardTitle, MatCardSubtitle,
                    MatCardActions, MatCardFooter, MatCardSmImage, MatCardMdImage, MatCardLgImage, MatCardImage,
                    MatCardXlImage, MatCardAvatar,
                ]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatCardModule, { declarations: function () { return [MatCard, MatCardHeader, MatCardTitleGroup, MatCardContent, MatCardTitle, MatCardSubtitle, MatCardActions, MatCardFooter, MatCardSmImage, MatCardMdImage, MatCardLgImage, MatCardImage, MatCardXlImage, MatCardAvatar]; }, imports: function () { return [fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [MatCard, MatCardHeader, MatCardTitleGroup, MatCardContent, MatCardTitle, MatCardSubtitle, MatCardActions, MatCardFooter, MatCardSmImage, MatCardMdImage, MatCardLgImage, MatCardImage, MatCardXlImage, MatCardAvatar, fesm2015_core/* MatCommonModule */.BQ]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=card.js.map
// EXTERNAL MODULE: ./node_modules/ngx-countdown/fesm2015/ngx-countdown.js
var ngx_countdown = __webpack_require__(8591);
;// CONCATENATED MODULE: ./src/app/pages/exposure/exposure.component.ts


















function ExposureComponent_div_0_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 17);
    core/* ɵɵelementStart */.TgZ(2, "div", 18);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 19);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 20);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r6 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r6.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r6.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r6.comment);
} }
function ExposureComponent_div_0_div_20_div_1_Template(rf, ctx) { if (rf & 1) {
    const _r10 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 23);
    core/* ɵɵelementStart */.TgZ(1, "div", 24);
    core/* ɵɵelementStart */.TgZ(2, "mat-button-toggle", 25);
    core/* ɵɵlistener */.NdJ("change", function ExposureComponent_div_0_div_20_div_1_Template_mat_button_toggle_change_2_listener($event) { core/* ɵɵrestoreView */.CHM(_r10); const ctx_r9 = core/* ɵɵnextContext */.oxw(3); return ctx_r9.onValChange($event.value); });
    core/* ɵɵelementStart */.TgZ(3, "div", 26);
    core/* ɵɵelement */._UZ(4, "img", 27);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 28);
    core/* ɵɵelementStart */.TgZ(6, "div", 29);
    core/* ɵɵelementStart */.TgZ(7, "p", 30);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const driver_r8 = ctx.$implicit;
    const ctx_r7 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("value", driver_r8.code);
    core/* ɵɵproperty */.Q6J("checked", ctx_r7.myFlagForButtonToggle);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("alt", driver_r8.name);
    core/* ɵɵpropertyInterpolate */.s9C("src", "assets/img/drivers/" + ctx_r7.exposureResponse.year + "/" + driver_r8.code + ".png", core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate1 */.hij("", driver_r8.fullName, " ");
} }
function ExposureComponent_div_0_div_20_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 21);
    core/* ɵɵtemplate */.YNc(1, ExposureComponent_div_0_div_20_div_1_Template, 9, 5, "div", 22);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r3.exposureResponse.drivers);
} }
function ExposureComponent_div_0_div_21_Template(rf, ctx) { if (rf & 1) {
    const _r12 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 5);
    core/* ɵɵelementStart */.TgZ(1, "button", 31);
    core/* ɵɵlistener */.NdJ("click", function ExposureComponent_div_0_div_21_Template_button_click_1_listener() { core/* ɵɵrestoreView */.CHM(_r12); const ctx_r11 = core/* ɵɵnextContext */.oxw(2); return ctx_r11.expose(); });
    core/* ɵɵtext */._uU(2, " EXPOSE ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
const exposure_component_c0 = function (a0) { return { leftTime: a0 }; };
function ExposureComponent_div_0_div_22_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 32);
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 33);
    core/* ɵɵtext */._uU(3, " Exposure live in: ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "h1");
    core/* ɵɵelementStart */.TgZ(5, "span");
    core/* ɵɵelement */._UZ(6, "countdown", 34, 35);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(8, "br");
    core/* ɵɵelement */._UZ(9, "br");
    core/* ɵɵelement */._UZ(10, "br");
    core/* ɵɵelement */._UZ(11, "br");
    core/* ɵɵelement */._UZ(12, "br");
    core/* ɵɵelement */._UZ(13, "br");
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵelement */._UZ(15, "br");
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(6);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(1, exposure_component_c0, ctx_r5.exposureCountdown));
} }
function ExposureComponent_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r15 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 1);
    core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 2);
    core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 3, 4);
    core/* ɵɵlistener */.NdJ("openedChange", function ExposureComponent_div_0_Template_mat_drawer_openedChange_2_listener($event) { core/* ɵɵrestoreView */.CHM(_r15); const ctx_r14 = core/* ɵɵnextContext */.oxw(); return ctx_r14.utilityService.setSidebarStatus($event); });
    core/* ɵɵelementStart */.TgZ(4, "div", 5);
    core/* ɵɵelementStart */.TgZ(5, "div", 6);
    core/* ɵɵelementStart */.TgZ(6, "div");
    core/* ɵɵelementStart */.TgZ(7, "textarea", 7);
    core/* ɵɵlistener */.NdJ("ngModelChange", function ExposureComponent_div_0_Template_textarea_ngModelChange_7_listener($event) { core/* ɵɵrestoreView */.CHM(_r15); const ctx_r16 = core/* ɵɵnextContext */.oxw(); return ctx_r16.utilityService.postText = $event; });
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "div", 8);
    core/* ɵɵelementStart */.TgZ(9, "button", 9);
    core/* ɵɵlistener */.NdJ("click", function ExposureComponent_div_0_Template_button_click_9_listener() { core/* ɵɵrestoreView */.CHM(_r15); const ctx_r17 = core/* ɵɵnextContext */.oxw(); return ctx_r17.utilityService.postComment(2); });
    core/* ɵɵtext */._uU(10, "Post");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "button", 9);
    core/* ɵɵlistener */.NdJ("click", function ExposureComponent_div_0_Template_button_click_11_listener() { core/* ɵɵrestoreView */.CHM(_r15); const ctx_r18 = core/* ɵɵnextContext */.oxw(); return ctx_r18.utilityService.reloadPosts(2); });
    core/* ɵɵtext */._uU(12, "Reload");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(13, ExposureComponent_div_0_div_13_Template, 8, 3, "div", 10);
    core/* ɵɵelementStart */.TgZ(14, "div", 11);
    core/* ɵɵelementStart */.TgZ(15, "button", 12);
    core/* ɵɵlistener */.NdJ("click", function ExposureComponent_div_0_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r15); const _r1 = core/* ɵɵreference */.MAs(3); return _r1.toggle(); });
    core/* ɵɵelement */._UZ(16, "i", 13);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "div", 11);
    core/* ɵɵelementStart */.TgZ(18, "button", 12);
    core/* ɵɵlistener */.NdJ("click", function ExposureComponent_div_0_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r15); const _r1 = core/* ɵɵreference */.MAs(3); return _r1.toggle(); });
    core/* ɵɵelement */._UZ(19, "i", 13);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(20, ExposureComponent_div_0_div_20_Template, 2, 1, "div", 14);
    core/* ɵɵtemplate */.YNc(21, ExposureComponent_div_0_div_21_Template, 3, 0, "div", 15);
    core/* ɵɵtemplate */.YNc(22, ExposureComponent_div_0_div_22_Template, 16, 3, "div", 16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r0 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("opened", ctx_r0.utilityService.getSidebarStatus());
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r0.utilityService.postText);
    core/* ɵɵadvance */.xp6(6);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r0.utilityService.comments);
    core/* ɵɵadvance */.xp6(7);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r0.exposureResponse.status == "ACTIVE");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r0.exposureResponse.status == "ACTIVE");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r0.exposureResponse.status == "SOON");
} }
class ExposureComponent {
    constructor(restService, snackBar, router, utilityService) {
        //TODO delete me
        // localStorage.setItem('f1-vote-status', 'aa');
        this.restService = restService;
        this.snackBar = snackBar;
        this.router = router;
        this.utilityService = utilityService;
        this.exposedDrivers = [];
        this.exposureCountdown = 0;
        let observableDrivers = this.restService.getExposureDriverList();
        observableDrivers.subscribe({
            next: data => {
                this.exposureResponse = data;
                if (localStorage.getItem('f1-vote-status') == this.exposureResponse.title + this.exposureResponse.year) {
                    this.redirectToExposedResults();
                }
                this.utilityService.setTitle('Exposure - /' + this.exposureResponse.title + '/ edition');
                switch (this.exposureResponse.status) {
                    case 'OVER': {
                        this.redirectToExposedResults();
                        break;
                    }
                    case 'SOON': {
                        this.exposureCountdown = ~~((new Date(this.exposureResponse.exposureTime).getTime() - new Date().getTime()) / 1000);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return data;
            }
        });
        this.utilityService.reloadPosts(2);
    }
    expose() {
        this.observable = this.restService.exposeDrivers(this.exposedDrivers);
        this.observable.subscribe({
            next: data => {
                this.openSnackBar(data);
                localStorage.setItem('f1-vote-status', this.exposureResponse.title + this.exposureResponse.year);
                this.redirectToExposedResults();
                return data;
            }
        });
    }
    openSnackBar(success) {
        if (success) {
            localStorage.setItem('f1-vote-message', 'You\'ve exposed them');
        }
        else {
            localStorage.setItem('f1-vote-message', 'Rejected, you\'ve already voted');
        }
        this.snackBar.openFromComponent(SnackbarComponent, {
            duration: 4000,
        });
    }
    redirectToExposedResults() {
        this.router.navigate(['exposed']);
    }
    onValChange(driverCode) {
        if (!this.exposedDrivers.includes(driverCode)) {
            this.exposedDrivers.push(driverCode);
        }
        else {
            this.exposedDrivers = this.exposedDrivers.filter(item => item !== driverCode);
        }
    }
}
ExposureComponent.ɵfac = function ExposureComponent_Factory(t) { return new (t || ExposureComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(snack_bar/* MatSnackBar */.ux), core/* ɵɵdirectiveInject */.Y36(router/* Router */.F0), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
ExposureComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: ExposureComponent, selectors: [["exposure-cmp"]], decls: 1, vars: 1, consts: [["class", "div-padded", 4, "ngIf"], [1, "div-padded"], ["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], ["class", "row div-padded-1rem", 4, "ngIf"], ["class", "row", 4, "ngIf"], ["class", "div-height-100a", 4, "ngIf"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [1, "row", "div-padded-1rem"], ["class", "col-lg-2 col-md-2 col-6", 4, "ngFor", "ngForOf"], [1, "col-lg-2", "col-md-2", "col-6"], [1, "card"], [3, "checked", "value", "change"], [1, "card-body"], ["mat-card-image", "", 3, "alt", "src"], [1, "card-footer"], [1, "stats"], [1, "card-title"], ["color", "primary", "mat-raised-button", "", 3, "click"], [1, "div-height-100a"], [1, "countdown-desc"], [3, "config"], ["cd", ""]], template: function ExposureComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵtemplate */.YNc(0, ExposureComponent_div_0_Template, 23, 6, "div", 0);
    } if (rf & 2) {
        core/* ɵɵproperty */.Q6J("ngIf", ctx.exposureResponse !== undefined);
    } }, directives: [common/* NgIf */.O5, sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, MatButtonToggle, MatCardImage, ngx_countdown/* CountdownComponent */.MR], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/utility/chart-utility.ts
class ChartUtility {
}
ChartUtility.defaultToolbar = {
    export: {
        csv: {
            filename: 'F1Exposure.com_' + new Date().toISOString(),
            columnDelimiter: ',',
            headerCategory: 'category',
            headerValue: 'value',
            dateFormatter(timestamp) {
                return new Date(timestamp).toDateString();
            }
        },
        svg: {
            filename: 'F1Exposure.com_' + new Date().toISOString(),
        },
        png: {
            filename: 'F1Exposure.com_' + new Date().toISOString(),
        }
    }
};

// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/tabs.js
var tabs = __webpack_require__(5939);
// EXTERNAL MODULE: ./node_modules/ng-apexcharts/__ivy_ngcc__/fesm2015/ng-apexcharts.js
var ng_apexcharts = __webpack_require__(4256);
;// CONCATENATED MODULE: ./src/app/pages/exposureResults/exposed.component.ts














const exposed_component_c0 = ["chart"];
function ExposedComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 18);
    core/* ɵɵelementStart */.TgZ(2, "div", 19);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 20);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 21);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r5 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.comment);
} }
function ExposedComponent_ng_template_23_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "apx-chart", 22);
    core/* ɵɵelement */._UZ(1, "apx-chart", 23);
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("chart", ctx_r2.defaultChartOptions.chart)("dataLabels", ctx_r2.defaultChartOptions.dataLabels)("plotOptions", ctx_r2.defaultChartOptions.plotOptions)("series", ctx_r2.exposedChartOptions.series)("xaxis", ctx_r2.exposedChartOptions.xaxis)("title", ctx_r2.exposedChartOptions.title)("yaxis", ctx_r2.defaultChartOptions.yaxis);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("chart", ctx_r2.defaultChartOptions.chart)("dataLabels", ctx_r2.defaultChartOptions.dataLabels)("plotOptions", ctx_r2.defaultChartOptions.plotOptions)("series", ctx_r2.votesChartOptions.series)("xaxis", ctx_r2.votesChartOptions.xaxis)("title", ctx_r2.votesChartOptions.title);
} }
function ExposedComponent_ng_template_25_div_1_tr_15_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 31);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 32);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td", 33);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵpipe */.ALo(9, "number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r11 = ctx.$implicit;
    const i_r12 = ctx.index;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(i_r12 + 1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r11.fullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r11.id.driver);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(core/* ɵɵpipeBind1 */.lcZ(9, 4, standing_r11.exposure));
} }
function ExposedComponent_ng_template_25_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 26);
    core/* ɵɵelementStart */.TgZ(1, "table", 27);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 28);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr", 29);
    core/* ɵɵelement */._UZ(7, "th", 30);
    core/* ɵɵelementStart */.TgZ(8, "th", 30);
    core/* ɵɵtext */._uU(9, "Driver");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "th", 30);
    core/* ɵɵtext */._uU(11, "Code");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 30);
    core/* ɵɵtext */._uU(13, "Exposure");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "tbody");
    core/* ɵɵtemplate */.YNc(15, ExposedComponent_ng_template_25_div_1_tr_15_Template, 10, 6, "tr", 9);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r6.exposureData.activeExposureChart.season, " Exposure Standings");
    core/* ɵɵadvance */.xp6(10);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r6.exposureData.standings);
} }
function ExposedComponent_ng_template_25_div_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 34);
    core/* ɵɵelement */._UZ(1, "apx-chart", 35);
    core/* ɵɵelement */._UZ(2, "apx-chart", 35);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r7 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r7.exposureStandingsOptions.series)("xaxis", ctx_r7.numericXaxis)("yaxis", ctx_r7.defYaxis)("title", ctx_r7.exposureStandingsOptions.title)("chart", ctx_r7.lineChart)("stroke", ctx_r7.defStroke)("tooltip", ctx_r7.defTooltip);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r7.exposureRoundByRoundOptions.series)("xaxis", ctx_r7.numericXaxis)("yaxis", ctx_r7.defYaxis)("title", ctx_r7.exposureRoundByRoundOptions.title)("chart", ctx_r7.lineChart)("stroke", ctx_r7.defStroke)("tooltip", ctx_r7.defTooltip);
} }
function ExposedComponent_ng_template_25_div_4_tr_14_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "td");
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td", 32);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(5, "td");
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const race_r14 = ctx.$implicit;
    const ctx_r13 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(race_r14.round);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(race_r14.raceName);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵstyleProp */.Udp("background-color", ctx_r13.getColor(race_r14.round));
} }
function ExposedComponent_ng_template_25_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 26);
    core/* ɵɵelementStart */.TgZ(1, "table", 27);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 28);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr", 29);
    core/* ɵɵelementStart */.TgZ(7, "th", 30);
    core/* ɵɵtext */._uU(8, "Round");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 30);
    core/* ɵɵtext */._uU(10, "Race");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 30);
    core/* ɵɵtext */._uU(12, "Color");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "tbody");
    core/* ɵɵtemplate */.YNc(14, ExposedComponent_ng_template_25_div_4_tr_14_Template, 6, 4, "tr", 9);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r8 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r8.exposureData.activeExposureChart.season, " races");
    core/* ɵɵadvance */.xp6(9);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r8.exposureData.exposureRaces);
} }
function ExposedComponent_ng_template_25_div_5_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 34);
    core/* ɵɵelement */._UZ(1, "apx-chart", 36);
    core/* ɵɵelement */._UZ(2, "apx-chart", 37);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r9 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("chart", ctx_r9.defaultChartOptions.chart)("dataLabels", ctx_r9.defaultChartOptions.dataLabels)("plotOptions", ctx_r9.distributedPlotOptions)("series", ctx_r9.maxExposureOptions.series)("title", ctx_r9.maxExposureOptions.title)("xaxis", ctx_r9.maxExposureOptions.xaxis)("yaxis", ctx_r9.maxExposureOptions.yaxis)("colors", ctx_r9.maxExposureColors)("legend", ctx_r9.noLegend);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r9.votersOptions.series)("xaxis", ctx_r9.numericXaxis)("yaxis", ctx_r9.defYaxis)("title", ctx_r9.votersOptions.title)("chart", ctx_r9.lineChart)("stroke", ctx_r9.defStroke)("legend", ctx_r9.raceLegend);
} }
function ExposedComponent_ng_template_25_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 4);
    core/* ɵɵtemplate */.YNc(1, ExposedComponent_ng_template_25_div_1_Template, 16, 2, "div", 24);
    core/* ɵɵtemplate */.YNc(2, ExposedComponent_ng_template_25_div_2_Template, 3, 14, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 4);
    core/* ɵɵtemplate */.YNc(4, ExposedComponent_ng_template_25_div_4_Template, 15, 2, "div", 24);
    core/* ɵɵtemplate */.YNc(5, ExposedComponent_ng_template_25_div_5_Template, 3, 16, "div", 25);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.exposureData !== undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.exposureData !== undefined);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.exposureData !== undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.exposureData !== undefined);
} }
function ExposedComponent_ng_template_27_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 38);
    core/* ɵɵelementStart */.TgZ(1, "p");
    core/* ɵɵtext */._uU(2, " The exposure calculation method is the same as the one used by the real exposure hosted on strawpoll by ");
    core/* ɵɵelementStart */.TgZ(3, "a", 39);
    core/* ɵɵtext */._uU(4, "Vitaly Petrov");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 40);
    core/* ɵɵtext */._uU(6, " formula is ");
    core/* ɵɵelementStart */.TgZ(7, "p");
    core/* ɵɵtext */._uU(8, "exposure = votes/total voters");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "p");
    core/* ɵɵtext */._uU(10, " Example: There are only 2 people voting. One votes HAM and VER, the other votes HAM, BOT and GAS. That's total of 2 voters, and 5 votes. HAM's exposure is 100% ( 2/2, all voters voted for him), VER/BOT/GAS exposure is 50% ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "p");
    core/* ɵɵelementStart */.TgZ(12, "span", 41);
    core/* ɵɵtext */._uU(13, ">When does the poll close?");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵtext */._uU(15, "During 2021 testing - Tuesday evening. ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function sortByMaxExpoDesc(a, b) {
    if (a.maxExposure > b.maxExposure) {
        return -1;
    }
    if (a.maxExposure < b.maxExposure) {
        return 1;
    }
    return 0;
}
class ExposedComponent {
    constructor(restService, utilityService) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.maxExposureColors = [];
        this.defaultChartOptions = {
            chart: {
                type: 'bar',
                height: 500,
                toolbar: ChartUtility.defaultToolbar
            },
            plotOptions: {
                bar: {
                    horizontal: true,
                }
            },
            dataLabels: {
                enabled: true
            },
            yaxis: {
                min: 0,
                max: 100,
                tickAmount: 20,
                axisTicks: {
                    show: true
                },
                axisBorder: {
                    show: true,
                    color: "#008FFB"
                }
            }
        };
        this.lineChart = {
            height: 500,
            type: "line",
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.defStroke = {
            width: 3
        };
        this.defTooltip = {
            shared: false,
        };
        this.numericXaxis = {
            type: 'numeric',
            decimalsInFloat: 0,
            min: 1,
            max: 23,
            tickAmount: 11
        };
        this.defYaxis = {
            decimalsInFloat: 0
        };
        this.raceLegend = {
            position: 'top',
            horizontalAlign: 'left',
            floating: false,
            show: true
        };
        this.noLegend = {
            show: false
        };
        this.distributedPlotOptions = {
            bar: {
                horizontal: true,
                distributed: true
            }
        };
        this.observable = restService.getExposureResults();
        this.observable.subscribe({
            next: data => {
                this.exposureData = data;
                this.utilityService.setTitle('Exposure - /' + this.exposureData.title + '/ edition');
                this.exposedChart = this.exposureData.activeExposureChart;
                this.exposureChampionshipDataSortedByMaxExpo = this.exposureData.exposureChampionshipData.sort(sortByMaxExpoDesc);
                this.round = this.exposureData.activeExposureChart.round;
                if (this.round > 0) {
                    this.numericXaxis.max = this.round;
                    this.numericXaxis.tickAmount = (this.round + 1) / 2;
                }
                this.votesChartOptions = {
                    series: [
                        {
                            name: 'Votes',
                            data: this.exposedChart.results
                        }
                    ],
                    xaxis: {
                        categories: this.exposedChart.driverNames
                    },
                    title: {
                        text: 'Total votes',
                        align: 'center'
                    }
                };
                this.exposedChartOptions = {
                    series: [
                        {
                            name: 'Exposure',
                            data: this.exposedChart.exposure
                        }
                    ],
                    xaxis: {
                        categories: this.exposedChart.driverNames
                    },
                    title: {
                        text: 'Exposure (%)',
                        align: 'center'
                    }
                };
                this.setExposureChampionshipData();
                return data;
            }
        });
        this.utilityService.reloadPosts(3);
    }
    setExposureChampionshipData() {
        let standingSeries = [];
        let roundByRound = [];
        let maxExposureSeries = [];
        let driverNamesSortedByMaxExpo = [];
        this.exposureData.exposureChampionshipData.forEach((driverExposure) => {
            standingSeries.push({
                name: driverExposure.code,
                data: driverExposure.scoresThroughRounds,
                color: driverExposure.color
            });
            roundByRound.push({
                name: driverExposure.code,
                data: driverExposure.scoresByRound,
                color: driverExposure.color
            });
        });
        this.exposureChampionshipDataSortedByMaxExpo.forEach((driverExposure) => {
            maxExposureSeries.push(driverExposure.maxExposure);
            driverNamesSortedByMaxExpo.push(driverExposure.code);
            this.maxExposureColors.push(this.getColor(driverExposure.maxExposureRound));
        });
        this.exposureStandingsOptions = {
            series: standingSeries,
            title: {
                text: 'Exposure standing through season',
                align: 'center'
            },
        };
        this.votersOptions = {
            series: [
                {
                    name: 'Voters',
                    data: this.exposureData.voters
                }
            ],
            title: {
                text: 'Voters through season',
                align: 'center'
            },
        };
        this.maxExposureOptions = {
            series: [
                {
                    name: 'Max Exposure',
                    data: maxExposureSeries
                }
            ],
            xaxis: {
                categories: driverNamesSortedByMaxExpo
            },
            title: {
                text: 'Highest exposure by driver',
                align: 'center'
            }
        };
        this.exposureRoundByRoundOptions = {
            series: roundByRound,
            title: {
                text: 'Round by round',
                align: 'center'
            },
        };
    }
    getColor(number) {
        return utility_service/* UtilityService.colorMap.get */.t.colorMap.get(number);
    }
    postCommentMethod(page) {
        this.utilityService.postComment(page);
    }
}
ExposedComponent.ɵfac = function ExposedComponent_Factory(t) { return new (t || ExposedComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
ExposedComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: ExposedComponent, selectors: [["exposed-cmp"]], viewQuery: function ExposedComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(exposed_component_c0, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.chart = _t.first);
    } }, decls: 28, vars: 3, consts: [[1, "div-padded"], ["autosize", ""], ["mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], ["id", "chart1", 1, "div-padded-right", "bg-white", "height-fill-screen"], ["label", "Latest race"], ["matTabContent", ""], ["label", "World Exposure Championship"], ["label", "Exposure calculation"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [3, "chart", "dataLabels", "plotOptions", "series", "xaxis", "title", "yaxis"], [3, "chart", "dataLabels", "plotOptions", "series", "xaxis", "title"], ["class", "col-lg-4 col-md-6 col-xs-6", 4, "ngIf"], ["class", "col-lg-8 col-md-6 col-xs-6", 4, "ngIf"], [1, "col-lg-4", "col-md-6", "col-xs-6"], [1, "table", "table-striped"], ["colspan", "4", 1, "text-center", "bg-primary", "text-white"], [1, "text-center", "bg-info", "text-white"], ["scope", "col"], ["scope", "row"], [1, "text-center"], [1, "text-right"], [1, "col-lg-8", "col-md-6", "col-xs-6"], [3, "series", "xaxis", "yaxis", "title", "chart", "stroke", "tooltip"], [3, "chart", "dataLabels", "plotOptions", "series", "title", "xaxis", "yaxis", "colors", "legend"], [3, "series", "xaxis", "yaxis", "title", "chart", "stroke", "legend"], [1, "card", "div-padded-2rem", "text-justify"], ["href", "https://strawpoll.com/user/vitaly-petrov"], [1, "text-justify"], [1, "implying"]], template: function ExposedComponent_Template(rf, ctx) { if (rf & 1) {
        const _r16 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 1);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 2, 3);
        core/* ɵɵlistener */.NdJ("openedChange", function ExposedComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 4);
        core/* ɵɵelementStart */.TgZ(5, "div", 5);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 6);
        core/* ɵɵlistener */.NdJ("ngModelChange", function ExposedComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 7);
        core/* ɵɵelementStart */.TgZ(9, "button", 8);
        core/* ɵɵlistener */.NdJ("click", function ExposedComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(3); });
        core/* ɵɵtext */._uU(10, "Post ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 8);
        core/* ɵɵlistener */.NdJ("click", function ExposedComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(3); });
        core/* ɵɵtext */._uU(12, " Reload ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, ExposedComponent_div_13_Template, 8, 3, "div", 9);
        core/* ɵɵelementStart */.TgZ(14, "div", 10);
        core/* ɵɵelementStart */.TgZ(15, "button", 11);
        core/* ɵɵlistener */.NdJ("click", function ExposedComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r16); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 12);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 10);
        core/* ɵɵelementStart */.TgZ(18, "button", 11);
        core/* ɵɵlistener */.NdJ("click", function ExposedComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r16); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 12);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 13);
        core/* ɵɵelementStart */.TgZ(21, "mat-tab-group");
        core/* ɵɵelementStart */.TgZ(22, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(23, ExposedComponent_ng_template_23_Template, 2, 13, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "mat-tab", 16);
        core/* ɵɵtemplate */.YNc(25, ExposedComponent_ng_template_25_Template, 6, 4, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(26, "mat-tab", 17);
        core/* ɵɵtemplate */.YNc(27, ExposedComponent_ng_template_27_Template, 16, 0, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, ng_apexcharts/* ChartComponent */.x, common/* NgIf */.O5], pipes: [common/* DecimalPipe */.JJ], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/model/calendar-data.ts
class CalendarData {
}
class F1Calendar {
}
class CountdownData {
}

;// CONCATENATED MODULE: ./src/app/pages/countdown/f1-countdown.component.ts
















const f1_countdown_component_c0 = ["countdown"];
function F1CountdownComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 16);
    core/* ɵɵelementStart */.TgZ(2, "div", 17);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 18);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 19);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r7 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.comment);
} }
function F1CountdownComponent_ng_template_23_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r8 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r8.FP1Days, " days");
} }
const f1_countdown_component_c1 = function (a0) { return { leftTime: a0 }; };
function F1CountdownComponent_ng_template_23_Template(rf, ctx) { if (rf & 1) {
    const _r11 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 20);
    core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_ng_template_23_Template_div_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r11); const ctx_r10 = core/* ɵɵnextContext */.oxw(); return ctx_r10.countdownWasClicked(1); });
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(4, F1CountdownComponent_ng_template_23_div_4_Template, 4, 1, "div", 22);
    core/* ɵɵelementStart */.TgZ(5, "h1");
    core/* ɵɵelementStart */.TgZ(6, "span");
    core/* ɵɵelement */._UZ(7, "countdown", 23, 24);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "br");
    core/* ɵɵelement */._UZ(10, "br");
    core/* ɵɵelement */._UZ(11, "br");
    core/* ɵɵelement */._UZ(12, "br");
    core/* ɵɵelement */._UZ(13, "br");
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵelement */._UZ(15, "br");
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij(" ", ctx_r2.calendarData.f1Calendar.practice1Name, " in: ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r2.FP1Days > 0);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(3, f1_countdown_component_c1, ctx_r2.FP1Seconds));
} }
function F1CountdownComponent_ng_template_25_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r12 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r12.FP2Days, " days");
} }
function F1CountdownComponent_ng_template_25_Template(rf, ctx) { if (rf & 1) {
    const _r15 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 20);
    core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_ng_template_25_Template_div_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r15); const ctx_r14 = core/* ɵɵnextContext */.oxw(); return ctx_r14.countdownWasClicked(2); });
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(4, F1CountdownComponent_ng_template_25_div_4_Template, 4, 1, "div", 22);
    core/* ɵɵelementStart */.TgZ(5, "h1");
    core/* ɵɵelementStart */.TgZ(6, "span");
    core/* ɵɵelement */._UZ(7, "countdown", 23, 24);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "br");
    core/* ɵɵelement */._UZ(10, "br");
    core/* ɵɵelement */._UZ(11, "br");
    core/* ɵɵelement */._UZ(12, "br");
    core/* ɵɵelement */._UZ(13, "br");
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵelement */._UZ(15, "br");
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij(" ", ctx_r3.calendarData.f1Calendar.practice2Name, " in: ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.FP2Days > 0);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(3, f1_countdown_component_c1, ctx_r3.FP2Seconds));
} }
function F1CountdownComponent_ng_template_27_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r16 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r16.FP3Days, " days");
} }
function F1CountdownComponent_ng_template_27_Template(rf, ctx) { if (rf & 1) {
    const _r19 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 20);
    core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_ng_template_27_Template_div_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r19); const ctx_r18 = core/* ɵɵnextContext */.oxw(); return ctx_r18.countdownWasClicked(3); });
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(4, F1CountdownComponent_ng_template_27_div_4_Template, 4, 1, "div", 22);
    core/* ɵɵelementStart */.TgZ(5, "h1");
    core/* ɵɵelementStart */.TgZ(6, "span");
    core/* ɵɵelement */._UZ(7, "countdown", 23, 24);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "br");
    core/* ɵɵelement */._UZ(10, "br");
    core/* ɵɵelement */._UZ(11, "br");
    core/* ɵɵelement */._UZ(12, "br");
    core/* ɵɵelement */._UZ(13, "br");
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵelement */._UZ(15, "br");
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij(" ", ctx_r4.calendarData.f1Calendar.practice3Name, " in: ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r4.FP3Days > 0);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(3, f1_countdown_component_c1, ctx_r4.FP3Seconds));
} }
function F1CountdownComponent_ng_template_29_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r20 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r20.qualifyingDays, " days");
} }
function F1CountdownComponent_ng_template_29_Template(rf, ctx) { if (rf & 1) {
    const _r23 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 20);
    core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_ng_template_29_Template_div_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r23); const ctx_r22 = core/* ɵɵnextContext */.oxw(); return ctx_r22.countdownWasClicked(4); });
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(4, F1CountdownComponent_ng_template_29_div_4_Template, 4, 1, "div", 22);
    core/* ɵɵelementStart */.TgZ(5, "h1");
    core/* ɵɵelementStart */.TgZ(6, "span");
    core/* ɵɵelement */._UZ(7, "countdown", 23, 24);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "br");
    core/* ɵɵelement */._UZ(10, "br");
    core/* ɵɵelement */._UZ(11, "br");
    core/* ɵɵelement */._UZ(12, "br");
    core/* ɵɵelement */._UZ(13, "br");
    core/* ɵɵelement */._UZ(14, "br");
    core/* ɵɵelement */._UZ(15, "br");
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij(" ", ctx_r5.calendarData.f1Calendar.qualifyingName, " in: ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r5.qualifyingDays > 0);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(3, f1_countdown_component_c1, ctx_r5.qualifyingSeconds));
} }
function F1CountdownComponent_ng_template_31_div_0_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r25 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij("", ctx_r25.raceDays, " days");
} }
function F1CountdownComponent_ng_template_31_div_0_div_5_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 25);
    core/* ɵɵtext */._uU(3, "1 day");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function F1CountdownComponent_ng_template_31_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r29 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 20);
    core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_ng_template_31_div_0_Template_div_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r29); const ctx_r28 = core/* ɵɵnextContext */.oxw(2); return ctx_r28.countdownWasClicked(5); });
    core/* ɵɵelementStart */.TgZ(1, "h1");
    core/* ɵɵelementStart */.TgZ(2, "span", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(4, F1CountdownComponent_ng_template_31_div_0_div_4_Template, 4, 1, "div", 22);
    core/* ɵɵtemplate */.YNc(5, F1CountdownComponent_ng_template_31_div_0_div_5_Template, 4, 0, "div", 22);
    core/* ɵɵelementStart */.TgZ(6, "h1");
    core/* ɵɵelementStart */.TgZ(7, "span");
    core/* ɵɵelementStart */.TgZ(8, "countdown", 27, 24);
    core/* ɵɵlistener */.NdJ("event", function F1CountdownComponent_ng_template_31_div_0_Template_countdown_event_8_listener($event) { core/* ɵɵrestoreView */.CHM(_r29); const ctx_r30 = core/* ɵɵnextContext */.oxw(2); return ctx_r30.itsRaceTime($event); });
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r24 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate1 */.hij(" ", ctx_r24.calendarData.f1Calendar.raceName, " in: ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r24.raceDays > 1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r24.raceDays == 1);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("config", core/* ɵɵpureFunction1 */.VKq(4, f1_countdown_component_c1, ctx_r24.raceSeconds));
} }
function F1CountdownComponent_ng_template_31_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, F1CountdownComponent_ng_template_31_div_0_Template, 10, 6, "div", 26);
    core/* ɵɵelement */._UZ(1, "br");
    core/* ɵɵelement */._UZ(2, "br");
    core/* ɵɵelement */._UZ(3, "br");
    core/* ɵɵelement */._UZ(4, "br");
    core/* ɵɵelement */._UZ(5, "br");
    core/* ɵɵelement */._UZ(6, "br");
    core/* ɵɵelement */._UZ(7, "br");
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r6.calendarData != null);
} }
class F1CountdownComponent {
    constructor(restService, utilityService, toastr) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.toastr = toastr;
        this.raceDays = 0;
        this.raceSeconds = 0;
        this.qualifyingDays = 0;
        this.qualifyingSeconds = 0;
        this.FP3Days = 0;
        this.FP3Seconds = 0;
        this.FP2Days = 0;
        this.FP2Seconds = 0;
        this.FP1Days = 0;
        this.FP1Seconds = 0;
        this.getCountdownData(0);
        this.utilityService.reloadPosts(1);
    }
    getCountdownData(mode) {
        this.observable = this.restService.getCountdownData(mode);
        console.log();
        this.observable.subscribe({
            next: data => {
                this.calendarData = data;
                console.log(this.calendarData);
                if (mode == 0 || mode == 1) {
                    this.FP1Days = this.calendarData.countdownData.FP1Days;
                    this.FP1Seconds = this.calendarData.countdownData.FP1Seconds;
                }
                if (mode == 0 || mode == 2) {
                    this.FP2Days = this.calendarData.countdownData.FP2Days;
                    this.FP2Seconds = this.calendarData.countdownData.FP2Seconds;
                }
                if (mode == 0 || mode == 3) {
                    this.FP3Days = this.calendarData.countdownData.FP3Days;
                    this.FP3Seconds = this.calendarData.countdownData.FP3Seconds;
                }
                if (mode == 0 || mode == 4) {
                    this.qualifyingDays = this.calendarData.countdownData.qualifyingDays;
                    this.qualifyingSeconds = this.calendarData.countdownData.qualifyingSeconds;
                }
                if (mode == 0 || mode == 5) {
                    this.raceDays = this.calendarData.countdownData.raceDays;
                    this.raceSeconds = this.calendarData.countdownData.raceSeconds;
                }
                if (mode == 0) {
                    this.utilityService.setTitleDefaultPage(this.calendarData.f1Calendar.summary);
                }
                return data;
            },
            'error': error => {
                console.error('There was an error!', error);
                this.calendarData = new CalendarData();
                this.calendarData.countdownData.raceDays = 0;
                this.calendarData.countdownData.raceSeconds = 0;
            }
        });
    }
    countdownWasClicked(tab) {
        let val;
        switch (tab) {
            case 1:
                val = 'Practice 1 in: ' + this.getTimeFromSeconds(this.calendarData.countdownData.FP1Seconds);
                break;
            case 2:
                val = 'Practice 2 in: ' + this.getTimeFromSeconds(this.calendarData.countdownData.FP2Seconds);
                break;
            case 3:
                val = 'Practice 3 in: ' + this.getTimeFromSeconds(this.calendarData.countdownData.FP3Seconds);
                break;
            case 4:
                val = 'Qualifying in: ' + this.getTimeFromSeconds(this.calendarData.countdownData.qualifyingSeconds);
                break;
            case 5:
                val = 'Race in: ' + this.getTimeFromSeconds(this.calendarData.countdownData.raceSeconds);
                break;
            default:
                break;
        }
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = val;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy');
        document.body.removeChild(selBox);
        this.toastr.info('Countdown copied to clipboard', '', {
            timeOut: 4000,
            closeButton: false,
            enableHtml: false,
            toastClass: 'alert alert-info',
            positionClass: 'toast-top-center'
        });
    }
    getTimeFromSeconds(sec_num) {
        let days = Math.floor(sec_num / 86400);
        let hours = Math.floor(sec_num / 3600) % 24;
        let minutes = Math.floor(sec_num / 60) % 60;
        let seconds = sec_num % 60;
        let response = '';
        if (days > 0) {
            response = days + ' days, ';
        }
        response += hours + ' hours, ' + minutes + ' minutes, ' + seconds + ' seconds ';
        return response;
    }
    tabChanged(tabChangeEvent) {
        this.getCountdownData(tabChangeEvent.index + 1);
    }
    itsRaceTime(cevent) {
        if (cevent.action == 'done' && this.raceSeconds > 0) {
            var iframe = document.createElement('iframe');
            iframe.style.display = "none";
            iframe.src = 'https://www.youtube.com/embed/jKlL6wvGrbc?autoplay=1';
            iframe.allow = 'autoplay; encrypted-media';
            document.body.appendChild(iframe);
        }
    }
}
F1CountdownComponent.ɵfac = function F1CountdownComponent_Factory(t) { return new (t || F1CountdownComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(ngx_toastr/* ToastrService */._W)); };
F1CountdownComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: F1CountdownComponent, selectors: [["f1-countdown-cmp"]], viewQuery: function F1CountdownComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(f1_countdown_component_c0, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.countdown = _t.first);
    } }, decls: 32, vars: 9, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], [1, "bg-white", "height-fill-screen"], [3, "selectedIndex", "selectedTabChange"], [3, "label"], ["matTabContent", ""], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [3, "click"], [1, "countdown-desc"], [4, "ngIf"], [3, "config"], ["cd", ""], [1, "count-down"], [3, "click", 4, "ngIf"], [3, "config", "event"]], template: function F1CountdownComponent_Template(rf, ctx) { if (rf & 1) {
        const _r31 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function F1CountdownComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function F1CountdownComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(1); });
        core/* ɵɵtext */._uU(10, "Post ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(1); });
        core/* ɵɵtext */._uU(12, "Reload ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, F1CountdownComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r31); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function F1CountdownComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r31); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 12);
        core/* ɵɵelementStart */.TgZ(21, "mat-tab-group", 13);
        core/* ɵɵlistener */.NdJ("selectedTabChange", function F1CountdownComponent_Template_mat_tab_group_selectedTabChange_21_listener($event) { return ctx.tabChanged($event); });
        core/* ɵɵelementStart */.TgZ(22, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(23, F1CountdownComponent_ng_template_23_Template, 16, 5, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(25, F1CountdownComponent_ng_template_25_Template, 16, 5, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(26, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(27, F1CountdownComponent_ng_template_27_Template, 16, 5, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(28, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(29, F1CountdownComponent_ng_template_29_Template, 16, 5, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(30, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(31, F1CountdownComponent_ng_template_31_Template, 8, 1, "ng-template", 15);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
        core/* ɵɵadvance */.xp6(8);
        core/* ɵɵproperty */.Q6J("selectedIndex", 4);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵpropertyInterpolate */.s9C("label", ctx.calendarData.f1Calendar.practice1Name);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("label", ctx.calendarData.f1Calendar.practice2Name);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("label", ctx.calendarData.f1Calendar.practice3Name);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("label", ctx.calendarData.f1Calendar.qualifyingName);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("label", ctx.calendarData.f1Calendar.raceName);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, common/* NgIf */.O5, ngx_countdown/* CountdownComponent */.MR], styles: [""] });

// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/table.js + 1 modules
var table = __webpack_require__(2789);
;// CONCATENATED MODULE: ./src/app/pages/standings/standings.component.ts














function StandingsComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 16);
    core/* ɵɵelementStart */.TgZ(2, "div", 17);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 18);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 19);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r5 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r5.comment);
} }
function StandingsComponent_ng_template_22_div_0_tr_16_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 26);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td", 27);
    core/* ɵɵelementStart */.TgZ(4, "a", 28);
    core/* ɵɵelementStart */.TgZ(5, "div");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td", 29);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td", 29);
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 30);
    core/* ɵɵelementStart */.TgZ(12, "a", 28);
    core/* ɵɵelementStart */.TgZ(13, "div");
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td");
    core/* ɵɵtext */._uU(16);
    core/* ɵɵpipe */.ALo(17, "number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r9 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r9.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", standing_r9.driverUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", standing_r9.name, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r9.code);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r9.nationality);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", standing_r9.constructorUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", standing_r9.car, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(core/* ɵɵpipeBind1 */.lcZ(17, 8, standing_r9.points));
} }
function StandingsComponent_ng_template_22_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 22);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr", 23);
    core/* ɵɵelement */._UZ(4, "th", 24);
    core/* ɵɵelementStart */.TgZ(5, "th", 24);
    core/* ɵɵtext */._uU(6, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "th", 25);
    core/* ɵɵtext */._uU(8, "Code");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 25);
    core/* ɵɵtext */._uU(10, "Nationality");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 25);
    core/* ɵɵtext */._uU(12, "Car");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 24);
    core/* ɵɵtext */._uU(14, "Points");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "tbody");
    core/* ɵɵtemplate */.YNc(16, StandingsComponent_ng_template_22_div_0_tr_16_Template, 18, 10, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(16);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r6.standings.driverStandings);
} }
function StandingsComponent_ng_template_22_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 31);
    core/* ɵɵelement */._UZ(1, "apx-chart", 32);
    core/* ɵɵelement */._UZ(2, "apx-chart", 32);
    core/* ɵɵelement */._UZ(3, "apx-chart", 32);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r7 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r7.driverStandingsSeries)("yaxis", ctx_r7.defYaxisWithDecimal)("title", ctx_r7.titleStandings)("chart", ctx_r7.lineChart)("stroke", ctx_r7.defStroke)("tooltip", ctx_r7.defTooltip);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r7.driverResultByRoundSeries)("yaxis", ctx_r7.reverseYAxis)("title", ctx_r7.titleResults)("chart", ctx_r7.lineChart)("stroke", ctx_r7.defStroke)("tooltip", ctx_r7.defTooltip);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r7.driverPointsByRoundSeries)("yaxis", ctx_r7.defYaxis)("title", ctx_r7.titleByRound)("chart", ctx_r7.lineChart)("stroke", ctx_r7.defStroke)("tooltip", ctx_r7.defTooltip);
} }
function StandingsComponent_ng_template_22_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, StandingsComponent_ng_template_22_div_0_Template, 17, 1, "div", 20);
    core/* ɵɵtemplate */.YNc(1, StandingsComponent_ng_template_22_div_1_Template, 4, 18, "div", 21);
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r2.standings !== undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r2.standings.driverStandingByRound != undefined);
} }
function StandingsComponent_ng_template_24_div_0_tr_10_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 26);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td", 27);
    core/* ɵɵelementStart */.TgZ(4, "a", 28);
    core/* ɵɵelementStart */.TgZ(5, "div");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵpipe */.ALo(9, "number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r14 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r14.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", standing_r14.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", standing_r14.name, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(core/* ɵɵpipeBind1 */.lcZ(9, 4, standing_r14.points));
} }
function StandingsComponent_ng_template_24_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 22);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr", 23);
    core/* ɵɵelement */._UZ(4, "th", 24);
    core/* ɵɵelementStart */.TgZ(5, "th", 24);
    core/* ɵɵtext */._uU(6, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "th", 24);
    core/* ɵɵtext */._uU(8, "Points");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tbody");
    core/* ɵɵtemplate */.YNc(10, StandingsComponent_ng_template_24_div_0_tr_10_Template, 10, 6, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r11 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(10);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r11.standings.constructorStandings);
} }
function StandingsComponent_ng_template_24_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 31);
    core/* ɵɵelement */._UZ(1, "apx-chart", 32);
    core/* ɵɵelement */._UZ(2, "apx-chart", 32);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r12 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r12.constructorStandingsSeries)("yaxis", ctx_r12.defYaxis)("title", ctx_r12.titleStandings)("chart", ctx_r12.lineChart)("stroke", ctx_r12.defStroke)("tooltip", ctx_r12.defTooltip);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("series", ctx_r12.constructorRoundsSeries)("yaxis", ctx_r12.defYaxis)("title", ctx_r12.titleByRound)("chart", ctx_r12.lineChart)("stroke", ctx_r12.defStroke)("tooltip", ctx_r12.defTooltip);
} }
function StandingsComponent_ng_template_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, StandingsComponent_ng_template_24_div_0_Template, 11, 1, "div", 20);
    core/* ɵɵtemplate */.YNc(1, StandingsComponent_ng_template_24_div_1_Template, 3, 12, "div", 21);
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.standings !== undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.standings.constructorStandingByRound != undefined);
} }
function StandingsComponent_ng_template_26_div_0_tr_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 26);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td", 27);
    core/* ɵɵelementStart */.TgZ(4, "a", 28);
    core/* ɵɵelementStart */.TgZ(5, "div");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵpipe */.ALo(9, "date");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "td", 27);
    core/* ɵɵelementStart */.TgZ(11, "a", 28);
    core/* ɵɵelementStart */.TgZ(12, "div");
    core/* ɵɵtext */._uU(13);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const race_r18 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(race_r18.round);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", race_r18.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", race_r18.raceName, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(core/* ɵɵpipeBind1 */.lcZ(9, 6, race_r18.date));
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵpropertyInterpolate */.s9C("href", "https://www.google.com/maps?q=" + race_r18.x + "," + race_r18.y, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", race_r18.circuitName, " ");
} }
function StandingsComponent_ng_template_26_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 22);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr", 23);
    core/* ɵɵelementStart */.TgZ(4, "th", 33);
    core/* ɵɵtext */._uU(5, "Round");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "th", 24);
    core/* ɵɵtext */._uU(7, "Race");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "th", 33);
    core/* ɵɵtext */._uU(9, "Date");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "th", 24);
    core/* ɵɵtext */._uU(11, "Circuit");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "tbody");
    core/* ɵɵtemplate */.YNc(13, StandingsComponent_ng_template_26_div_0_tr_13_Template, 14, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r16 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(13);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r16.standings.races);
} }
function StandingsComponent_ng_template_26_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, StandingsComponent_ng_template_26_div_0_Template, 14, 1, "div", 20);
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r4.standings !== undefined);
} }
class StandingsComponent {
    constructor(restService, utilityService) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.driverStandingsSeries = [];
        this.driverResultByRoundSeries = [];
        this.driverPointsByRoundSeries = [];
        this.constructorStandingsSeries = [];
        this.constructorRoundsSeries = [];
        this.currentDate = new Date();
        this.lineChart = {
            height: 500,
            type: "line",
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.numericXaxis = {
            type: 'numeric',
            decimalsInFloat: 0,
            min: 1,
            max: 23,
            tickAmount: 11
        };
        this.defYaxis = {
            decimalsInFloat: 0
        };
        this.defYaxisWithDecimal = {
            decimalsInFloat: 1
        };
        this.reverseYAxis = {
            tickAmount: 20,
            decimalsInFloat: 0,
            reversed: true
        };
        this.titleStandings = {
            text: 'Points through season',
            align: 'center'
        };
        this.titleResults = {
            text: 'Results through season',
            align: 'center'
        };
        this.titleByRound = {
            text: 'Points by race',
            align: 'center'
        };
        this.defStroke = {
            width: 3
        };
        this.defTooltip = {
            shared: false,
        };
        let observableStandings = this.restService.getStandings();
        observableStandings.subscribe({
            next: data => {
                this.standings = data;
                this.standings.driverStandingByRound.forEach((standing) => {
                    this.driverStandingsSeries.push({
                        name: standing.name,
                        data: standing.series,
                        color: standing.color
                    });
                });
                this.standings.driverPointsByRound.forEach((standing) => {
                    this.driverPointsByRoundSeries.push({
                        name: standing.name,
                        data: standing.series,
                        color: standing.color
                    });
                });
                this.standings.driverResultByRound.forEach((standing) => {
                    this.driverResultByRoundSeries.push({
                        name: standing.name,
                        data: standing.series,
                        color: standing.color
                    });
                });
                this.standings.constructorStandingByRound.forEach((standing) => {
                    this.constructorStandingsSeries.push({
                        name: standing.name,
                        data: standing.series,
                        color: standing.color
                    });
                });
                this.standings.constructorPointsByRound.forEach((standing) => {
                    this.constructorRoundsSeries.push({
                        name: standing.name,
                        data: standing.series,
                        color: standing.color
                    });
                });
                return data;
            }
        });
        this.utilityService.setTitle('Championship');
        this.utilityService.reloadPosts(4);
    }
}
StandingsComponent.ɵfac = function StandingsComponent_Factory(t) { return new (t || StandingsComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
StandingsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: StandingsComponent, selectors: [["standings-cmp"]], viewQuery: function StandingsComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(table/* MatTable */.BZ, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.driverTable = _t.first);
    } }, decls: 27, vars: 3, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], ["label", "Drivers"], ["matTabContent", ""], ["label", "Constructors"], ["label", "Races"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [4, "ngIf"], ["class", "div-padded-2rem", 4, "ngIf"], [1, "table", "table-striped"], [1, "text-center", "bg-primary", "text-white"], ["scope", "col"], ["scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["scope", "row"], [1, "hover-effects-row"], ["target", "_blank", 2, "color", "black", 3, "href"], [1, "d-sm-none", "d-none", "d-lg-table-cell", "text-center"], [1, "d-sm-none", "d-none", "d-lg-table-cell", "hover-effects-row"], [1, "div-padded-2rem"], [3, "series", "yaxis", "title", "chart", "stroke", "tooltip"], ["scope", "col", 1, "text-left"]], template: function StandingsComponent_Template(rf, ctx) { if (rf & 1) {
        const _r20 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function StandingsComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function StandingsComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function StandingsComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(4); });
        core/* ɵɵtext */._uU(10, "Post");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function StandingsComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(4); });
        core/* ɵɵtext */._uU(12, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, StandingsComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function StandingsComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r20); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function StandingsComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r20); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "mat-tab-group");
        core/* ɵɵelementStart */.TgZ(21, "mat-tab", 12);
        core/* ɵɵtemplate */.YNc(22, StandingsComponent_ng_template_22_Template, 2, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(23, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(24, StandingsComponent_ng_template_24_Template, 2, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(25, "mat-tab", 15);
        core/* ɵɵtemplate */.YNc(26, StandingsComponent_ng_template_26_Template, 1, 1, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, common/* NgIf */.O5, ng_apexcharts/* ChartComponent */.x], pipes: [common/* DecimalPipe */.JJ, common/* DatePipe */.uU], styles: [""] });

// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/overlay.js + 1 modules
var overlay = __webpack_require__(625);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/portal.js
var portal = __webpack_require__(7636);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/observable/defer.js
var defer = __webpack_require__(1439);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/observable/of.js
var of = __webpack_require__(5917);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/filter.js
var filter = __webpack_require__(5435);
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/keycodes.js
var keycodes = __webpack_require__(6461);
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/dialog.js













/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Configuration for opening a modal dialog with the MatDialog service.
 */






function MatDialogContainer_ng_template_0_Template(rf, ctx) { }
class MatDialogConfig {
    constructor() {
        /** The ARIA role of the dialog element. */
        this.role = 'dialog';
        /** Custom class for the overlay pane. */
        this.panelClass = '';
        /** Whether the dialog has a backdrop. */
        this.hasBackdrop = true;
        /** Custom class for the backdrop. */
        this.backdropClass = '';
        /** Whether the user can use escape or clicking on the backdrop to close the modal. */
        this.disableClose = false;
        /** Width of the dialog. */
        this.width = '';
        /** Height of the dialog. */
        this.height = '';
        /** Max-width of the dialog. If a number is provided, assumes pixel units. Defaults to 80vw. */
        this.maxWidth = '80vw';
        /** Data being injected into the child component. */
        this.data = null;
        /** ID of the element that describes the dialog. */
        this.ariaDescribedBy = null;
        /** ID of the element that labels the dialog. */
        this.ariaLabelledBy = null;
        /** Aria label to assign to the dialog element. */
        this.ariaLabel = null;
        /** Whether the dialog should focus the first focusable element on open. */
        this.autoFocus = true;
        /**
         * Whether the dialog should restore focus to the
         * previously-focused element, after it's closed.
         */
        this.restoreFocus = true;
        /**
         * Whether the dialog should close when the user goes backwards/forwards in history.
         * Note that this usually doesn't include clicking on links (unless the user is using
         * the `HashLocationStrategy`).
         */
        this.closeOnNavigation = true;
        // TODO(jelbourn): add configuration for lifecycle hooks, ARIA labelling.
    }
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Animations used by MatDialog.
 * @docs-private
 */
const matDialogAnimations = {
    /** Animation that is applied on the dialog container by default. */
    dialogContainer: (0,animations/* trigger */.X$)('dialogContainer', [
        // Note: The `enter` animation transitions to `transform: none`, because for some reason
        // specifying the transform explicitly, causes IE both to blur the dialog content and
        // decimate the animation performance. Leaving it as `none` solves both issues.
        (0,animations/* state */.SB)('void, exit', (0,animations/* style */.oB)({ opacity: 0, transform: 'scale(0.7)' })),
        (0,animations/* state */.SB)('enter', (0,animations/* style */.oB)({ transform: 'none' })),
        (0,animations/* transition */.eR)('* => enter', (0,animations/* animate */.jt)('150ms cubic-bezier(0, 0, 0.2, 1)', (0,animations/* style */.oB)({ transform: 'none', opacity: 1 }))),
        (0,animations/* transition */.eR)('* => void, * => exit', (0,animations/* animate */.jt)('75ms cubic-bezier(0.4, 0.0, 0.2, 1)', (0,animations/* style */.oB)({ opacity: 0 }))),
    ])
};

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Throws an exception for the case when a ComponentPortal is
 * attached to a DomPortalOutlet without an origin.
 * @docs-private
 */
function throwMatDialogContentAlreadyAttachedError() {
    throw Error('Attempting to attach dialog content after content is already attached');
}
/**
 * Base class for the `MatDialogContainer`. The base class does not implement
 * animations as these are left to implementers of the dialog container.
 */
class _MatDialogContainerBase extends portal/* BasePortalOutlet */.en {
    constructor(_elementRef, _focusTrapFactory, _changeDetectorRef, _document, 
    /** The dialog configuration. */
    _config, _focusMonitor) {
        super();
        this._elementRef = _elementRef;
        this._focusTrapFactory = _focusTrapFactory;
        this._changeDetectorRef = _changeDetectorRef;
        this._config = _config;
        this._focusMonitor = _focusMonitor;
        /** Emits when an animation state changes. */
        this._animationStateChanged = new core/* EventEmitter */.vpe();
        /** Element that was focused before the dialog was opened. Save this to restore upon close. */
        this._elementFocusedBeforeDialogWasOpened = null;
        /**
         * Type of interaction that led to the dialog being closed. This is used to determine
         * whether the focus style will be applied when returning focus to its original location
         * after the dialog is closed.
         */
        this._closeInteractionType = null;
        /**
         * Attaches a DOM portal to the dialog container.
         * @param portal Portal to be attached.
         * @deprecated To be turned into a method.
         * @breaking-change 10.0.0
         */
        this.attachDomPortal = (portal) => {
            if (this._portalOutlet.hasAttached() && (typeof ngDevMode === 'undefined' || ngDevMode)) {
                throwMatDialogContentAlreadyAttachedError();
            }
            return this._portalOutlet.attachDomPortal(portal);
        };
        this._ariaLabelledBy = _config.ariaLabelledBy || null;
        this._document = _document;
    }
    /** Initializes the dialog container with the attached content. */
    _initializeWithAttachedContent() {
        this._setupFocusTrap();
        // Save the previously focused element. This element will be re-focused
        // when the dialog closes.
        this._capturePreviouslyFocusedElement();
        // Move focus onto the dialog immediately in order to prevent the user
        // from accidentally opening multiple dialogs at the same time.
        this._focusDialogContainer();
    }
    /**
     * Attach a ComponentPortal as content to this dialog container.
     * @param portal Portal to be attached as the dialog content.
     */
    attachComponentPortal(portal) {
        if (this._portalOutlet.hasAttached() && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throwMatDialogContentAlreadyAttachedError();
        }
        return this._portalOutlet.attachComponentPortal(portal);
    }
    /**
     * Attach a TemplatePortal as content to this dialog container.
     * @param portal Portal to be attached as the dialog content.
     */
    attachTemplatePortal(portal) {
        if (this._portalOutlet.hasAttached() && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throwMatDialogContentAlreadyAttachedError();
        }
        return this._portalOutlet.attachTemplatePortal(portal);
    }
    /** Moves focus back into the dialog if it was moved out. */
    _recaptureFocus() {
        if (!this._containsFocus()) {
            const focusContainer = !this._config.autoFocus || !this._focusTrap.focusInitialElement();
            if (focusContainer) {
                this._elementRef.nativeElement.focus();
            }
        }
    }
    /** Moves the focus inside the focus trap. */
    _trapFocus() {
        // If we were to attempt to focus immediately, then the content of the dialog would not yet be
        // ready in instances where change detection has to run first. To deal with this, we simply
        // wait for the microtask queue to be empty.
        if (this._config.autoFocus) {
            this._focusTrap.focusInitialElementWhenReady();
        }
        else if (!this._containsFocus()) {
            // Otherwise ensure that focus is on the dialog container. It's possible that a different
            // component tried to move focus while the open animation was running. See:
            // https://github.com/angular/components/issues/16215. Note that we only want to do this
            // if the focus isn't inside the dialog already, because it's possible that the consumer
            // turned off `autoFocus` in order to move focus themselves.
            this._elementRef.nativeElement.focus();
        }
    }
    /** Restores focus to the element that was focused before the dialog opened. */
    _restoreFocus() {
        const previousElement = this._elementFocusedBeforeDialogWasOpened;
        // We need the extra check, because IE can set the `activeElement` to null in some cases.
        if (this._config.restoreFocus && previousElement &&
            typeof previousElement.focus === 'function') {
            const activeElement = (0,platform/* _getFocusedElementPierceShadowDom */.ht)();
            const element = this._elementRef.nativeElement;
            // Make sure that focus is still inside the dialog or is on the body (usually because a
            // non-focusable element like the backdrop was clicked) before moving it. It's possible that
            // the consumer moved it themselves before the animation was done, in which case we shouldn't
            // do anything.
            if (!activeElement || activeElement === this._document.body || activeElement === element ||
                element.contains(activeElement)) {
                if (this._focusMonitor) {
                    this._focusMonitor.focusVia(previousElement, this._closeInteractionType);
                    this._closeInteractionType = null;
                }
                else {
                    previousElement.focus();
                }
            }
        }
        if (this._focusTrap) {
            this._focusTrap.destroy();
        }
    }
    /** Sets up the focus trap. */
    _setupFocusTrap() {
        this._focusTrap = this._focusTrapFactory.create(this._elementRef.nativeElement);
    }
    /** Captures the element that was focused before the dialog was opened. */
    _capturePreviouslyFocusedElement() {
        if (this._document) {
            this._elementFocusedBeforeDialogWasOpened = (0,platform/* _getFocusedElementPierceShadowDom */.ht)();
        }
    }
    /** Focuses the dialog container. */
    _focusDialogContainer() {
        // Note that there is no focus method when rendering on the server.
        if (this._elementRef.nativeElement.focus) {
            this._elementRef.nativeElement.focus();
        }
    }
    /** Returns whether focus is inside the dialog. */
    _containsFocus() {
        const element = this._elementRef.nativeElement;
        const activeElement = (0,platform/* _getFocusedElementPierceShadowDom */.ht)();
        return element === activeElement || element.contains(activeElement);
    }
}
_MatDialogContainerBase.ɵfac = function _MatDialogContainerBase_Factory(t) { return new (t || _MatDialogContainerBase)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusTrapFactory */.qV), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(common/* DOCUMENT */.K0, 8), core/* ɵɵdirectiveInject */.Y36(MatDialogConfig), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusMonitor */.tE)); };
_MatDialogContainerBase.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: _MatDialogContainerBase, viewQuery: function _MatDialogContainerBase_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(portal/* CdkPortalOutlet */.Pl, 7);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._portalOutlet = _t.first);
    } }, features: [core/* ɵɵInheritDefinitionFeature */.qOj] });
_MatDialogContainerBase.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: a11y/* FocusTrapFactory */.qV },
    { type: core/* ChangeDetectorRef */.sBO },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [common/* DOCUMENT */.K0,] }] },
    { type: MatDialogConfig },
    { type: a11y/* FocusMonitor */.tE }
];
_MatDialogContainerBase.propDecorators = {
    _portalOutlet: [{ type: core/* ViewChild */.i9L, args: [portal/* CdkPortalOutlet */.Pl, { static: true },] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(_MatDialogContainerBase, [{
        type: core/* Directive */.Xek
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: a11y/* FocusTrapFactory */.qV }, { type: core/* ChangeDetectorRef */.sBO }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [common/* DOCUMENT */.K0]
            }] }, { type: MatDialogConfig }, { type: a11y/* FocusMonitor */.tE }]; }, { _portalOutlet: [{
            type: core/* ViewChild */.i9L,
            args: [portal/* CdkPortalOutlet */.Pl, { static: true }]
        }] }); })();
/**
 * Internal component that wraps user-provided dialog content.
 * Animation is based on https://material.io/guidelines/motion/choreography.html.
 * @docs-private
 */
class MatDialogContainer extends _MatDialogContainerBase {
    constructor() {
        super(...arguments);
        /** State of the dialog animation. */
        this._state = 'enter';
    }
    /** Callback, invoked whenever an animation on the host completes. */
    _onAnimationDone({ toState, totalTime }) {
        if (toState === 'enter') {
            this._trapFocus();
            this._animationStateChanged.next({ state: 'opened', totalTime });
        }
        else if (toState === 'exit') {
            this._restoreFocus();
            this._animationStateChanged.next({ state: 'closed', totalTime });
        }
    }
    /** Callback, invoked when an animation on the host starts. */
    _onAnimationStart({ toState, totalTime }) {
        if (toState === 'enter') {
            this._animationStateChanged.next({ state: 'opening', totalTime });
        }
        else if (toState === 'exit' || toState === 'void') {
            this._animationStateChanged.next({ state: 'closing', totalTime });
        }
    }
    /** Starts the dialog exit animation. */
    _startExitAnimation() {
        this._state = 'exit';
        // Mark the container for check so it can react if the
        // view container is using OnPush change detection.
        this._changeDetectorRef.markForCheck();
    }
}
MatDialogContainer.ɵfac = /*@__PURE__*/ function () { let ɵMatDialogContainer_BaseFactory; return function MatDialogContainer_Factory(t) { return (ɵMatDialogContainer_BaseFactory || (ɵMatDialogContainer_BaseFactory = core/* ɵɵgetInheritedFactory */.n5z(MatDialogContainer)))(t || MatDialogContainer); }; }();
MatDialogContainer.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatDialogContainer, selectors: [["mat-dialog-container"]], hostAttrs: ["tabindex", "-1", "aria-modal", "true", 1, "mat-dialog-container"], hostVars: 6, hostBindings: function MatDialogContainer_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵsyntheticHostListener */.WFA("@dialogContainer.start", function MatDialogContainer_animation_dialogContainer_start_HostBindingHandler($event) { return ctx._onAnimationStart($event); })("@dialogContainer.done", function MatDialogContainer_animation_dialogContainer_done_HostBindingHandler($event) { return ctx._onAnimationDone($event); });
    } if (rf & 2) {
        core/* ɵɵhostProperty */.Ikx("id", ctx._id);
        core/* ɵɵattribute */.uIk("role", ctx._config.role)("aria-labelledby", ctx._config.ariaLabel ? null : ctx._ariaLabelledBy)("aria-label", ctx._config.ariaLabel)("aria-describedby", ctx._config.ariaDescribedBy || null);
        core/* ɵɵsyntheticHostProperty */.d8E("@dialogContainer", ctx._state);
    } }, features: [core/* ɵɵInheritDefinitionFeature */.qOj], decls: 1, vars: 0, consts: [["cdkPortalOutlet", ""]], template: function MatDialogContainer_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵtemplate */.YNc(0, MatDialogContainer_ng_template_0_Template, 0, 0, "ng-template", 0);
    } }, directives: [portal/* CdkPortalOutlet */.Pl], styles: [".mat-dialog-container{display:block;padding:24px;border-radius:4px;box-sizing:border-box;overflow:auto;outline:0;width:100%;height:100%;min-height:inherit;max-height:inherit}.cdk-high-contrast-active .mat-dialog-container{outline:solid 1px}.mat-dialog-content{display:block;margin:0 -24px;padding:0 24px;max-height:65vh;overflow:auto;-webkit-overflow-scrolling:touch}.mat-dialog-title{margin:0 0 20px;display:block}.mat-dialog-actions{padding:8px 0;display:flex;flex-wrap:wrap;min-height:52px;align-items:center;box-sizing:content-box;margin-bottom:-24px}.mat-dialog-actions[align=end]{justify-content:flex-end}.mat-dialog-actions[align=center]{justify-content:center}.mat-dialog-actions .mat-button-base+.mat-button-base,.mat-dialog-actions .mat-mdc-button-base+.mat-mdc-button-base{margin-left:8px}[dir=rtl] .mat-dialog-actions .mat-button-base+.mat-button-base,[dir=rtl] .mat-dialog-actions .mat-mdc-button-base+.mat-mdc-button-base{margin-left:0;margin-right:8px}\n"], encapsulation: 2, data: { animation: [matDialogAnimations.dialogContainer] } });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogContainer, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-dialog-container',
                template: "<ng-template cdkPortalOutlet></ng-template>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                // Using OnPush for dialogs caused some G3 sync issues. Disabled until we can track them down.
                // tslint:disable-next-line:validate-decorators
                changeDetection: core/* ChangeDetectionStrategy.Default */.n4l.Default,
                animations: [matDialogAnimations.dialogContainer],
                host: {
                    'class': 'mat-dialog-container',
                    'tabindex': '-1',
                    'aria-modal': 'true',
                    '[id]': '_id',
                    '[attr.role]': '_config.role',
                    '[attr.aria-labelledby]': '_config.ariaLabel ? null : _ariaLabelledBy',
                    '[attr.aria-label]': '_config.ariaLabel',
                    '[attr.aria-describedby]': '_config.ariaDescribedBy || null',
                    '[@dialogContainer]': '_state',
                    '(@dialogContainer.start)': '_onAnimationStart($event)',
                    '(@dialogContainer.done)': '_onAnimationDone($event)'
                },
                styles: [".mat-dialog-container{display:block;padding:24px;border-radius:4px;box-sizing:border-box;overflow:auto;outline:0;width:100%;height:100%;min-height:inherit;max-height:inherit}.cdk-high-contrast-active .mat-dialog-container{outline:solid 1px}.mat-dialog-content{display:block;margin:0 -24px;padding:0 24px;max-height:65vh;overflow:auto;-webkit-overflow-scrolling:touch}.mat-dialog-title{margin:0 0 20px;display:block}.mat-dialog-actions{padding:8px 0;display:flex;flex-wrap:wrap;min-height:52px;align-items:center;box-sizing:content-box;margin-bottom:-24px}.mat-dialog-actions[align=end]{justify-content:flex-end}.mat-dialog-actions[align=center]{justify-content:center}.mat-dialog-actions .mat-button-base+.mat-button-base,.mat-dialog-actions .mat-mdc-button-base+.mat-mdc-button-base{margin-left:8px}[dir=rtl] .mat-dialog-actions .mat-button-base+.mat-button-base,[dir=rtl] .mat-dialog-actions .mat-mdc-button-base+.mat-mdc-button-base{margin-left:0;margin-right:8px}\n"]
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
// TODO(jelbourn): resizing
// Counter for unique dialog ids.
let uniqueId = 0;
/**
 * Reference to a dialog opened via the MatDialog service.
 */
class MatDialogRef {
    constructor(_overlayRef, _containerInstance, 
    /** Id of the dialog. */
    id = `mat-dialog-${uniqueId++}`) {
        this._overlayRef = _overlayRef;
        this._containerInstance = _containerInstance;
        this.id = id;
        /** Whether the user is allowed to close the dialog. */
        this.disableClose = this._containerInstance._config.disableClose;
        /** Subject for notifying the user that the dialog has finished opening. */
        this._afterOpened = new Subject/* Subject */.xQ();
        /** Subject for notifying the user that the dialog has finished closing. */
        this._afterClosed = new Subject/* Subject */.xQ();
        /** Subject for notifying the user that the dialog has started closing. */
        this._beforeClosed = new Subject/* Subject */.xQ();
        /** Current state of the dialog. */
        this._state = 0 /* OPEN */;
        // Pass the id along to the container.
        _containerInstance._id = id;
        // Emit when opening animation completes
        _containerInstance._animationStateChanged.pipe((0,filter/* filter */.h)(event => event.state === 'opened'), (0,take/* take */.q)(1))
            .subscribe(() => {
            this._afterOpened.next();
            this._afterOpened.complete();
        });
        // Dispose overlay when closing animation is complete
        _containerInstance._animationStateChanged.pipe((0,filter/* filter */.h)(event => event.state === 'closed'), (0,take/* take */.q)(1)).subscribe(() => {
            clearTimeout(this._closeFallbackTimeout);
            this._finishDialogClose();
        });
        _overlayRef.detachments().subscribe(() => {
            this._beforeClosed.next(this._result);
            this._beforeClosed.complete();
            this._afterClosed.next(this._result);
            this._afterClosed.complete();
            this.componentInstance = null;
            this._overlayRef.dispose();
        });
        _overlayRef.keydownEvents()
            .pipe((0,filter/* filter */.h)(event => {
            return event.keyCode === keycodes/* ESCAPE */.hY && !this.disableClose && !(0,keycodes/* hasModifierKey */.Vb)(event);
        }))
            .subscribe(event => {
            event.preventDefault();
            _closeDialogVia(this, 'keyboard');
        });
        _overlayRef.backdropClick().subscribe(() => {
            if (this.disableClose) {
                this._containerInstance._recaptureFocus();
            }
            else {
                _closeDialogVia(this, 'mouse');
            }
        });
    }
    /**
     * Close the dialog.
     * @param dialogResult Optional result to return to the dialog opener.
     */
    close(dialogResult) {
        this._result = dialogResult;
        // Transition the backdrop in parallel to the dialog.
        this._containerInstance._animationStateChanged.pipe((0,filter/* filter */.h)(event => event.state === 'closing'), (0,take/* take */.q)(1))
            .subscribe(event => {
            this._beforeClosed.next(dialogResult);
            this._beforeClosed.complete();
            this._overlayRef.detachBackdrop();
            // The logic that disposes of the overlay depends on the exit animation completing, however
            // it isn't guaranteed if the parent view is destroyed while it's running. Add a fallback
            // timeout which will clean everything up if the animation hasn't fired within the specified
            // amount of time plus 100ms. We don't need to run this outside the NgZone, because for the
            // vast majority of cases the timeout will have been cleared before it has the chance to fire.
            this._closeFallbackTimeout = setTimeout(() => this._finishDialogClose(), event.totalTime + 100);
        });
        this._state = 1 /* CLOSING */;
        this._containerInstance._startExitAnimation();
    }
    /**
     * Gets an observable that is notified when the dialog is finished opening.
     */
    afterOpened() {
        return this._afterOpened;
    }
    /**
     * Gets an observable that is notified when the dialog is finished closing.
     */
    afterClosed() {
        return this._afterClosed;
    }
    /**
     * Gets an observable that is notified when the dialog has started closing.
     */
    beforeClosed() {
        return this._beforeClosed;
    }
    /**
     * Gets an observable that emits when the overlay's backdrop has been clicked.
     */
    backdropClick() {
        return this._overlayRef.backdropClick();
    }
    /**
     * Gets an observable that emits when keydown events are targeted on the overlay.
     */
    keydownEvents() {
        return this._overlayRef.keydownEvents();
    }
    /**
     * Updates the dialog's position.
     * @param position New dialog position.
     */
    updatePosition(position) {
        let strategy = this._getPositionStrategy();
        if (position && (position.left || position.right)) {
            position.left ? strategy.left(position.left) : strategy.right(position.right);
        }
        else {
            strategy.centerHorizontally();
        }
        if (position && (position.top || position.bottom)) {
            position.top ? strategy.top(position.top) : strategy.bottom(position.bottom);
        }
        else {
            strategy.centerVertically();
        }
        this._overlayRef.updatePosition();
        return this;
    }
    /**
     * Updates the dialog's width and height.
     * @param width New width of the dialog.
     * @param height New height of the dialog.
     */
    updateSize(width = '', height = '') {
        this._overlayRef.updateSize({ width, height });
        this._overlayRef.updatePosition();
        return this;
    }
    /** Add a CSS class or an array of classes to the overlay pane. */
    addPanelClass(classes) {
        this._overlayRef.addPanelClass(classes);
        return this;
    }
    /** Remove a CSS class or an array of classes from the overlay pane. */
    removePanelClass(classes) {
        this._overlayRef.removePanelClass(classes);
        return this;
    }
    /** Gets the current state of the dialog's lifecycle. */
    getState() {
        return this._state;
    }
    /**
     * Finishes the dialog close by updating the state of the dialog
     * and disposing the overlay.
     */
    _finishDialogClose() {
        this._state = 2 /* CLOSED */;
        this._overlayRef.dispose();
    }
    /** Fetches the position strategy object from the overlay ref. */
    _getPositionStrategy() {
        return this._overlayRef.getConfig().positionStrategy;
    }
}
/**
 * Closes the dialog with the specified interaction type. This is currently not part of
 * `MatDialogRef` as that would conflict with custom dialog ref mocks provided in tests.
 * More details. See: https://github.com/angular/components/pull/9257#issuecomment-651342226.
 */
// TODO: TODO: Move this back into `MatDialogRef` when we provide an official mock dialog ref.
function _closeDialogVia(ref, interactionType, result) {
    // Some mock dialog ref instances in tests do not have the `_containerInstance` property.
    // For those, we keep the behavior as is and do not deal with the interaction type.
    if (ref._containerInstance !== undefined) {
        ref._containerInstance._closeInteractionType = interactionType;
    }
    return ref.close(result);
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** Injection token that can be used to access the data that was passed in to a dialog. */
const MAT_DIALOG_DATA = new core/* InjectionToken */.OlP('MatDialogData');
/** Injection token that can be used to specify default dialog options. */
const MAT_DIALOG_DEFAULT_OPTIONS = new core/* InjectionToken */.OlP('mat-dialog-default-options');
/** Injection token that determines the scroll handling while the dialog is open. */
const MAT_DIALOG_SCROLL_STRATEGY = new core/* InjectionToken */.OlP('mat-dialog-scroll-strategy');
/** @docs-private */
function MAT_DIALOG_SCROLL_STRATEGY_FACTORY(overlay) {
    return () => overlay.scrollStrategies.block();
}
/** @docs-private */
function MAT_DIALOG_SCROLL_STRATEGY_PROVIDER_FACTORY(overlay) {
    return () => overlay.scrollStrategies.block();
}
/** @docs-private */
const MAT_DIALOG_SCROLL_STRATEGY_PROVIDER = {
    provide: MAT_DIALOG_SCROLL_STRATEGY,
    deps: [overlay/* Overlay */.aV],
    useFactory: MAT_DIALOG_SCROLL_STRATEGY_PROVIDER_FACTORY,
};
/**
 * Base class for dialog services. The base dialog service allows
 * for arbitrary dialog refs and dialog container components.
 */
class _MatDialogBase {
    constructor(_overlay, _injector, _defaultOptions, _parentDialog, _overlayContainer, scrollStrategy, _dialogRefConstructor, _dialogContainerType, _dialogDataToken) {
        this._overlay = _overlay;
        this._injector = _injector;
        this._defaultOptions = _defaultOptions;
        this._parentDialog = _parentDialog;
        this._overlayContainer = _overlayContainer;
        this._dialogRefConstructor = _dialogRefConstructor;
        this._dialogContainerType = _dialogContainerType;
        this._dialogDataToken = _dialogDataToken;
        this._openDialogsAtThisLevel = [];
        this._afterAllClosedAtThisLevel = new Subject/* Subject */.xQ();
        this._afterOpenedAtThisLevel = new Subject/* Subject */.xQ();
        this._ariaHiddenElements = new Map();
        // TODO (jelbourn): tighten the typing right-hand side of this expression.
        /**
         * Stream that emits when all open dialog have finished closing.
         * Will emit on subscribe if there are no open dialogs to begin with.
         */
        this.afterAllClosed = (0,defer/* defer */.P)(() => this.openDialogs.length ?
            this._getAfterAllClosed() :
            this._getAfterAllClosed().pipe((0,startWith/* startWith */.O)(undefined)));
        this._scrollStrategy = scrollStrategy;
    }
    /** Keeps track of the currently-open dialogs. */
    get openDialogs() {
        return this._parentDialog ? this._parentDialog.openDialogs : this._openDialogsAtThisLevel;
    }
    /** Stream that emits when a dialog has been opened. */
    get afterOpened() {
        return this._parentDialog ? this._parentDialog.afterOpened : this._afterOpenedAtThisLevel;
    }
    _getAfterAllClosed() {
        const parent = this._parentDialog;
        return parent ? parent._getAfterAllClosed() : this._afterAllClosedAtThisLevel;
    }
    open(componentOrTemplateRef, config) {
        config = _applyConfigDefaults(config, this._defaultOptions || new MatDialogConfig());
        if (config.id && this.getDialogById(config.id) &&
            (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw Error(`Dialog with id "${config.id}" exists already. The dialog id must be unique.`);
        }
        const overlayRef = this._createOverlay(config);
        const dialogContainer = this._attachDialogContainer(overlayRef, config);
        const dialogRef = this._attachDialogContent(componentOrTemplateRef, dialogContainer, overlayRef, config);
        // If this is the first dialog that we're opening, hide all the non-overlay content.
        if (!this.openDialogs.length) {
            this._hideNonDialogContentFromAssistiveTechnology();
        }
        this.openDialogs.push(dialogRef);
        dialogRef.afterClosed().subscribe(() => this._removeOpenDialog(dialogRef));
        this.afterOpened.next(dialogRef);
        // Notify the dialog container that the content has been attached.
        dialogContainer._initializeWithAttachedContent();
        return dialogRef;
    }
    /**
     * Closes all of the currently-open dialogs.
     */
    closeAll() {
        this._closeDialogs(this.openDialogs);
    }
    /**
     * Finds an open dialog by its id.
     * @param id ID to use when looking up the dialog.
     */
    getDialogById(id) {
        return this.openDialogs.find(dialog => dialog.id === id);
    }
    ngOnDestroy() {
        // Only close the dialogs at this level on destroy
        // since the parent service may still be active.
        this._closeDialogs(this._openDialogsAtThisLevel);
        this._afterAllClosedAtThisLevel.complete();
        this._afterOpenedAtThisLevel.complete();
    }
    /**
     * Creates the overlay into which the dialog will be loaded.
     * @param config The dialog configuration.
     * @returns A promise resolving to the OverlayRef for the created overlay.
     */
    _createOverlay(config) {
        const overlayConfig = this._getOverlayConfig(config);
        return this._overlay.create(overlayConfig);
    }
    /**
     * Creates an overlay config from a dialog config.
     * @param dialogConfig The dialog configuration.
     * @returns The overlay configuration.
     */
    _getOverlayConfig(dialogConfig) {
        const state = new overlay/* OverlayConfig */.X_({
            positionStrategy: this._overlay.position().global(),
            scrollStrategy: dialogConfig.scrollStrategy || this._scrollStrategy(),
            panelClass: dialogConfig.panelClass,
            hasBackdrop: dialogConfig.hasBackdrop,
            direction: dialogConfig.direction,
            minWidth: dialogConfig.minWidth,
            minHeight: dialogConfig.minHeight,
            maxWidth: dialogConfig.maxWidth,
            maxHeight: dialogConfig.maxHeight,
            disposeOnNavigation: dialogConfig.closeOnNavigation
        });
        if (dialogConfig.backdropClass) {
            state.backdropClass = dialogConfig.backdropClass;
        }
        return state;
    }
    /**
     * Attaches a dialog container to a dialog's already-created overlay.
     * @param overlay Reference to the dialog's underlying overlay.
     * @param config The dialog configuration.
     * @returns A promise resolving to a ComponentRef for the attached container.
     */
    _attachDialogContainer(overlay, config) {
        const userInjector = config && config.viewContainerRef && config.viewContainerRef.injector;
        const injector = core/* Injector.create */.zs3.create({
            parent: userInjector || this._injector,
            providers: [{ provide: MatDialogConfig, useValue: config }]
        });
        const containerPortal = new portal/* ComponentPortal */.C5(this._dialogContainerType, config.viewContainerRef, injector, config.componentFactoryResolver);
        const containerRef = overlay.attach(containerPortal);
        return containerRef.instance;
    }
    /**
     * Attaches the user-provided component to the already-created dialog container.
     * @param componentOrTemplateRef The type of component being loaded into the dialog,
     *     or a TemplateRef to instantiate as the content.
     * @param dialogContainer Reference to the wrapping dialog container.
     * @param overlayRef Reference to the overlay in which the dialog resides.
     * @param config The dialog configuration.
     * @returns A promise resolving to the MatDialogRef that should be returned to the user.
     */
    _attachDialogContent(componentOrTemplateRef, dialogContainer, overlayRef, config) {
        // Create a reference to the dialog we're creating in order to give the user a handle
        // to modify and close it.
        const dialogRef = new this._dialogRefConstructor(overlayRef, dialogContainer, config.id);
        if (componentOrTemplateRef instanceof core/* TemplateRef */.Rgc) {
            dialogContainer.attachTemplatePortal(new portal/* TemplatePortal */.UE(componentOrTemplateRef, null, { $implicit: config.data, dialogRef }));
        }
        else {
            const injector = this._createInjector(config, dialogRef, dialogContainer);
            const contentRef = dialogContainer.attachComponentPortal(new portal/* ComponentPortal */.C5(componentOrTemplateRef, config.viewContainerRef, injector));
            dialogRef.componentInstance = contentRef.instance;
        }
        dialogRef
            .updateSize(config.width, config.height)
            .updatePosition(config.position);
        return dialogRef;
    }
    /**
     * Creates a custom injector to be used inside the dialog. This allows a component loaded inside
     * of a dialog to close itself and, optionally, to return a value.
     * @param config Config object that is used to construct the dialog.
     * @param dialogRef Reference to the dialog.
     * @param dialogContainer Dialog container element that wraps all of the contents.
     * @returns The custom injector that can be used inside the dialog.
     */
    _createInjector(config, dialogRef, dialogContainer) {
        const userInjector = config && config.viewContainerRef && config.viewContainerRef.injector;
        // The dialog container should be provided as the dialog container and the dialog's
        // content are created out of the same `ViewContainerRef` and as such, are siblings
        // for injector purposes. To allow the hierarchy that is expected, the dialog
        // container is explicitly provided in the injector.
        const providers = [
            { provide: this._dialogContainerType, useValue: dialogContainer },
            { provide: this._dialogDataToken, useValue: config.data },
            { provide: this._dialogRefConstructor, useValue: dialogRef }
        ];
        if (config.direction && (!userInjector ||
            !userInjector.get(bidi/* Directionality */.Is, null, core/* InjectFlags.Optional */.XFs.Optional))) {
            providers.push({
                provide: bidi/* Directionality */.Is,
                useValue: { value: config.direction, change: (0,of.of)() }
            });
        }
        return core/* Injector.create */.zs3.create({ parent: userInjector || this._injector, providers });
    }
    /**
     * Removes a dialog from the array of open dialogs.
     * @param dialogRef Dialog to be removed.
     */
    _removeOpenDialog(dialogRef) {
        const index = this.openDialogs.indexOf(dialogRef);
        if (index > -1) {
            this.openDialogs.splice(index, 1);
            // If all the dialogs were closed, remove/restore the `aria-hidden`
            // to a the siblings and emit to the `afterAllClosed` stream.
            if (!this.openDialogs.length) {
                this._ariaHiddenElements.forEach((previousValue, element) => {
                    if (previousValue) {
                        element.setAttribute('aria-hidden', previousValue);
                    }
                    else {
                        element.removeAttribute('aria-hidden');
                    }
                });
                this._ariaHiddenElements.clear();
                this._getAfterAllClosed().next();
            }
        }
    }
    /**
     * Hides all of the content that isn't an overlay from assistive technology.
     */
    _hideNonDialogContentFromAssistiveTechnology() {
        const overlayContainer = this._overlayContainer.getContainerElement();
        // Ensure that the overlay container is attached to the DOM.
        if (overlayContainer.parentElement) {
            const siblings = overlayContainer.parentElement.children;
            for (let i = siblings.length - 1; i > -1; i--) {
                let sibling = siblings[i];
                if (sibling !== overlayContainer &&
                    sibling.nodeName !== 'SCRIPT' &&
                    sibling.nodeName !== 'STYLE' &&
                    !sibling.hasAttribute('aria-live')) {
                    this._ariaHiddenElements.set(sibling, sibling.getAttribute('aria-hidden'));
                    sibling.setAttribute('aria-hidden', 'true');
                }
            }
        }
    }
    /** Closes all of the dialogs in an array. */
    _closeDialogs(dialogs) {
        let i = dialogs.length;
        while (i--) {
            // The `_openDialogs` property isn't updated after close until the rxjs subscription
            // runs on the next microtask, in addition to modifying the array as we're going
            // through it. We loop through all of them and call close without assuming that
            // they'll be removed from the list instantaneously.
            dialogs[i].close();
        }
    }
}
_MatDialogBase.ɵfac = function _MatDialogBase_Factory(t) { return new (t || _MatDialogBase)(core/* ɵɵdirectiveInject */.Y36(overlay/* Overlay */.aV), core/* ɵɵdirectiveInject */.Y36(core/* Injector */.zs3), core/* ɵɵdirectiveInject */.Y36(undefined), core/* ɵɵdirectiveInject */.Y36(undefined), core/* ɵɵdirectiveInject */.Y36(overlay/* OverlayContainer */.Xj), core/* ɵɵdirectiveInject */.Y36(undefined), core/* ɵɵdirectiveInject */.Y36(core/* Type */.DyG), core/* ɵɵdirectiveInject */.Y36(core/* Type */.DyG), core/* ɵɵdirectiveInject */.Y36(core/* InjectionToken */.OlP)); };
_MatDialogBase.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: _MatDialogBase });
_MatDialogBase.ctorParameters = () => [
    { type: overlay/* Overlay */.aV },
    { type: core/* Injector */.zs3 },
    { type: undefined },
    { type: undefined },
    { type: overlay/* OverlayContainer */.Xj },
    { type: undefined },
    { type: core/* Type */.DyG },
    { type: core/* Type */.DyG },
    { type: core/* InjectionToken */.OlP }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(_MatDialogBase, [{
        type: core/* Directive */.Xek
    }], function () { return [{ type: overlay/* Overlay */.aV }, { type: core/* Injector */.zs3 }, { type: undefined }, { type: undefined }, { type: overlay/* OverlayContainer */.Xj }, { type: undefined }, { type: core/* Type */.DyG }, { type: core/* Type */.DyG }, { type: core/* InjectionToken */.OlP }]; }, null); })();
/**
 * Service to open Material Design modal dialogs.
 */
class MatDialog extends _MatDialogBase {
    constructor(overlay, injector, 
    /**
     * @deprecated `_location` parameter to be removed.
     * @breaking-change 10.0.0
     */
    location, defaultOptions, scrollStrategy, parentDialog, overlayContainer) {
        super(overlay, injector, defaultOptions, parentDialog, overlayContainer, scrollStrategy, MatDialogRef, MatDialogContainer, MAT_DIALOG_DATA);
    }
}
MatDialog.ɵfac = function MatDialog_Factory(t) { return new (t || MatDialog)(core/* ɵɵinject */.LFG(overlay/* Overlay */.aV), core/* ɵɵinject */.LFG(core/* Injector */.zs3), core/* ɵɵinject */.LFG(common/* Location */.Ye, 8), core/* ɵɵinject */.LFG(MAT_DIALOG_DEFAULT_OPTIONS, 8), core/* ɵɵinject */.LFG(MAT_DIALOG_SCROLL_STRATEGY), core/* ɵɵinject */.LFG(MatDialog, 12), core/* ɵɵinject */.LFG(overlay/* OverlayContainer */.Xj)); };
MatDialog.ɵprov = /*@__PURE__*/ core/* ɵɵdefineInjectable */.Yz7({ token: MatDialog, factory: MatDialog.ɵfac });
MatDialog.ctorParameters = () => [
    { type: overlay/* Overlay */.aV },
    { type: core/* Injector */.zs3 },
    { type: common/* Location */.Ye, decorators: [{ type: core/* Optional */.FiY }] },
    { type: MatDialogConfig, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_DIALOG_DEFAULT_OPTIONS,] }] },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [MAT_DIALOG_SCROLL_STRATEGY,] }] },
    { type: MatDialog, decorators: [{ type: core/* Optional */.FiY }, { type: core/* SkipSelf */.tp0 }] },
    { type: overlay/* OverlayContainer */.Xj }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialog, [{
        type: core/* Injectable */.GSi
    }], function () { return [{ type: overlay/* Overlay */.aV }, { type: core/* Injector */.zs3 }, { type: common/* Location */.Ye, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: MatDialogConfig, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_DIALOG_DEFAULT_OPTIONS]
            }] }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [MAT_DIALOG_SCROLL_STRATEGY]
            }] }, { type: MatDialog, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* SkipSelf */.tp0
            }] }, { type: overlay/* OverlayContainer */.Xj }]; }, null); })();
/**
 * Applies default options to the dialog config.
 * @param config Config to be modified.
 * @param defaultOptions Default options provided.
 * @returns The new configuration object.
 */
function _applyConfigDefaults(config, defaultOptions) {
    return Object.assign(Object.assign({}, defaultOptions), config);
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** Counter used to generate unique IDs for dialog elements. */
let dialogElementUid = 0;
/**
 * Button that will close the current dialog.
 */
class MatDialogClose {
    constructor(
    /**
     * Reference to the containing dialog.
     * @deprecated `dialogRef` property to become private.
     * @breaking-change 13.0.0
     */
    // The dialog title directive is always used in combination with a `MatDialogRef`.
    // tslint:disable-next-line: lightweight-tokens
    dialogRef, _elementRef, _dialog) {
        this.dialogRef = dialogRef;
        this._elementRef = _elementRef;
        this._dialog = _dialog;
        /** Default to "button" to prevents accidental form submits. */
        this.type = 'button';
    }
    ngOnInit() {
        if (!this.dialogRef) {
            // When this directive is included in a dialog via TemplateRef (rather than being
            // in a Component), the DialogRef isn't available via injection because embedded
            // views cannot be given a custom injector. Instead, we look up the DialogRef by
            // ID. This must occur in `onInit`, as the ID binding for the dialog container won't
            // be resolved at constructor time.
            this.dialogRef = getClosestDialog(this._elementRef, this._dialog.openDialogs);
        }
    }
    ngOnChanges(changes) {
        const proxiedChange = changes['_matDialogClose'] || changes['_matDialogCloseResult'];
        if (proxiedChange) {
            this.dialogResult = proxiedChange.currentValue;
        }
    }
    _onButtonClick(event) {
        // Determinate the focus origin using the click event, because using the FocusMonitor will
        // result in incorrect origins. Most of the time, close buttons will be auto focused in the
        // dialog, and therefore clicking the button won't result in a focus change. This means that
        // the FocusMonitor won't detect any origin change, and will always output `program`.
        _closeDialogVia(this.dialogRef, event.screenX === 0 && event.screenY === 0 ? 'keyboard' : 'mouse', this.dialogResult);
    }
}
MatDialogClose.ɵfac = function MatDialogClose_Factory(t) { return new (t || MatDialogClose)(core/* ɵɵdirectiveInject */.Y36(MatDialogRef, 8), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(MatDialog)); };
MatDialogClose.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatDialogClose, selectors: [["", "mat-dialog-close", ""], ["", "matDialogClose", ""]], hostVars: 2, hostBindings: function MatDialogClose_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("click", function MatDialogClose_click_HostBindingHandler($event) { return ctx._onButtonClick($event); });
    } if (rf & 2) {
        core/* ɵɵattribute */.uIk("aria-label", ctx.ariaLabel || null)("type", ctx.type);
    } }, inputs: { type: "type", dialogResult: ["mat-dialog-close", "dialogResult"], ariaLabel: ["aria-label", "ariaLabel"], _matDialogClose: ["matDialogClose", "_matDialogClose"] }, exportAs: ["matDialogClose"], features: [core/* ɵɵNgOnChangesFeature */.TTD] });
MatDialogClose.ctorParameters = () => [
    { type: MatDialogRef, decorators: [{ type: core/* Optional */.FiY }] },
    { type: core/* ElementRef */.SBq },
    { type: MatDialog }
];
MatDialogClose.propDecorators = {
    ariaLabel: [{ type: core/* Input */.IIB, args: ['aria-label',] }],
    type: [{ type: core/* Input */.IIB }],
    dialogResult: [{ type: core/* Input */.IIB, args: ['mat-dialog-close',] }],
    _matDialogClose: [{ type: core/* Input */.IIB, args: ['matDialogClose',] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogClose, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-dialog-close], [matDialogClose]',
                exportAs: 'matDialogClose',
                host: {
                    '(click)': '_onButtonClick($event)',
                    '[attr.aria-label]': 'ariaLabel || null',
                    '[attr.type]': 'type'
                }
            }]
    }], function () { return [{ type: MatDialogRef, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: core/* ElementRef */.SBq }, { type: MatDialog }]; }, { type: [{
            type: core/* Input */.IIB
        }], dialogResult: [{
            type: core/* Input */.IIB,
            args: ['mat-dialog-close']
        }], ariaLabel: [{
            type: core/* Input */.IIB,
            args: ['aria-label']
        }], _matDialogClose: [{
            type: core/* Input */.IIB,
            args: ['matDialogClose']
        }] }); })();
/**
 * Title of a dialog element. Stays fixed to the top of the dialog when scrolling.
 */
class MatDialogTitle {
    constructor(
    // The dialog title directive is always used in combination with a `MatDialogRef`.
    // tslint:disable-next-line: lightweight-tokens
    _dialogRef, _elementRef, _dialog) {
        this._dialogRef = _dialogRef;
        this._elementRef = _elementRef;
        this._dialog = _dialog;
        /** Unique id for the dialog title. If none is supplied, it will be auto-generated. */
        this.id = `mat-dialog-title-${dialogElementUid++}`;
    }
    ngOnInit() {
        if (!this._dialogRef) {
            this._dialogRef = getClosestDialog(this._elementRef, this._dialog.openDialogs);
        }
        if (this._dialogRef) {
            Promise.resolve().then(() => {
                const container = this._dialogRef._containerInstance;
                if (container && !container._ariaLabelledBy) {
                    container._ariaLabelledBy = this.id;
                }
            });
        }
    }
}
MatDialogTitle.ɵfac = function MatDialogTitle_Factory(t) { return new (t || MatDialogTitle)(core/* ɵɵdirectiveInject */.Y36(MatDialogRef, 8), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(MatDialog)); };
MatDialogTitle.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatDialogTitle, selectors: [["", "mat-dialog-title", ""], ["", "matDialogTitle", ""]], hostAttrs: [1, "mat-dialog-title"], hostVars: 1, hostBindings: function MatDialogTitle_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵhostProperty */.Ikx("id", ctx.id);
    } }, inputs: { id: "id" }, exportAs: ["matDialogTitle"] });
MatDialogTitle.ctorParameters = () => [
    { type: MatDialogRef, decorators: [{ type: core/* Optional */.FiY }] },
    { type: core/* ElementRef */.SBq },
    { type: MatDialog }
];
MatDialogTitle.propDecorators = {
    id: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogTitle, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-dialog-title], [matDialogTitle]',
                exportAs: 'matDialogTitle',
                host: {
                    'class': 'mat-dialog-title',
                    '[id]': 'id'
                }
            }]
    }], function () { return [{ type: MatDialogRef, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: core/* ElementRef */.SBq }, { type: MatDialog }]; }, { id: [{
            type: core/* Input */.IIB
        }] }); })();
/**
 * Scrollable content container of a dialog.
 */
class MatDialogContent {
}
MatDialogContent.ɵfac = function MatDialogContent_Factory(t) { return new (t || MatDialogContent)(); };
MatDialogContent.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatDialogContent, selectors: [["", "mat-dialog-content", ""], ["mat-dialog-content"], ["", "matDialogContent", ""]], hostAttrs: [1, "mat-dialog-content"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogContent, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: `[mat-dialog-content], mat-dialog-content, [matDialogContent]`,
                host: { 'class': 'mat-dialog-content' }
            }]
    }], null, null); })();
/**
 * Container for the bottom action buttons in a dialog.
 * Stays fixed to the bottom when scrolling.
 */
class MatDialogActions {
}
MatDialogActions.ɵfac = function MatDialogActions_Factory(t) { return new (t || MatDialogActions)(); };
MatDialogActions.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatDialogActions, selectors: [["", "mat-dialog-actions", ""], ["mat-dialog-actions"], ["", "matDialogActions", ""]], hostAttrs: [1, "mat-dialog-actions"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogActions, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: `[mat-dialog-actions], mat-dialog-actions, [matDialogActions]`,
                host: { 'class': 'mat-dialog-actions' }
            }]
    }], null, null); })();
/**
 * Finds the closest MatDialogRef to an element by looking at the DOM.
 * @param element Element relative to which to look for a dialog.
 * @param openDialogs References to the currently-open dialogs.
 */
function getClosestDialog(element, openDialogs) {
    let parent = element.nativeElement.parentElement;
    while (parent && !parent.classList.contains('mat-dialog-container')) {
        parent = parent.parentElement;
    }
    return parent ? openDialogs.find(dialog => dialog.id === parent.id) : null;
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatDialogModule {
}
MatDialogModule.ɵfac = function MatDialogModule_Factory(t) { return new (t || MatDialogModule)(); };
MatDialogModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatDialogModule });
MatDialogModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [
        MatDialog,
        MAT_DIALOG_SCROLL_STRATEGY_PROVIDER,
    ], imports: [[
            overlay/* OverlayModule */.U8,
            portal/* PortalModule */.eL,
            fesm2015_core/* MatCommonModule */.BQ,
        ], fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatDialogModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [
                    overlay/* OverlayModule */.U8,
                    portal/* PortalModule */.eL,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                exports: [
                    MatDialogContainer,
                    MatDialogClose,
                    MatDialogTitle,
                    MatDialogContent,
                    MatDialogActions,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                declarations: [
                    MatDialogContainer,
                    MatDialogClose,
                    MatDialogTitle,
                    MatDialogActions,
                    MatDialogContent,
                ],
                providers: [
                    MatDialog,
                    MAT_DIALOG_SCROLL_STRATEGY_PROVIDER,
                ],
                entryComponents: [MatDialogContainer]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatDialogModule, { declarations: function () { return [MatDialogContainer, MatDialogClose, MatDialogTitle, MatDialogActions, MatDialogContent]; }, imports: function () { return [overlay/* OverlayModule */.U8,
        portal/* PortalModule */.eL,
        fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [MatDialogContainer, MatDialogClose, MatDialogTitle, MatDialogContent, MatDialogActions, fesm2015_core/* MatCommonModule */.BQ]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=dialog.js.map
;// CONCATENATED MODULE: ./src/app/shared/dialog/info-dialog.component.ts


class InfoDialog {
}
InfoDialog.ɵfac = function InfoDialog_Factory(t) { return new (t || InfoDialog)(); };
InfoDialog.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: InfoDialog, selectors: [["info-dialog"]], decls: 6, vars: 0, consts: [["mat-dialog-content", "", 1, "overflow-hidden"], [1, "teko-text"], ["href", "https://sportsurge.net/", "target", "_blank"]], template: function InfoDialog_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "span", 1);
        core/* ɵɵtext */._uU(2, "The livestream links are retrieved from ");
        core/* ɵɵelementStart */.TgZ(3, "a", 2);
        core/* ɵɵtext */._uU(4, "sportsurge.net");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(5, "br");
        core/* ɵɵelementEnd */.qZA();
    } }, directives: [MatDialogContent], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/videostreams/videostreams.component.ts














function VideostreamsComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 16);
    core/* ɵɵelementStart */.TgZ(2, "div", 17);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 18);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 19);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r4 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r4.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r4.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r4.comment);
} }
function VideostreamsComponent_ng_template_23_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "a", 20);
    core/* ɵɵelement */._UZ(2, "img", 21);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function VideostreamsComponent_ng_template_25_div_0_div_1_tr_16_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr", 30);
    core/* ɵɵelementStart */.TgZ(1, "th", 31);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵelementStart */.TgZ(4, "a", 32);
    core/* ɵɵelementStart */.TgZ(5, "div");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵelementStart */.TgZ(8, "a", 32);
    core/* ɵɵelementStart */.TgZ(9, "div");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const stream_r10 = ctx.$implicit;
    const i_r11 = ctx.index;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(i_r11 + 1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", stream_r10.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", stream_r10.name, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", stream_r10.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", stream_r10.url, " ");
} }
function VideostreamsComponent_ng_template_25_div_0_div_1_Template(rf, ctx) { if (rf & 1) {
    const _r13 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 23);
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 24);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementStart */.TgZ(6, "button", 25);
    core/* ɵɵlistener */.NdJ("click", function VideostreamsComponent_ng_template_25_div_0_div_1_Template_button_click_6_listener() { core/* ɵɵrestoreView */.CHM(_r13); const ctx_r12 = core/* ɵɵnextContext */.oxw(3); return ctx_r12.openInfo(); });
    core/* ɵɵelement */._UZ(7, "i", 26);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "tr");
    core/* ɵɵelementStart */.TgZ(9, "th", 27);
    core/* ɵɵtext */._uU(10, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 28);
    core/* ɵɵtext */._uU(12, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 28);
    core/* ɵɵtext */._uU(14, "URL ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "tbody");
    core/* ɵɵtemplate */.YNc(16, VideostreamsComponent_ng_template_25_div_0_div_1_tr_16_Template, 11, 5, "tr", 29);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const event_r8 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵtextInterpolate1 */.hij("", event_r8.name, " ");
    core/* ɵɵadvance */.xp6(11);
    core/* ɵɵproperty */.Q6J("ngForOf", event_r8.streams);
} }
function VideostreamsComponent_ng_template_25_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵtemplate */.YNc(1, VideostreamsComponent_ng_template_25_div_0_div_1_Template, 17, 2, "div", 8);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r5.events);
} }
function VideostreamsComponent_ng_template_25_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 33);
    core/* ɵɵelementStart */.TgZ(2, "div", 34);
    core/* ɵɵelementStart */.TgZ(3, "div", 35);
    core/* ɵɵelementStart */.TgZ(4, "div", 36);
    core/* ɵɵelementStart */.TgZ(5, "p", 35);
    core/* ɵɵtext */._uU(6, "No streams currently. ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function VideostreamsComponent_ng_template_25_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, VideostreamsComponent_ng_template_25_div_0_Template, 2, 1, "div", 22);
    core/* ɵɵtemplate */.YNc(1, VideostreamsComponent_ng_template_25_div_1_Template, 7, 0, "div", 22);
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.events !== undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.events.length == 0);
} }
class VideostreamsComponent {
    constructor(restService, utilityService, dialog) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.dialog = dialog;
        this.utilityService.setTitle("Videostreams");
        let observableEvents = this.restService.getSportSurge();
        observableEvents.subscribe({
            next: data => {
                this.events = data;
                return data;
            }
        });
        this.utilityService.reloadPosts(5);
    }
    openInfo() {
        let dialogRef = this.dialog.open(InfoDialog);
    }
}
VideostreamsComponent.ɵfac = function VideostreamsComponent_Factory(t) { return new (t || VideostreamsComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(MatDialog)); };
VideostreamsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: VideostreamsComponent, selectors: [["videostreams-cmp"]], decls: 26, vars: 3, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], [1, "height-fill-screen"], ["label", "F1TV"], ["matTabContent", ""], ["label", "SportSurge"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], ["href", "https://f1tv.formula1.com", "target", "_blank", 2, "color", "black"], ["alt", "f1tv", "src", "https://ott-img.formula1.com/subscription/ProImagesNoBG/live-nocut.png"], [4, "ngIf"], [1, "table", "table-striped"], ["colspan", "4", 2, "background-color", "#4db1bc", "text-align", "center"], [1, "btn", "btn-outline-info", "btn-round", "btn-icon", "right-header-button", 3, "click"], [1, "fa", "fa-info"], ["scope", "col"], ["scope", "col", 2, "text-align", "center"], ["class", "hover-effects-bold-row", 4, "ngFor", "ngForOf"], [1, "hover-effects-bold-row"], ["scope", "row"], ["target", "_blank", 2, "color", "black", 3, "href"], [1, "div-padded-right-2", "div-padded-top-1", "col-lg-5", "col-md-6", "col-sm-6"], [1, "card", "card-stats"], [1, "card-body"], [1, "m-3", "col-7", "col-md-9"]], template: function VideostreamsComponent_Template(rf, ctx) { if (rf & 1) {
        const _r14 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function VideostreamsComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function VideostreamsComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function VideostreamsComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(5); });
        core/* ɵɵtext */._uU(10, "Post");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function VideostreamsComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(5); });
        core/* ɵɵtext */._uU(12, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, VideostreamsComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function VideostreamsComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r14); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function VideostreamsComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r14); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 12);
        core/* ɵɵelementStart */.TgZ(21, "mat-tab-group");
        core/* ɵɵelementStart */.TgZ(22, "mat-tab", 13);
        core/* ɵɵtemplate */.YNc(23, VideostreamsComponent_ng_template_23_Template, 3, 0, "ng-template", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "mat-tab", 15);
        core/* ɵɵtemplate */.YNc(25, VideostreamsComponent_ng_template_25_Template, 2, 2, "ng-template", 14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, common/* NgIf */.O5], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/model/livetiming/ComparisonTable.ts
class ComparisonTableRow {
    constructor(driver1, driver2, index) {
        this.driver1 = driver1.lapByLapData.lapTimesY[index];
        this.driver2 = driver2.lapByLapData.lapTimesY[index];
        this.difference = driver1.lapByLapData.lapTimesYms[index] - driver2.lapByLapData.lapTimesYms[index];
        if (!isNaN(this.difference)) {
            this.differenceString = this.difference / 1000 + ' s';
        }
    }
}
class ComparisonTable {
    constructor() {
        this.rows = [];
    }
}

;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/sort.js










/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** @docs-private */




const sort_c0 = ["mat-sort-header", ""];
function MatSortHeader_div_3_Template(rf, ctx) { if (rf & 1) {
    const _r2 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵlistener */.NdJ("@arrowPosition.start", function MatSortHeader_div_3_Template_div_animation_arrowPosition_start_0_listener() { core/* ɵɵrestoreView */.CHM(_r2); const ctx_r1 = core/* ɵɵnextContext */.oxw(); return ctx_r1._disableViewStateAnimation = true; })("@arrowPosition.done", function MatSortHeader_div_3_Template_div_animation_arrowPosition_done_0_listener() { core/* ɵɵrestoreView */.CHM(_r2); const ctx_r3 = core/* ɵɵnextContext */.oxw(); return ctx_r3._disableViewStateAnimation = false; });
    core/* ɵɵelement */._UZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 5);
    core/* ɵɵelement */._UZ(3, "div", 6);
    core/* ɵɵelement */._UZ(4, "div", 7);
    core/* ɵɵelement */._UZ(5, "div", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r0 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("@arrowOpacity", ctx_r0._getArrowViewState())("@arrowPosition", ctx_r0._getArrowViewState())("@allowChildren", ctx_r0._getArrowDirectionState());
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("@indicator", ctx_r0._getArrowDirectionState());
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("@leftPointer", ctx_r0._getArrowDirectionState());
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("@rightPointer", ctx_r0._getArrowDirectionState());
} }
const sort_c1 = ["*"];
function getSortDuplicateSortableIdError(id) {
    return Error(`Cannot have two MatSortables with the same id (${id}).`);
}
/** @docs-private */
function getSortHeaderNotContainedWithinSortError() {
    return Error(`MatSortHeader must be placed within a parent element with the MatSort directive.`);
}
/** @docs-private */
function getSortHeaderMissingIdError() {
    return Error(`MatSortHeader must be provided with a unique id.`);
}
/** @docs-private */
function getSortInvalidDirectionError(direction) {
    return Error(`${direction} is not a valid sort direction ('asc' or 'desc').`);
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/** Injection token to be used to override the default options for `mat-sort`. */
const MAT_SORT_DEFAULT_OPTIONS = new core/* InjectionToken */.OlP('MAT_SORT_DEFAULT_OPTIONS');
// Boilerplate for applying mixins to MatSort.
/** @docs-private */
const _MatSortBase = (0,fesm2015_core/* mixinInitialized */.dB)((0,fesm2015_core/* mixinDisabled */.Id)(class {
}));
/** Container for MatSortables to manage the sort state and provide default sort parameters. */
class MatSort extends _MatSortBase {
    constructor(_defaultOptions) {
        super();
        this._defaultOptions = _defaultOptions;
        /** Collection of all registered sortables that this directive manages. */
        this.sortables = new Map();
        /** Used to notify any child components listening to state changes. */
        this._stateChanges = new Subject/* Subject */.xQ();
        /**
         * The direction to set when an MatSortable is initially sorted.
         * May be overriden by the MatSortable's sort start.
         */
        this.start = 'asc';
        this._direction = '';
        /** Event emitted when the user changes either the active sort or sort direction. */
        this.sortChange = new core/* EventEmitter */.vpe();
    }
    /** The sort direction of the currently active MatSortable. */
    get direction() { return this._direction; }
    set direction(direction) {
        if (direction && direction !== 'asc' && direction !== 'desc' &&
            (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getSortInvalidDirectionError(direction);
        }
        this._direction = direction;
    }
    /**
     * Whether to disable the user from clearing the sort by finishing the sort direction cycle.
     * May be overriden by the MatSortable's disable clear input.
     */
    get disableClear() { return this._disableClear; }
    set disableClear(v) { this._disableClear = (0,coercion/* coerceBooleanProperty */.Ig)(v); }
    /**
     * Register function to be used by the contained MatSortables. Adds the MatSortable to the
     * collection of MatSortables.
     */
    register(sortable) {
        if (typeof ngDevMode === 'undefined' || ngDevMode) {
            if (!sortable.id) {
                throw getSortHeaderMissingIdError();
            }
            if (this.sortables.has(sortable.id)) {
                throw getSortDuplicateSortableIdError(sortable.id);
            }
        }
        this.sortables.set(sortable.id, sortable);
    }
    /**
     * Unregister function to be used by the contained MatSortables. Removes the MatSortable from the
     * collection of contained MatSortables.
     */
    deregister(sortable) {
        this.sortables.delete(sortable.id);
    }
    /** Sets the active sort id and determines the new sort direction. */
    sort(sortable) {
        if (this.active != sortable.id) {
            this.active = sortable.id;
            this.direction = sortable.start ? sortable.start : this.start;
        }
        else {
            this.direction = this.getNextSortDirection(sortable);
        }
        this.sortChange.emit({ active: this.active, direction: this.direction });
    }
    /** Returns the next sort direction of the active sortable, checking for potential overrides. */
    getNextSortDirection(sortable) {
        var _a, _b, _c;
        if (!sortable) {
            return '';
        }
        // Get the sort direction cycle with the potential sortable overrides.
        const disableClear = (_b = (_a = sortable === null || sortable === void 0 ? void 0 : sortable.disableClear) !== null && _a !== void 0 ? _a : this.disableClear) !== null && _b !== void 0 ? _b : !!((_c = this._defaultOptions) === null || _c === void 0 ? void 0 : _c.disableClear);
        let sortDirectionCycle = getSortDirectionCycle(sortable.start || this.start, disableClear);
        // Get and return the next direction in the cycle
        let nextDirectionIndex = sortDirectionCycle.indexOf(this.direction) + 1;
        if (nextDirectionIndex >= sortDirectionCycle.length) {
            nextDirectionIndex = 0;
        }
        return sortDirectionCycle[nextDirectionIndex];
    }
    ngOnInit() {
        this._markInitialized();
    }
    ngOnChanges() {
        this._stateChanges.next();
    }
    ngOnDestroy() {
        this._stateChanges.complete();
    }
}
MatSort.ɵfac = function MatSort_Factory(t) { return new (t || MatSort)(core/* ɵɵdirectiveInject */.Y36(MAT_SORT_DEFAULT_OPTIONS, 8)); };
MatSort.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatSort, selectors: [["", "matSort", ""]], hostAttrs: [1, "mat-sort"], inputs: { disabled: ["matSortDisabled", "disabled"], start: ["matSortStart", "start"], direction: ["matSortDirection", "direction"], disableClear: ["matSortDisableClear", "disableClear"], active: ["matSortActive", "active"] }, outputs: { sortChange: "matSortChange" }, exportAs: ["matSort"], features: [core/* ɵɵInheritDefinitionFeature */.qOj, core/* ɵɵNgOnChangesFeature */.TTD] });
MatSort.ctorParameters = () => [
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_SORT_DEFAULT_OPTIONS,] }] }
];
MatSort.propDecorators = {
    active: [{ type: core/* Input */.IIB, args: ['matSortActive',] }],
    start: [{ type: core/* Input */.IIB, args: ['matSortStart',] }],
    direction: [{ type: core/* Input */.IIB, args: ['matSortDirection',] }],
    disableClear: [{ type: core/* Input */.IIB, args: ['matSortDisableClear',] }],
    sortChange: [{ type: core/* Output */.r_U, args: ['matSortChange',] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSort, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[matSort]',
                exportAs: 'matSort',
                host: { 'class': 'mat-sort' },
                inputs: ['disabled: matSortDisabled']
            }]
    }], function () { return [{ type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_SORT_DEFAULT_OPTIONS]
            }] }]; }, { start: [{
            type: core/* Input */.IIB,
            args: ['matSortStart']
        }], sortChange: [{
            type: core/* Output */.r_U,
            args: ['matSortChange']
        }], direction: [{
            type: core/* Input */.IIB,
            args: ['matSortDirection']
        }], disableClear: [{
            type: core/* Input */.IIB,
            args: ['matSortDisableClear']
        }], active: [{
            type: core/* Input */.IIB,
            args: ['matSortActive']
        }] }); })();
/** Returns the sort direction cycle to use given the provided parameters of order and clear. */
function getSortDirectionCycle(start, disableClear) {
    let sortOrder = ['asc', 'desc'];
    if (start == 'desc') {
        sortOrder.reverse();
    }
    if (!disableClear) {
        sortOrder.push('');
    }
    return sortOrder;
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
const SORT_ANIMATION_TRANSITION = fesm2015_core/* AnimationDurations.ENTERING */.mZ.ENTERING + ' ' +
    fesm2015_core/* AnimationCurves.STANDARD_CURVE */.yN.STANDARD_CURVE;
/**
 * Animations used by MatSort.
 * @docs-private
 */
const matSortAnimations = {
    /** Animation that moves the sort indicator. */
    indicator: (0,animations/* trigger */.X$)('indicator', [
        (0,animations/* state */.SB)('active-asc, asc', (0,animations/* style */.oB)({ transform: 'translateY(0px)' })),
        // 10px is the height of the sort indicator, minus the width of the pointers
        (0,animations/* state */.SB)('active-desc, desc', (0,animations/* style */.oB)({ transform: 'translateY(10px)' })),
        (0,animations/* transition */.eR)('active-asc <=> active-desc', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION))
    ]),
    /** Animation that rotates the left pointer of the indicator based on the sorting direction. */
    leftPointer: (0,animations/* trigger */.X$)('leftPointer', [
        (0,animations/* state */.SB)('active-asc, asc', (0,animations/* style */.oB)({ transform: 'rotate(-45deg)' })),
        (0,animations/* state */.SB)('active-desc, desc', (0,animations/* style */.oB)({ transform: 'rotate(45deg)' })),
        (0,animations/* transition */.eR)('active-asc <=> active-desc', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION))
    ]),
    /** Animation that rotates the right pointer of the indicator based on the sorting direction. */
    rightPointer: (0,animations/* trigger */.X$)('rightPointer', [
        (0,animations/* state */.SB)('active-asc, asc', (0,animations/* style */.oB)({ transform: 'rotate(45deg)' })),
        (0,animations/* state */.SB)('active-desc, desc', (0,animations/* style */.oB)({ transform: 'rotate(-45deg)' })),
        (0,animations/* transition */.eR)('active-asc <=> active-desc', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION))
    ]),
    /** Animation that controls the arrow opacity. */
    arrowOpacity: (0,animations/* trigger */.X$)('arrowOpacity', [
        (0,animations/* state */.SB)('desc-to-active, asc-to-active, active', (0,animations/* style */.oB)({ opacity: 1 })),
        (0,animations/* state */.SB)('desc-to-hint, asc-to-hint, hint', (0,animations/* style */.oB)({ opacity: .54 })),
        (0,animations/* state */.SB)('hint-to-desc, active-to-desc, desc, hint-to-asc, active-to-asc, asc, void', (0,animations/* style */.oB)({ opacity: 0 })),
        // Transition between all states except for immediate transitions
        (0,animations/* transition */.eR)('* => asc, * => desc, * => active, * => hint, * => void', (0,animations/* animate */.jt)('0ms')),
        (0,animations/* transition */.eR)('* <=> *', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION)),
    ]),
    /**
     * Animation for the translation of the arrow as a whole. States are separated into two
     * groups: ones with animations and others that are immediate. Immediate states are asc, desc,
     * peek, and active. The other states define a specific animation (source-to-destination)
     * and are determined as a function of their prev user-perceived state and what the next state
     * should be.
     */
    arrowPosition: (0,animations/* trigger */.X$)('arrowPosition', [
        // Hidden Above => Hint Center
        (0,animations/* transition */.eR)('* => desc-to-hint, * => desc-to-active', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION, (0,animations/* keyframes */.F4)([
            (0,animations/* style */.oB)({ transform: 'translateY(-25%)' }),
            (0,animations/* style */.oB)({ transform: 'translateY(0)' })
        ]))),
        // Hint Center => Hidden Below
        (0,animations/* transition */.eR)('* => hint-to-desc, * => active-to-desc', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION, (0,animations/* keyframes */.F4)([
            (0,animations/* style */.oB)({ transform: 'translateY(0)' }),
            (0,animations/* style */.oB)({ transform: 'translateY(25%)' })
        ]))),
        // Hidden Below => Hint Center
        (0,animations/* transition */.eR)('* => asc-to-hint, * => asc-to-active', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION, (0,animations/* keyframes */.F4)([
            (0,animations/* style */.oB)({ transform: 'translateY(25%)' }),
            (0,animations/* style */.oB)({ transform: 'translateY(0)' })
        ]))),
        // Hint Center => Hidden Above
        (0,animations/* transition */.eR)('* => hint-to-asc, * => active-to-asc', (0,animations/* animate */.jt)(SORT_ANIMATION_TRANSITION, (0,animations/* keyframes */.F4)([
            (0,animations/* style */.oB)({ transform: 'translateY(0)' }),
            (0,animations/* style */.oB)({ transform: 'translateY(-25%)' })
        ]))),
        (0,animations/* state */.SB)('desc-to-hint, asc-to-hint, hint, desc-to-active, asc-to-active, active', (0,animations/* style */.oB)({ transform: 'translateY(0)' })),
        (0,animations/* state */.SB)('hint-to-desc, active-to-desc, desc', (0,animations/* style */.oB)({ transform: 'translateY(-25%)' })),
        (0,animations/* state */.SB)('hint-to-asc, active-to-asc, asc', (0,animations/* style */.oB)({ transform: 'translateY(25%)' })),
    ]),
    /** Necessary trigger that calls animate on children animations. */
    allowChildren: (0,animations/* trigger */.X$)('allowChildren', [
        (0,animations/* transition */.eR)('* <=> *', [
            (0,animations/* query */.IO)('@*', (0,animations/* animateChild */.pV)(), { optional: true })
        ])
    ]),
};

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * To modify the labels and text displayed, create a new instance of MatSortHeaderIntl and
 * include it in a custom provider.
 * @deprecated No longer being used. To be removed.
 * @breaking-change 13.0.0
 */
class MatSortHeaderIntl {
    constructor() {
        /**
         * Stream that emits whenever the labels here are changed. Use this to notify
         * components if the labels have changed after initialization.
         */
        this.changes = new Subject/* Subject */.xQ();
    }
}
MatSortHeaderIntl.ɵfac = function MatSortHeaderIntl_Factory(t) { return new (t || MatSortHeaderIntl)(); };
MatSortHeaderIntl.ɵprov = core/* ɵɵdefineInjectable */.Yz7({ factory: function MatSortHeaderIntl_Factory() { return new MatSortHeaderIntl(); }, token: MatSortHeaderIntl, providedIn: "root" });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSortHeaderIntl, [{
        type: core/* Injectable */.GSi,
        args: [{ providedIn: 'root' }]
    }], function () { return []; }, null); })();
/** @docs-private */
function MAT_SORT_HEADER_INTL_PROVIDER_FACTORY(parentIntl) {
    return parentIntl || new MatSortHeaderIntl();
}
/** @docs-private */
const MAT_SORT_HEADER_INTL_PROVIDER = {
    // If there is already an MatSortHeaderIntl available, use that. Otherwise, provide a new one.
    provide: MatSortHeaderIntl,
    deps: [[new core/* Optional */.FiY(), new core/* SkipSelf */.tp0(), MatSortHeaderIntl]],
    useFactory: MAT_SORT_HEADER_INTL_PROVIDER_FACTORY
};

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
// Boilerplate for applying mixins to the sort header.
/** @docs-private */
const _MatSortHeaderBase = (0,fesm2015_core/* mixinDisabled */.Id)(class {
});
/**
 * Applies sorting behavior (click to change sort) and styles to an element, including an
 * arrow to display the current sort direction.
 *
 * Must be provided with an id and contained within a parent MatSort directive.
 *
 * If used on header cells in a CdkTable, it will automatically default its id from its containing
 * column definition.
 */
class MatSortHeader extends _MatSortHeaderBase {
    constructor(
    /**
     * @deprecated `_intl` parameter isn't being used anymore and it'll be removed.
     * @breaking-change 13.0.0
     */
    _intl, _changeDetectorRef, 
    // `MatSort` is not optionally injected, but just asserted manually w/ better error.
    // tslint:disable-next-line: lightweight-tokens
    _sort, _columnDef, _focusMonitor, _elementRef) {
        // Note that we use a string token for the `_columnDef`, because the value is provided both by
        // `material/table` and `cdk/table` and we can't have the CDK depending on Material,
        // and we want to avoid having the sort header depending on the CDK table because
        // of this single reference.
        super();
        this._intl = _intl;
        this._changeDetectorRef = _changeDetectorRef;
        this._sort = _sort;
        this._columnDef = _columnDef;
        this._focusMonitor = _focusMonitor;
        this._elementRef = _elementRef;
        /**
         * Flag set to true when the indicator should be displayed while the sort is not active. Used to
         * provide an affordance that the header is sortable by showing on focus and hover.
         */
        this._showIndicatorHint = false;
        /**
         * The view transition state of the arrow (translation/ opacity) - indicates its `from` and `to`
         * position through the animation. If animations are currently disabled, the fromState is removed
         * so that there is no animation displayed.
         */
        this._viewState = {};
        /** The direction the arrow should be facing according to the current state. */
        this._arrowDirection = '';
        /**
         * Whether the view state animation should show the transition between the `from` and `to` states.
         */
        this._disableViewStateAnimation = false;
        /** Sets the position of the arrow that displays when sorted. */
        this.arrowPosition = 'after';
        if (!_sort && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getSortHeaderNotContainedWithinSortError();
        }
        this._handleStateChanges();
    }
    /** Overrides the disable clear value of the containing MatSort for this MatSortable. */
    get disableClear() { return this._disableClear; }
    set disableClear(v) { this._disableClear = (0,coercion/* coerceBooleanProperty */.Ig)(v); }
    ngOnInit() {
        if (!this.id && this._columnDef) {
            this.id = this._columnDef.name;
        }
        // Initialize the direction of the arrow and set the view state to be immediately that state.
        this._updateArrowDirection();
        this._setAnimationTransitionState({ toState: this._isSorted() ? 'active' : this._arrowDirection });
        this._sort.register(this);
    }
    ngAfterViewInit() {
        // We use the focus monitor because we also want to style
        // things differently based on the focus origin.
        this._focusMonitor.monitor(this._elementRef, true).subscribe(origin => {
            const newState = !!origin;
            if (newState !== this._showIndicatorHint) {
                this._setIndicatorHintVisible(newState);
                this._changeDetectorRef.markForCheck();
            }
        });
    }
    ngOnDestroy() {
        this._focusMonitor.stopMonitoring(this._elementRef);
        this._sort.deregister(this);
        this._rerenderSubscription.unsubscribe();
    }
    /**
     * Sets the "hint" state such that the arrow will be semi-transparently displayed as a hint to the
     * user showing what the active sort will become. If set to false, the arrow will fade away.
     */
    _setIndicatorHintVisible(visible) {
        // No-op if the sort header is disabled - should not make the hint visible.
        if (this._isDisabled() && visible) {
            return;
        }
        this._showIndicatorHint = visible;
        if (!this._isSorted()) {
            this._updateArrowDirection();
            if (this._showIndicatorHint) {
                this._setAnimationTransitionState({ fromState: this._arrowDirection, toState: 'hint' });
            }
            else {
                this._setAnimationTransitionState({ fromState: 'hint', toState: this._arrowDirection });
            }
        }
    }
    /**
     * Sets the animation transition view state for the arrow's position and opacity. If the
     * `disableViewStateAnimation` flag is set to true, the `fromState` will be ignored so that
     * no animation appears.
     */
    _setAnimationTransitionState(viewState) {
        this._viewState = viewState || {};
        // If the animation for arrow position state (opacity/translation) should be disabled,
        // remove the fromState so that it jumps right to the toState.
        if (this._disableViewStateAnimation) {
            this._viewState = { toState: viewState.toState };
        }
    }
    /** Triggers the sort on this sort header and removes the indicator hint. */
    _toggleOnInteraction() {
        this._sort.sort(this);
        // Do not show the animation if the header was already shown in the right position.
        if (this._viewState.toState === 'hint' || this._viewState.toState === 'active') {
            this._disableViewStateAnimation = true;
        }
    }
    _handleClick() {
        if (!this._isDisabled()) {
            this._sort.sort(this);
        }
    }
    _handleKeydown(event) {
        if (!this._isDisabled() && (event.keyCode === keycodes/* SPACE */.L_ || event.keyCode === keycodes/* ENTER */.K5)) {
            event.preventDefault();
            this._toggleOnInteraction();
        }
    }
    /** Whether this MatSortHeader is currently sorted in either ascending or descending order. */
    _isSorted() {
        return this._sort.active == this.id &&
            (this._sort.direction === 'asc' || this._sort.direction === 'desc');
    }
    /** Returns the animation state for the arrow direction (indicator and pointers). */
    _getArrowDirectionState() {
        return `${this._isSorted() ? 'active-' : ''}${this._arrowDirection}`;
    }
    /** Returns the arrow position state (opacity, translation). */
    _getArrowViewState() {
        const fromState = this._viewState.fromState;
        return (fromState ? `${fromState}-to-` : '') + this._viewState.toState;
    }
    /**
     * Updates the direction the arrow should be pointing. If it is not sorted, the arrow should be
     * facing the start direction. Otherwise if it is sorted, the arrow should point in the currently
     * active sorted direction. The reason this is updated through a function is because the direction
     * should only be changed at specific times - when deactivated but the hint is displayed and when
     * the sort is active and the direction changes. Otherwise the arrow's direction should linger
     * in cases such as the sort becoming deactivated but we want to animate the arrow away while
     * preserving its direction, even though the next sort direction is actually different and should
     * only be changed once the arrow displays again (hint or activation).
     */
    _updateArrowDirection() {
        this._arrowDirection = this._isSorted() ?
            this._sort.direction :
            (this.start || this._sort.start);
    }
    _isDisabled() {
        return this._sort.disabled || this.disabled;
    }
    /**
     * Gets the aria-sort attribute that should be applied to this sort header. If this header
     * is not sorted, returns null so that the attribute is removed from the host element. Aria spec
     * says that the aria-sort property should only be present on one header at a time, so removing
     * ensures this is true.
     */
    _getAriaSortAttribute() {
        if (!this._isSorted()) {
            return 'none';
        }
        return this._sort.direction == 'asc' ? 'ascending' : 'descending';
    }
    /** Whether the arrow inside the sort header should be rendered. */
    _renderArrow() {
        return !this._isDisabled() || this._isSorted();
    }
    /** Handles changes in the sorting state. */
    _handleStateChanges() {
        this._rerenderSubscription =
            (0,merge/* merge */.T)(this._sort.sortChange, this._sort._stateChanges, this._intl.changes).subscribe(() => {
                if (this._isSorted()) {
                    this._updateArrowDirection();
                    // Do not show the animation if the header was already shown in the right position.
                    if (this._viewState.toState === 'hint' || this._viewState.toState === 'active') {
                        this._disableViewStateAnimation = true;
                    }
                    this._setAnimationTransitionState({ fromState: this._arrowDirection, toState: 'active' });
                    this._showIndicatorHint = false;
                }
                // If this header was recently active and now no longer sorted, animate away the arrow.
                if (!this._isSorted() && this._viewState && this._viewState.toState === 'active') {
                    this._disableViewStateAnimation = false;
                    this._setAnimationTransitionState({ fromState: 'active', toState: this._arrowDirection });
                }
                this._changeDetectorRef.markForCheck();
            });
    }
}
MatSortHeader.ɵfac = function MatSortHeader_Factory(t) { return new (t || MatSortHeader)(core/* ɵɵdirectiveInject */.Y36(MatSortHeaderIntl), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(MatSort, 8), core/* ɵɵdirectiveInject */.Y36('MAT_SORT_HEADER_COLUMN_DEF', 8), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusMonitor */.tE), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq)); };
MatSortHeader.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatSortHeader, selectors: [["", "mat-sort-header", ""]], hostAttrs: [1, "mat-sort-header"], hostVars: 3, hostBindings: function MatSortHeader_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("click", function MatSortHeader_click_HostBindingHandler() { return ctx._handleClick(); })("keydown", function MatSortHeader_keydown_HostBindingHandler($event) { return ctx._handleKeydown($event); })("mouseenter", function MatSortHeader_mouseenter_HostBindingHandler() { return ctx._setIndicatorHintVisible(true); })("mouseleave", function MatSortHeader_mouseleave_HostBindingHandler() { return ctx._setIndicatorHintVisible(false); });
    } if (rf & 2) {
        core/* ɵɵattribute */.uIk("aria-sort", ctx._getAriaSortAttribute());
        core/* ɵɵclassProp */.ekj("mat-sort-header-disabled", ctx._isDisabled());
    } }, inputs: { disabled: "disabled", arrowPosition: "arrowPosition", disableClear: "disableClear", id: ["mat-sort-header", "id"], start: "start" }, exportAs: ["matSortHeader"], features: [core/* ɵɵInheritDefinitionFeature */.qOj], attrs: sort_c0, ngContentSelectors: sort_c1, decls: 4, vars: 6, consts: [["role", "button", 1, "mat-sort-header-container", "mat-focus-indicator"], [1, "mat-sort-header-content"], ["class", "mat-sort-header-arrow", 4, "ngIf"], [1, "mat-sort-header-arrow"], [1, "mat-sort-header-stem"], [1, "mat-sort-header-indicator"], [1, "mat-sort-header-pointer-left"], [1, "mat-sort-header-pointer-right"], [1, "mat-sort-header-pointer-middle"]], template: function MatSortHeader_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t();
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1);
        core/* ɵɵprojection */.Hsn(2);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(3, MatSortHeader_div_3_Template, 6, 6, "div", 2);
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵclassProp */.ekj("mat-sort-header-sorted", ctx._isSorted())("mat-sort-header-position-before", ctx.arrowPosition == "before");
        core/* ɵɵattribute */.uIk("tabindex", ctx._isDisabled() ? null : 0);
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("ngIf", ctx._renderArrow());
    } }, directives: [common/* NgIf */.O5], styles: [".mat-sort-header-container{display:flex;cursor:pointer;align-items:center;letter-spacing:normal;outline:0}[mat-sort-header].cdk-keyboard-focused .mat-sort-header-container,[mat-sort-header].cdk-program-focused .mat-sort-header-container{border-bottom:solid 1px currentColor}.mat-sort-header-disabled .mat-sort-header-container{cursor:default}.mat-sort-header-content{text-align:center;display:flex;align-items:center}.mat-sort-header-position-before{flex-direction:row-reverse}.mat-sort-header-arrow{height:12px;width:12px;min-width:12px;position:relative;display:flex;opacity:0}.mat-sort-header-arrow,[dir=rtl] .mat-sort-header-position-before .mat-sort-header-arrow{margin:0 0 0 6px}.mat-sort-header-position-before .mat-sort-header-arrow,[dir=rtl] .mat-sort-header-arrow{margin:0 6px 0 0}.mat-sort-header-stem{background:currentColor;height:10px;width:2px;margin:auto;display:flex;align-items:center}.cdk-high-contrast-active .mat-sort-header-stem{width:0;border-left:solid 2px}.mat-sort-header-indicator{width:100%;height:2px;display:flex;align-items:center;position:absolute;top:0;left:0}.mat-sort-header-pointer-middle{margin:auto;height:2px;width:2px;background:currentColor;transform:rotate(45deg)}.cdk-high-contrast-active .mat-sort-header-pointer-middle{width:0;height:0;border-top:solid 2px;border-left:solid 2px}.mat-sort-header-pointer-left,.mat-sort-header-pointer-right{background:currentColor;width:6px;height:2px;position:absolute;top:0}.cdk-high-contrast-active .mat-sort-header-pointer-left,.cdk-high-contrast-active .mat-sort-header-pointer-right{width:0;height:0;border-left:solid 6px;border-top:solid 2px}.mat-sort-header-pointer-left{transform-origin:right;left:0}.mat-sort-header-pointer-right{transform-origin:left;right:0}\n"], encapsulation: 2, data: { animation: [
            matSortAnimations.indicator,
            matSortAnimations.leftPointer,
            matSortAnimations.rightPointer,
            matSortAnimations.arrowOpacity,
            matSortAnimations.arrowPosition,
            matSortAnimations.allowChildren,
        ] }, changeDetection: 0 });
MatSortHeader.ctorParameters = () => [
    { type: MatSortHeaderIntl },
    { type: core/* ChangeDetectorRef */.sBO },
    { type: MatSort, decorators: [{ type: core/* Optional */.FiY }] },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: ['MAT_SORT_HEADER_COLUMN_DEF',] }, { type: core/* Optional */.FiY }] },
    { type: a11y/* FocusMonitor */.tE },
    { type: core/* ElementRef */.SBq }
];
MatSortHeader.propDecorators = {
    id: [{ type: core/* Input */.IIB, args: ['mat-sort-header',] }],
    arrowPosition: [{ type: core/* Input */.IIB }],
    start: [{ type: core/* Input */.IIB }],
    disableClear: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSortHeader, [{
        type: core/* Component */.wA2,
        args: [{
                selector: '[mat-sort-header]',
                exportAs: 'matSortHeader',
                template: "<!--\n  We set the `tabindex` on an element inside the table header, rather than the header itself,\n  because of a bug in NVDA where having a `tabindex` on a `th` breaks keyboard navigation in the\n  table (see https://github.com/nvaccess/nvda/issues/7718). This allows for the header to both\n  be focusable, and have screen readers read out its `aria-sort` state. We prefer this approach\n  over having a button with an `aria-label` inside the header, because the button's `aria-label`\n  will be read out as the user is navigating the table's cell (see #13012).\n\n  The approach is based off of: https://dequeuniversity.com/library/aria/tables/sf-sortable-grid\n-->\n<div class=\"mat-sort-header-container mat-focus-indicator\"\n     [class.mat-sort-header-sorted]=\"_isSorted()\"\n     [class.mat-sort-header-position-before]=\"arrowPosition == 'before'\"\n     [attr.tabindex]=\"_isDisabled() ? null : 0\"\n     role=\"button\">\n\n  <!--\n    TODO(crisbeto): this div isn't strictly necessary, but we have to keep it due to a large\n    number of screenshot diff failures. It should be removed eventually. Note that the difference\n    isn't visible with a shorter header, but once it breaks up into multiple lines, this element\n    causes it to be center-aligned, whereas removing it will keep the text to the left.\n  -->\n  <div class=\"mat-sort-header-content\">\n    <ng-content></ng-content>\n  </div>\n\n  <!-- Disable animations while a current animation is running -->\n  <div class=\"mat-sort-header-arrow\"\n       *ngIf=\"_renderArrow()\"\n       [@arrowOpacity]=\"_getArrowViewState()\"\n       [@arrowPosition]=\"_getArrowViewState()\"\n       [@allowChildren]=\"_getArrowDirectionState()\"\n       (@arrowPosition.start)=\"_disableViewStateAnimation = true\"\n       (@arrowPosition.done)=\"_disableViewStateAnimation = false\">\n    <div class=\"mat-sort-header-stem\"></div>\n    <div class=\"mat-sort-header-indicator\" [@indicator]=\"_getArrowDirectionState()\">\n      <div class=\"mat-sort-header-pointer-left\" [@leftPointer]=\"_getArrowDirectionState()\"></div>\n      <div class=\"mat-sort-header-pointer-right\" [@rightPointer]=\"_getArrowDirectionState()\"></div>\n      <div class=\"mat-sort-header-pointer-middle\"></div>\n    </div>\n  </div>\n</div>\n",
                host: {
                    'class': 'mat-sort-header',
                    '(click)': '_handleClick()',
                    '(keydown)': '_handleKeydown($event)',
                    '(mouseenter)': '_setIndicatorHintVisible(true)',
                    '(mouseleave)': '_setIndicatorHintVisible(false)',
                    '[attr.aria-sort]': '_getAriaSortAttribute()',
                    '[class.mat-sort-header-disabled]': '_isDisabled()'
                },
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                inputs: ['disabled'],
                animations: [
                    matSortAnimations.indicator,
                    matSortAnimations.leftPointer,
                    matSortAnimations.rightPointer,
                    matSortAnimations.arrowOpacity,
                    matSortAnimations.arrowPosition,
                    matSortAnimations.allowChildren,
                ],
                styles: [".mat-sort-header-container{display:flex;cursor:pointer;align-items:center;letter-spacing:normal;outline:0}[mat-sort-header].cdk-keyboard-focused .mat-sort-header-container,[mat-sort-header].cdk-program-focused .mat-sort-header-container{border-bottom:solid 1px currentColor}.mat-sort-header-disabled .mat-sort-header-container{cursor:default}.mat-sort-header-content{text-align:center;display:flex;align-items:center}.mat-sort-header-position-before{flex-direction:row-reverse}.mat-sort-header-arrow{height:12px;width:12px;min-width:12px;position:relative;display:flex;opacity:0}.mat-sort-header-arrow,[dir=rtl] .mat-sort-header-position-before .mat-sort-header-arrow{margin:0 0 0 6px}.mat-sort-header-position-before .mat-sort-header-arrow,[dir=rtl] .mat-sort-header-arrow{margin:0 6px 0 0}.mat-sort-header-stem{background:currentColor;height:10px;width:2px;margin:auto;display:flex;align-items:center}.cdk-high-contrast-active .mat-sort-header-stem{width:0;border-left:solid 2px}.mat-sort-header-indicator{width:100%;height:2px;display:flex;align-items:center;position:absolute;top:0;left:0}.mat-sort-header-pointer-middle{margin:auto;height:2px;width:2px;background:currentColor;transform:rotate(45deg)}.cdk-high-contrast-active .mat-sort-header-pointer-middle{width:0;height:0;border-top:solid 2px;border-left:solid 2px}.mat-sort-header-pointer-left,.mat-sort-header-pointer-right{background:currentColor;width:6px;height:2px;position:absolute;top:0}.cdk-high-contrast-active .mat-sort-header-pointer-left,.cdk-high-contrast-active .mat-sort-header-pointer-right{width:0;height:0;border-left:solid 6px;border-top:solid 2px}.mat-sort-header-pointer-left{transform-origin:right;left:0}.mat-sort-header-pointer-right{transform-origin:left;right:0}\n"]
            }]
    }], function () { return [{ type: MatSortHeaderIntl }, { type: core/* ChangeDetectorRef */.sBO }, { type: MatSort, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: ['MAT_SORT_HEADER_COLUMN_DEF']
            }, {
                type: core/* Optional */.FiY
            }] }, { type: a11y/* FocusMonitor */.tE }, { type: core/* ElementRef */.SBq }]; }, { arrowPosition: [{
            type: core/* Input */.IIB
        }], disableClear: [{
            type: core/* Input */.IIB
        }], id: [{
            type: core/* Input */.IIB,
            args: ['mat-sort-header']
        }], start: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatSortModule {
}
MatSortModule.ɵfac = function MatSortModule_Factory(t) { return new (t || MatSortModule)(); };
MatSortModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatSortModule });
MatSortModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [MAT_SORT_HEADER_INTL_PROVIDER], imports: [[common/* CommonModule */.ez, fesm2015_core/* MatCommonModule */.BQ]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSortModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [common/* CommonModule */.ez, fesm2015_core/* MatCommonModule */.BQ],
                exports: [MatSort, MatSortHeader],
                declarations: [MatSort, MatSortHeader],
                providers: [MAT_SORT_HEADER_INTL_PROVIDER]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatSortModule, { declarations: function () { return [MatSort, MatSortHeader]; }, imports: function () { return [common/* CommonModule */.ez, fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [MatSort, MatSortHeader]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=sort.js.map
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/scrolling.js + 10 modules
var scrolling = __webpack_require__(9243);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/switchMap.js
var switchMap = __webpack_require__(3190);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/map.js
var map = __webpack_require__(8002);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/distinctUntilChanged.js
var distinctUntilChanged = __webpack_require__(7519);
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/select.js
















/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * The following are all the animations for the mat-select component, with each
 * const containing the metadata for one animation.
 *
 * The values below match the implementation of the AngularJS Material mat-select animation.
 * @docs-private
 */










const select_c0 = ["trigger"];
const select_c1 = ["panel"];
function MatSelect_span_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "span", 8);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r2.placeholder);
} }
function MatSelect_span_5_span_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "span", 12);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r5.triggerValue);
} }
function MatSelect_span_5_ng_content_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵprojection */.Hsn(0, 0, ["*ngSwitchCase", "true"]);
} }
function MatSelect_span_5_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "span", 9);
    core/* ɵɵtemplate */.YNc(1, MatSelect_span_5_span_1_Template, 2, 1, "span", 10);
    core/* ɵɵtemplate */.YNc(2, MatSelect_span_5_ng_content_2_Template, 1, 0, "ng-content", 11);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngSwitch", !!ctx_r3.customTrigger);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngSwitchCase", true);
} }
function MatSelect_ng_template_8_Template(rf, ctx) { if (rf & 1) {
    const _r9 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 13);
    core/* ɵɵelementStart */.TgZ(1, "div", 14, 15);
    core/* ɵɵlistener */.NdJ("@transformPanel.done", function MatSelect_ng_template_8_Template_div_animation_transformPanel_done_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const ctx_r8 = core/* ɵɵnextContext */.oxw(); return ctx_r8._panelDoneAnimatingStream.next($event.toState); })("keydown", function MatSelect_ng_template_8_Template_div_keydown_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const ctx_r10 = core/* ɵɵnextContext */.oxw(); return ctx_r10._handleKeydown($event); });
    core/* ɵɵprojection */.Hsn(3, 1);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("@transformPanelWrap", undefined);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵclassMapInterpolate1 */.Gre("mat-select-panel ", ctx_r4._getPanelTheme(), "");
    core/* ɵɵstyleProp */.Udp("transform-origin", ctx_r4._transformOrigin)("font-size", ctx_r4._triggerFontSize, "px");
    core/* ɵɵproperty */.Q6J("ngClass", ctx_r4.panelClass)("@transformPanel", ctx_r4.multiple ? "showing-multiple" : "showing");
    core/* ɵɵattribute */.uIk("id", ctx_r4.id + "-panel")("aria-multiselectable", ctx_r4.multiple)("aria-label", ctx_r4.ariaLabel || null)("aria-labelledby", ctx_r4._getPanelAriaLabelledby());
} }
const select_c2 = [[["mat-select-trigger"]], "*"];
const select_c3 = ["mat-select-trigger", "*"];
const matSelectAnimations = {
    /**
     * This animation ensures the select's overlay panel animation (transformPanel) is called when
     * closing the select.
     * This is needed due to https://github.com/angular/angular/issues/23302
     */
    transformPanelWrap: (0,animations/* trigger */.X$)('transformPanelWrap', [
        (0,animations/* transition */.eR)('* => void', (0,animations/* query */.IO)('@transformPanel', [(0,animations/* animateChild */.pV)()], { optional: true }))
    ]),
    /**
     * This animation transforms the select's overlay panel on and off the page.
     *
     * When the panel is attached to the DOM, it expands its width by the amount of padding, scales it
     * up to 100% on the Y axis, fades in its border, and translates slightly up and to the
     * side to ensure the option text correctly overlaps the trigger text.
     *
     * When the panel is removed from the DOM, it simply fades out linearly.
     */
    transformPanel: (0,animations/* trigger */.X$)('transformPanel', [
        (0,animations/* state */.SB)('void', (0,animations/* style */.oB)({
            transform: 'scaleY(0.8)',
            minWidth: '100%',
            opacity: 0
        })),
        (0,animations/* state */.SB)('showing', (0,animations/* style */.oB)({
            opacity: 1,
            minWidth: 'calc(100% + 32px)',
            transform: 'scaleY(1)'
        })),
        (0,animations/* state */.SB)('showing-multiple', (0,animations/* style */.oB)({
            opacity: 1,
            minWidth: 'calc(100% + 64px)',
            transform: 'scaleY(1)'
        })),
        (0,animations/* transition */.eR)('void => *', (0,animations/* animate */.jt)('120ms cubic-bezier(0, 0, 0.2, 1)')),
        (0,animations/* transition */.eR)('* => void', (0,animations/* animate */.jt)('100ms 25ms linear', (0,animations/* style */.oB)({ opacity: 0 })))
    ])
};

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Returns an exception to be thrown when attempting to change a select's `multiple` option
 * after initialization.
 * @docs-private
 */
function getMatSelectDynamicMultipleError() {
    return Error('Cannot change `multiple` mode of select after initialization.');
}
/**
 * Returns an exception to be thrown when attempting to assign a non-array value to a select
 * in `multiple` mode. Note that `undefined` and `null` are still valid values to allow for
 * resetting the value.
 * @docs-private
 */
function getMatSelectNonArrayValueError() {
    return Error('Value must be an array in multiple-selection mode.');
}
/**
 * Returns an exception to be thrown when assigning a non-function value to the comparator
 * used to determine if a value corresponds to an option. Note that whether the function
 * actually takes two values and returns a boolean is not checked.
 */
function getMatSelectNonFunctionValueError() {
    return Error('`compareWith` must be a function.');
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
let select_nextUniqueId = 0;
/**
 * The following style constants are necessary to save here in order
 * to properly calculate the alignment of the selected option over
 * the trigger element.
 */
/** The max height of the select's overlay panel. */
const SELECT_PANEL_MAX_HEIGHT = 256;
/** The panel's padding on the x-axis. */
const SELECT_PANEL_PADDING_X = 16;
/** The panel's x axis padding if it is indented (e.g. there is an option group). */
const SELECT_PANEL_INDENT_PADDING_X = SELECT_PANEL_PADDING_X * 2;
/** The height of the select items in `em` units. */
const SELECT_ITEM_HEIGHT_EM = 3;
// TODO(josephperrott): Revert to a constant after 2018 spec updates are fully merged.
/**
 * Distance between the panel edge and the option text in
 * multi-selection mode.
 *
 * Calculated as:
 * (SELECT_PANEL_PADDING_X * 1.5) + 16 = 40
 * The padding is multiplied by 1.5 because the checkbox's margin is half the padding.
 * The checkbox width is 16px.
 */
const SELECT_MULTIPLE_PANEL_PADDING_X = SELECT_PANEL_PADDING_X * 1.5 + 16;
/**
 * The select panel will only "fit" inside the viewport if it is positioned at
 * this value or more away from the viewport boundary.
 */
const SELECT_PANEL_VIEWPORT_PADDING = 8;
/** Injection token that determines the scroll handling while a select is open. */
const MAT_SELECT_SCROLL_STRATEGY = new core/* InjectionToken */.OlP('mat-select-scroll-strategy');
/** @docs-private */
function MAT_SELECT_SCROLL_STRATEGY_PROVIDER_FACTORY(overlay) {
    return () => overlay.scrollStrategies.reposition();
}
/** Injection token that can be used to provide the default options the select module. */
const MAT_SELECT_CONFIG = new core/* InjectionToken */.OlP('MAT_SELECT_CONFIG');
/** @docs-private */
const MAT_SELECT_SCROLL_STRATEGY_PROVIDER = {
    provide: MAT_SELECT_SCROLL_STRATEGY,
    deps: [overlay/* Overlay */.aV],
    useFactory: MAT_SELECT_SCROLL_STRATEGY_PROVIDER_FACTORY,
};
/** Change event object that is emitted when the select value has changed. */
class MatSelectChange {
    constructor(
    /** Reference to the select that emitted the change event. */
    source, 
    /** Current value of the select that emitted the event. */
    value) {
        this.source = source;
        this.value = value;
    }
}
// Boilerplate for applying mixins to MatSelect.
/** @docs-private */
const _MatSelectMixinBase = (0,fesm2015_core/* mixinDisableRipple */.Kr)((0,fesm2015_core/* mixinTabIndex */.sb)((0,fesm2015_core/* mixinDisabled */.Id)((0,fesm2015_core/* mixinErrorState */.FD)(class {
    constructor(_elementRef, _defaultErrorStateMatcher, _parentForm, _parentFormGroup, ngControl) {
        this._elementRef = _elementRef;
        this._defaultErrorStateMatcher = _defaultErrorStateMatcher;
        this._parentForm = _parentForm;
        this._parentFormGroup = _parentFormGroup;
        this.ngControl = ngControl;
    }
}))));
/**
 * Injection token that can be used to reference instances of `MatSelectTrigger`. It serves as
 * alternative token to the actual `MatSelectTrigger` class which could cause unnecessary
 * retention of the class and its directive metadata.
 */
const MAT_SELECT_TRIGGER = new core/* InjectionToken */.OlP('MatSelectTrigger');
/**
 * Allows the user to customize the trigger that is displayed when the select has a value.
 */
class MatSelectTrigger {
}
MatSelectTrigger.ɵfac = function MatSelectTrigger_Factory(t) { return new (t || MatSelectTrigger)(); };
MatSelectTrigger.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatSelectTrigger, selectors: [["mat-select-trigger"]], features: [core/* ɵɵProvidersFeature */._Bn([{ provide: MAT_SELECT_TRIGGER, useExisting: MatSelectTrigger }])] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSelectTrigger, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-select-trigger',
                providers: [{ provide: MAT_SELECT_TRIGGER, useExisting: MatSelectTrigger }]
            }]
    }], null, null); })();
/** Base class with all of the `MatSelect` functionality. */
class _MatSelectBase extends _MatSelectMixinBase {
    constructor(_viewportRuler, _changeDetectorRef, _ngZone, _defaultErrorStateMatcher, elementRef, _dir, _parentForm, _parentFormGroup, _parentFormField, ngControl, tabIndex, scrollStrategyFactory, _liveAnnouncer, _defaultOptions) {
        var _a, _b, _c;
        super(elementRef, _defaultErrorStateMatcher, _parentForm, _parentFormGroup, ngControl);
        this._viewportRuler = _viewportRuler;
        this._changeDetectorRef = _changeDetectorRef;
        this._ngZone = _ngZone;
        this._dir = _dir;
        this._parentFormField = _parentFormField;
        this._liveAnnouncer = _liveAnnouncer;
        this._defaultOptions = _defaultOptions;
        /** Whether or not the overlay panel is open. */
        this._panelOpen = false;
        /** Comparison function to specify which option is displayed. Defaults to object equality. */
        this._compareWith = (o1, o2) => o1 === o2;
        /** Unique id for this input. */
        this._uid = `mat-select-${select_nextUniqueId++}`;
        /** Current `ariar-labelledby` value for the select trigger. */
        this._triggerAriaLabelledBy = null;
        /** Emits whenever the component is destroyed. */
        this._destroy = new Subject/* Subject */.xQ();
        /** `View -> model callback called when value changes` */
        this._onChange = () => { };
        /** `View -> model callback called when select has been touched` */
        this._onTouched = () => { };
        /** ID for the DOM node containing the select's value. */
        this._valueId = `mat-select-value-${select_nextUniqueId++}`;
        /** Emits when the panel element is finished transforming in. */
        this._panelDoneAnimatingStream = new Subject/* Subject */.xQ();
        this._overlayPanelClass = ((_a = this._defaultOptions) === null || _a === void 0 ? void 0 : _a.overlayPanelClass) || '';
        this._focused = false;
        /** A name for this control that can be used by `mat-form-field`. */
        this.controlType = 'mat-select';
        this._required = false;
        this._multiple = false;
        this._disableOptionCentering = (_c = (_b = this._defaultOptions) === null || _b === void 0 ? void 0 : _b.disableOptionCentering) !== null && _c !== void 0 ? _c : false;
        /** Aria label of the select. */
        this.ariaLabel = '';
        /** Combined stream of all of the child options' change events. */
        this.optionSelectionChanges = (0,defer/* defer */.P)(() => {
            const options = this.options;
            if (options) {
                return options.changes.pipe((0,startWith/* startWith */.O)(options), (0,switchMap/* switchMap */.w)(() => (0,merge/* merge */.T)(...options.map(option => option.onSelectionChange))));
            }
            return this._ngZone.onStable
                .pipe((0,take/* take */.q)(1), (0,switchMap/* switchMap */.w)(() => this.optionSelectionChanges));
        });
        /** Event emitted when the select panel has been toggled. */
        this.openedChange = new core/* EventEmitter */.vpe();
        /** Event emitted when the select has been opened. */
        this._openedStream = this.openedChange.pipe((0,filter/* filter */.h)(o => o), (0,map/* map */.U)(() => { }));
        /** Event emitted when the select has been closed. */
        this._closedStream = this.openedChange.pipe((0,filter/* filter */.h)(o => !o), (0,map/* map */.U)(() => { }));
        /** Event emitted when the selected value has been changed by the user. */
        this.selectionChange = new core/* EventEmitter */.vpe();
        /**
         * Event that emits whenever the raw value of the select changes. This is here primarily
         * to facilitate the two-way binding for the `value` input.
         * @docs-private
         */
        this.valueChange = new core/* EventEmitter */.vpe();
        if (this.ngControl) {
            // Note: we provide the value accessor through here, instead of
            // the `providers` to avoid running into a circular import.
            this.ngControl.valueAccessor = this;
        }
        // Note that we only want to set this when the defaults pass it in, otherwise it should
        // stay as `undefined` so that it falls back to the default in the key manager.
        if ((_defaultOptions === null || _defaultOptions === void 0 ? void 0 : _defaultOptions.typeaheadDebounceInterval) != null) {
            this._typeaheadDebounceInterval = _defaultOptions.typeaheadDebounceInterval;
        }
        this._scrollStrategyFactory = scrollStrategyFactory;
        this._scrollStrategy = this._scrollStrategyFactory();
        this.tabIndex = parseInt(tabIndex) || 0;
        // Force setter to be called in case id was not specified.
        this.id = this.id;
    }
    /** Whether the select is focused. */
    get focused() {
        return this._focused || this._panelOpen;
    }
    /** Placeholder to be shown if no value has been selected. */
    get placeholder() { return this._placeholder; }
    set placeholder(value) {
        this._placeholder = value;
        this.stateChanges.next();
    }
    /** Whether the component is required. */
    get required() { return this._required; }
    set required(value) {
        this._required = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        this.stateChanges.next();
    }
    /** Whether the user should be allowed to select multiple options. */
    get multiple() { return this._multiple; }
    set multiple(value) {
        if (this._selectionModel && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getMatSelectDynamicMultipleError();
        }
        this._multiple = (0,coercion/* coerceBooleanProperty */.Ig)(value);
    }
    /** Whether to center the active option over the trigger. */
    get disableOptionCentering() { return this._disableOptionCentering; }
    set disableOptionCentering(value) {
        this._disableOptionCentering = (0,coercion/* coerceBooleanProperty */.Ig)(value);
    }
    /**
     * Function to compare the option values with the selected values. The first argument
     * is a value from an option. The second is a value from the selection. A boolean
     * should be returned.
     */
    get compareWith() { return this._compareWith; }
    set compareWith(fn) {
        if (typeof fn !== 'function' && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw getMatSelectNonFunctionValueError();
        }
        this._compareWith = fn;
        if (this._selectionModel) {
            // A different comparator means the selection could change.
            this._initializeSelection();
        }
    }
    /** Value of the select control. */
    get value() { return this._value; }
    set value(newValue) {
        // Always re-assign an array, because it might have been mutated.
        if (newValue !== this._value || (this._multiple && Array.isArray(newValue))) {
            if (this.options) {
                this._setSelectionByValue(newValue);
            }
            this._value = newValue;
        }
    }
    /** Time to wait in milliseconds after the last keystroke before moving focus to an item. */
    get typeaheadDebounceInterval() { return this._typeaheadDebounceInterval; }
    set typeaheadDebounceInterval(value) {
        this._typeaheadDebounceInterval = (0,coercion/* coerceNumberProperty */.su)(value);
    }
    /** Unique id of the element. */
    get id() { return this._id; }
    set id(value) {
        this._id = value || this._uid;
        this.stateChanges.next();
    }
    ngOnInit() {
        this._selectionModel = new collections/* SelectionModel */.Ov(this.multiple);
        this.stateChanges.next();
        // We need `distinctUntilChanged` here, because some browsers will
        // fire the animation end event twice for the same animation. See:
        // https://github.com/angular/angular/issues/24084
        this._panelDoneAnimatingStream
            .pipe((0,distinctUntilChanged/* distinctUntilChanged */.x)(), (0,takeUntil/* takeUntil */.R)(this._destroy))
            .subscribe(() => this._panelDoneAnimating(this.panelOpen));
    }
    ngAfterContentInit() {
        this._initKeyManager();
        this._selectionModel.changed.pipe((0,takeUntil/* takeUntil */.R)(this._destroy)).subscribe(event => {
            event.added.forEach(option => option.select());
            event.removed.forEach(option => option.deselect());
        });
        this.options.changes.pipe((0,startWith/* startWith */.O)(null), (0,takeUntil/* takeUntil */.R)(this._destroy)).subscribe(() => {
            this._resetOptions();
            this._initializeSelection();
        });
    }
    ngDoCheck() {
        const newAriaLabelledby = this._getTriggerAriaLabelledby();
        // We have to manage setting the `aria-labelledby` ourselves, because part of its value
        // is computed as a result of a content query which can cause this binding to trigger a
        // "changed after checked" error.
        if (newAriaLabelledby !== this._triggerAriaLabelledBy) {
            const element = this._elementRef.nativeElement;
            this._triggerAriaLabelledBy = newAriaLabelledby;
            if (newAriaLabelledby) {
                element.setAttribute('aria-labelledby', newAriaLabelledby);
            }
            else {
                element.removeAttribute('aria-labelledby');
            }
        }
        if (this.ngControl) {
            this.updateErrorState();
        }
    }
    ngOnChanges(changes) {
        // Updating the disabled state is handled by `mixinDisabled`, but we need to additionally let
        // the parent form field know to run change detection when the disabled state changes.
        if (changes['disabled']) {
            this.stateChanges.next();
        }
        if (changes['typeaheadDebounceInterval'] && this._keyManager) {
            this._keyManager.withTypeAhead(this._typeaheadDebounceInterval);
        }
    }
    ngOnDestroy() {
        this._destroy.next();
        this._destroy.complete();
        this.stateChanges.complete();
    }
    /** Toggles the overlay panel open or closed. */
    toggle() {
        this.panelOpen ? this.close() : this.open();
    }
    /** Opens the overlay panel. */
    open() {
        if (this._canOpen()) {
            this._panelOpen = true;
            this._keyManager.withHorizontalOrientation(null);
            this._highlightCorrectOption();
            this._changeDetectorRef.markForCheck();
        }
    }
    /** Closes the overlay panel and focuses the host element. */
    close() {
        if (this._panelOpen) {
            this._panelOpen = false;
            this._keyManager.withHorizontalOrientation(this._isRtl() ? 'rtl' : 'ltr');
            this._changeDetectorRef.markForCheck();
            this._onTouched();
        }
    }
    /**
     * Sets the select's value. Part of the ControlValueAccessor interface
     * required to integrate with Angular's core forms API.
     *
     * @param value New value to be written to the model.
     */
    writeValue(value) {
        this.value = value;
    }
    /**
     * Saves a callback function to be invoked when the select's value
     * changes from user input. Part of the ControlValueAccessor interface
     * required to integrate with Angular's core forms API.
     *
     * @param fn Callback to be triggered when the value changes.
     */
    registerOnChange(fn) {
        this._onChange = fn;
    }
    /**
     * Saves a callback function to be invoked when the select is blurred
     * by the user. Part of the ControlValueAccessor interface required
     * to integrate with Angular's core forms API.
     *
     * @param fn Callback to be triggered when the component has been touched.
     */
    registerOnTouched(fn) {
        this._onTouched = fn;
    }
    /**
     * Disables the select. Part of the ControlValueAccessor interface required
     * to integrate with Angular's core forms API.
     *
     * @param isDisabled Sets whether the component is disabled.
     */
    setDisabledState(isDisabled) {
        this.disabled = isDisabled;
        this._changeDetectorRef.markForCheck();
        this.stateChanges.next();
    }
    /** Whether or not the overlay panel is open. */
    get panelOpen() {
        return this._panelOpen;
    }
    /** The currently selected option. */
    get selected() {
        return this.multiple ? this._selectionModel.selected : this._selectionModel.selected[0];
    }
    /** The value displayed in the trigger. */
    get triggerValue() {
        if (this.empty) {
            return '';
        }
        if (this._multiple) {
            const selectedOptions = this._selectionModel.selected.map(option => option.viewValue);
            if (this._isRtl()) {
                selectedOptions.reverse();
            }
            // TODO(crisbeto): delimiter should be configurable for proper localization.
            return selectedOptions.join(', ');
        }
        return this._selectionModel.selected[0].viewValue;
    }
    /** Whether the element is in RTL mode. */
    _isRtl() {
        return this._dir ? this._dir.value === 'rtl' : false;
    }
    /** Handles all keydown events on the select. */
    _handleKeydown(event) {
        if (!this.disabled) {
            this.panelOpen ? this._handleOpenKeydown(event) : this._handleClosedKeydown(event);
        }
    }
    /** Handles keyboard events while the select is closed. */
    _handleClosedKeydown(event) {
        const keyCode = event.keyCode;
        const isArrowKey = keyCode === keycodes/* DOWN_ARROW */.JH || keyCode === keycodes/* UP_ARROW */.LH ||
            keyCode === keycodes/* LEFT_ARROW */.oh || keyCode === keycodes/* RIGHT_ARROW */.SV;
        const isOpenKey = keyCode === keycodes/* ENTER */.K5 || keyCode === keycodes/* SPACE */.L_;
        const manager = this._keyManager;
        // Open the select on ALT + arrow key to match the native <select>
        if (!manager.isTyping() && (isOpenKey && !(0,keycodes/* hasModifierKey */.Vb)(event)) ||
            ((this.multiple || event.altKey) && isArrowKey)) {
            event.preventDefault(); // prevents the page from scrolling down when pressing space
            this.open();
        }
        else if (!this.multiple) {
            const previouslySelectedOption = this.selected;
            manager.onKeydown(event);
            const selectedOption = this.selected;
            // Since the value has changed, we need to announce it ourselves.
            if (selectedOption && previouslySelectedOption !== selectedOption) {
                // We set a duration on the live announcement, because we want the live element to be
                // cleared after a while so that users can't navigate to it using the arrow keys.
                this._liveAnnouncer.announce(selectedOption.viewValue, 10000);
            }
        }
    }
    /** Handles keyboard events when the selected is open. */
    _handleOpenKeydown(event) {
        const manager = this._keyManager;
        const keyCode = event.keyCode;
        const isArrowKey = keyCode === keycodes/* DOWN_ARROW */.JH || keyCode === keycodes/* UP_ARROW */.LH;
        const isTyping = manager.isTyping();
        if (isArrowKey && event.altKey) {
            // Close the select on ALT + arrow key to match the native <select>
            event.preventDefault();
            this.close();
            // Don't do anything in this case if the user is typing,
            // because the typing sequence can include the space key.
        }
        else if (!isTyping && (keyCode === keycodes/* ENTER */.K5 || keyCode === keycodes/* SPACE */.L_) && manager.activeItem &&
            !(0,keycodes/* hasModifierKey */.Vb)(event)) {
            event.preventDefault();
            manager.activeItem._selectViaInteraction();
        }
        else if (!isTyping && this._multiple && keyCode === keycodes.A && event.ctrlKey) {
            event.preventDefault();
            const hasDeselectedOptions = this.options.some(opt => !opt.disabled && !opt.selected);
            this.options.forEach(option => {
                if (!option.disabled) {
                    hasDeselectedOptions ? option.select() : option.deselect();
                }
            });
        }
        else {
            const previouslyFocusedIndex = manager.activeItemIndex;
            manager.onKeydown(event);
            if (this._multiple && isArrowKey && event.shiftKey && manager.activeItem &&
                manager.activeItemIndex !== previouslyFocusedIndex) {
                manager.activeItem._selectViaInteraction();
            }
        }
    }
    _onFocus() {
        if (!this.disabled) {
            this._focused = true;
            this.stateChanges.next();
        }
    }
    /**
     * Calls the touched callback only if the panel is closed. Otherwise, the trigger will
     * "blur" to the panel when it opens, causing a false positive.
     */
    _onBlur() {
        this._focused = false;
        if (!this.disabled && !this.panelOpen) {
            this._onTouched();
            this._changeDetectorRef.markForCheck();
            this.stateChanges.next();
        }
    }
    /**
     * Callback that is invoked when the overlay panel has been attached.
     */
    _onAttached() {
        this._overlayDir.positionChange.pipe((0,take/* take */.q)(1)).subscribe(() => {
            this._changeDetectorRef.detectChanges();
            this._positioningSettled();
        });
    }
    /** Returns the theme to be used on the panel. */
    _getPanelTheme() {
        return this._parentFormField ? `mat-${this._parentFormField.color}` : '';
    }
    /** Whether the select has a value. */
    get empty() {
        return !this._selectionModel || this._selectionModel.isEmpty();
    }
    _initializeSelection() {
        // Defer setting the value in order to avoid the "Expression
        // has changed after it was checked" errors from Angular.
        Promise.resolve().then(() => {
            this._setSelectionByValue(this.ngControl ? this.ngControl.value : this._value);
            this.stateChanges.next();
        });
    }
    /**
     * Sets the selected option based on a value. If no option can be
     * found with the designated value, the select trigger is cleared.
     */
    _setSelectionByValue(value) {
        this._selectionModel.selected.forEach(option => option.setInactiveStyles());
        this._selectionModel.clear();
        if (this.multiple && value) {
            if (!Array.isArray(value) && (typeof ngDevMode === 'undefined' || ngDevMode)) {
                throw getMatSelectNonArrayValueError();
            }
            value.forEach((currentValue) => this._selectValue(currentValue));
            this._sortValues();
        }
        else {
            const correspondingOption = this._selectValue(value);
            // Shift focus to the active item. Note that we shouldn't do this in multiple
            // mode, because we don't know what option the user interacted with last.
            if (correspondingOption) {
                this._keyManager.updateActiveItem(correspondingOption);
            }
            else if (!this.panelOpen) {
                // Otherwise reset the highlighted option. Note that we only want to do this while
                // closed, because doing it while open can shift the user's focus unnecessarily.
                this._keyManager.updateActiveItem(-1);
            }
        }
        this._changeDetectorRef.markForCheck();
    }
    /**
     * Finds and selects and option based on its value.
     * @returns Option that has the corresponding value.
     */
    _selectValue(value) {
        const correspondingOption = this.options.find((option) => {
            // Skip options that are already in the model. This allows us to handle cases
            // where the same primitive value is selected multiple times.
            if (this._selectionModel.isSelected(option)) {
                return false;
            }
            try {
                // Treat null as a special reset value.
                return option.value != null && this._compareWith(option.value, value);
            }
            catch (error) {
                if (typeof ngDevMode === 'undefined' || ngDevMode) {
                    // Notify developers of errors in their comparator.
                    console.warn(error);
                }
                return false;
            }
        });
        if (correspondingOption) {
            this._selectionModel.select(correspondingOption);
        }
        return correspondingOption;
    }
    /** Sets up a key manager to listen to keyboard events on the overlay panel. */
    _initKeyManager() {
        this._keyManager = new a11y/* ActiveDescendantKeyManager */.s1(this.options)
            .withTypeAhead(this._typeaheadDebounceInterval)
            .withVerticalOrientation()
            .withHorizontalOrientation(this._isRtl() ? 'rtl' : 'ltr')
            .withHomeAndEnd()
            .withAllowedModifierKeys(['shiftKey']);
        this._keyManager.tabOut.pipe((0,takeUntil/* takeUntil */.R)(this._destroy)).subscribe(() => {
            if (this.panelOpen) {
                // Select the active item when tabbing away. This is consistent with how the native
                // select behaves. Note that we only want to do this in single selection mode.
                if (!this.multiple && this._keyManager.activeItem) {
                    this._keyManager.activeItem._selectViaInteraction();
                }
                // Restore focus to the trigger before closing. Ensures that the focus
                // position won't be lost if the user got focus into the overlay.
                this.focus();
                this.close();
            }
        });
        this._keyManager.change.pipe((0,takeUntil/* takeUntil */.R)(this._destroy)).subscribe(() => {
            if (this._panelOpen && this.panel) {
                this._scrollOptionIntoView(this._keyManager.activeItemIndex || 0);
            }
            else if (!this._panelOpen && !this.multiple && this._keyManager.activeItem) {
                this._keyManager.activeItem._selectViaInteraction();
            }
        });
    }
    /** Drops current option subscriptions and IDs and resets from scratch. */
    _resetOptions() {
        const changedOrDestroyed = (0,merge/* merge */.T)(this.options.changes, this._destroy);
        this.optionSelectionChanges.pipe((0,takeUntil/* takeUntil */.R)(changedOrDestroyed)).subscribe(event => {
            this._onSelect(event.source, event.isUserInput);
            if (event.isUserInput && !this.multiple && this._panelOpen) {
                this.close();
                this.focus();
            }
        });
        // Listen to changes in the internal state of the options and react accordingly.
        // Handles cases like the labels of the selected options changing.
        (0,merge/* merge */.T)(...this.options.map(option => option._stateChanges))
            .pipe((0,takeUntil/* takeUntil */.R)(changedOrDestroyed))
            .subscribe(() => {
            this._changeDetectorRef.markForCheck();
            this.stateChanges.next();
        });
    }
    /** Invoked when an option is clicked. */
    _onSelect(option, isUserInput) {
        const wasSelected = this._selectionModel.isSelected(option);
        if (option.value == null && !this._multiple) {
            option.deselect();
            this._selectionModel.clear();
            if (this.value != null) {
                this._propagateChanges(option.value);
            }
        }
        else {
            if (wasSelected !== option.selected) {
                option.selected ? this._selectionModel.select(option) :
                    this._selectionModel.deselect(option);
            }
            if (isUserInput) {
                this._keyManager.setActiveItem(option);
            }
            if (this.multiple) {
                this._sortValues();
                if (isUserInput) {
                    // In case the user selected the option with their mouse, we
                    // want to restore focus back to the trigger, in order to
                    // prevent the select keyboard controls from clashing with
                    // the ones from `mat-option`.
                    this.focus();
                }
            }
        }
        if (wasSelected !== this._selectionModel.isSelected(option)) {
            this._propagateChanges();
        }
        this.stateChanges.next();
    }
    /** Sorts the selected values in the selected based on their order in the panel. */
    _sortValues() {
        if (this.multiple) {
            const options = this.options.toArray();
            this._selectionModel.sort((a, b) => {
                return this.sortComparator ? this.sortComparator(a, b, options) :
                    options.indexOf(a) - options.indexOf(b);
            });
            this.stateChanges.next();
        }
    }
    /** Emits change event to set the model value. */
    _propagateChanges(fallbackValue) {
        let valueToEmit = null;
        if (this.multiple) {
            valueToEmit = this.selected.map(option => option.value);
        }
        else {
            valueToEmit = this.selected ? this.selected.value : fallbackValue;
        }
        this._value = valueToEmit;
        this.valueChange.emit(valueToEmit);
        this._onChange(valueToEmit);
        this.selectionChange.emit(this._getChangeEvent(valueToEmit));
        this._changeDetectorRef.markForCheck();
    }
    /**
     * Highlights the selected item. If no option is selected, it will highlight
     * the first item instead.
     */
    _highlightCorrectOption() {
        if (this._keyManager) {
            if (this.empty) {
                this._keyManager.setFirstItemActive();
            }
            else {
                this._keyManager.setActiveItem(this._selectionModel.selected[0]);
            }
        }
    }
    /** Whether the panel is allowed to open. */
    _canOpen() {
        var _a;
        return !this._panelOpen && !this.disabled && ((_a = this.options) === null || _a === void 0 ? void 0 : _a.length) > 0;
    }
    /** Focuses the select element. */
    focus(options) {
        this._elementRef.nativeElement.focus(options);
    }
    /** Gets the aria-labelledby for the select panel. */
    _getPanelAriaLabelledby() {
        var _a;
        if (this.ariaLabel) {
            return null;
        }
        const labelId = (_a = this._parentFormField) === null || _a === void 0 ? void 0 : _a.getLabelId();
        const labelExpression = (labelId ? labelId + ' ' : '');
        return this.ariaLabelledby ? labelExpression + this.ariaLabelledby : labelId;
    }
    /** Determines the `aria-activedescendant` to be set on the host. */
    _getAriaActiveDescendant() {
        if (this.panelOpen && this._keyManager && this._keyManager.activeItem) {
            return this._keyManager.activeItem.id;
        }
        return null;
    }
    /** Gets the aria-labelledby of the select component trigger. */
    _getTriggerAriaLabelledby() {
        var _a;
        if (this.ariaLabel) {
            return null;
        }
        const labelId = (_a = this._parentFormField) === null || _a === void 0 ? void 0 : _a.getLabelId();
        let value = (labelId ? labelId + ' ' : '') + this._valueId;
        if (this.ariaLabelledby) {
            value += ' ' + this.ariaLabelledby;
        }
        return value;
    }
    /** Called when the overlay panel is done animating. */
    _panelDoneAnimating(isOpen) {
        this.openedChange.emit(isOpen);
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    setDescribedByIds(ids) {
        this._ariaDescribedby = ids.join(' ');
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    onContainerClick() {
        this.focus();
        this.open();
    }
    /**
     * Implemented as part of MatFormFieldControl.
     * @docs-private
     */
    get shouldLabelFloat() {
        return this._panelOpen || !this.empty || (this._focused && !!this._placeholder);
    }
}
_MatSelectBase.ɵfac = function _MatSelectBase_Factory(t) { return new (t || _MatSelectBase)(core/* ɵɵdirectiveInject */.Y36(scrolling/* ViewportRuler */.rL), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(fesm2015_core/* ErrorStateMatcher */.rD), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(bidi/* Directionality */.Is, 8), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* NgForm */.F, 8), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* FormGroupDirective */.sg, 8), core/* ɵɵdirectiveInject */.Y36(MAT_FORM_FIELD, 8), core/* ɵɵdirectiveInject */.Y36(fesm2015_forms/* NgControl */.a5, 10), core/* ɵɵinjectAttribute */.$8M('tabindex'), core/* ɵɵdirectiveInject */.Y36(MAT_SELECT_SCROLL_STRATEGY), core/* ɵɵdirectiveInject */.Y36(a11y/* LiveAnnouncer */.Kd), core/* ɵɵdirectiveInject */.Y36(MAT_SELECT_CONFIG, 8)); };
_MatSelectBase.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: _MatSelectBase, viewQuery: function _MatSelectBase_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(select_c0, 5);
        core/* ɵɵviewQuery */.Gf(select_c1, 5);
        core/* ɵɵviewQuery */.Gf(overlay/* CdkConnectedOverlay */.pI, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.trigger = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.panel = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._overlayDir = _t.first);
    } }, inputs: { ariaLabel: ["aria-label", "ariaLabel"], id: "id", placeholder: "placeholder", required: "required", multiple: "multiple", disableOptionCentering: "disableOptionCentering", compareWith: "compareWith", value: "value", typeaheadDebounceInterval: "typeaheadDebounceInterval", panelClass: "panelClass", ariaLabelledby: ["aria-labelledby", "ariaLabelledby"], errorStateMatcher: "errorStateMatcher", sortComparator: "sortComparator" }, outputs: { openedChange: "openedChange", _openedStream: "opened", _closedStream: "closed", selectionChange: "selectionChange", valueChange: "valueChange" }, features: [core/* ɵɵInheritDefinitionFeature */.qOj, core/* ɵɵNgOnChangesFeature */.TTD] });
_MatSelectBase.ctorParameters = () => [
    { type: scrolling/* ViewportRuler */.rL },
    { type: core/* ChangeDetectorRef */.sBO },
    { type: core/* NgZone */.R0b },
    { type: fesm2015_core/* ErrorStateMatcher */.rD },
    { type: core/* ElementRef */.SBq },
    { type: bidi/* Directionality */.Is, decorators: [{ type: core/* Optional */.FiY }] },
    { type: fesm2015_forms/* NgForm */.F, decorators: [{ type: core/* Optional */.FiY }] },
    { type: fesm2015_forms/* FormGroupDirective */.sg, decorators: [{ type: core/* Optional */.FiY }] },
    { type: MatFormField, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_FORM_FIELD,] }] },
    { type: fesm2015_forms/* NgControl */.a5, decorators: [{ type: core/* Self */.PiD }, { type: core/* Optional */.FiY }] },
    { type: String, decorators: [{ type: core/* Attribute */.ahi, args: ['tabindex',] }] },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [MAT_SELECT_SCROLL_STRATEGY,] }] },
    { type: a11y/* LiveAnnouncer */.Kd },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_SELECT_CONFIG,] }] }
];
_MatSelectBase.propDecorators = {
    trigger: [{ type: core/* ViewChild */.i9L, args: ['trigger',] }],
    panel: [{ type: core/* ViewChild */.i9L, args: ['panel',] }],
    _overlayDir: [{ type: core/* ViewChild */.i9L, args: [overlay/* CdkConnectedOverlay */.pI,] }],
    panelClass: [{ type: core/* Input */.IIB }],
    placeholder: [{ type: core/* Input */.IIB }],
    required: [{ type: core/* Input */.IIB }],
    multiple: [{ type: core/* Input */.IIB }],
    disableOptionCentering: [{ type: core/* Input */.IIB }],
    compareWith: [{ type: core/* Input */.IIB }],
    value: [{ type: core/* Input */.IIB }],
    ariaLabel: [{ type: core/* Input */.IIB, args: ['aria-label',] }],
    ariaLabelledby: [{ type: core/* Input */.IIB, args: ['aria-labelledby',] }],
    errorStateMatcher: [{ type: core/* Input */.IIB }],
    typeaheadDebounceInterval: [{ type: core/* Input */.IIB }],
    sortComparator: [{ type: core/* Input */.IIB }],
    id: [{ type: core/* Input */.IIB }],
    openedChange: [{ type: core/* Output */.r_U }],
    _openedStream: [{ type: core/* Output */.r_U, args: ['opened',] }],
    _closedStream: [{ type: core/* Output */.r_U, args: ['closed',] }],
    selectionChange: [{ type: core/* Output */.r_U }],
    valueChange: [{ type: core/* Output */.r_U }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(_MatSelectBase, [{
        type: core/* Directive */.Xek
    }], function () { return [{ type: scrolling/* ViewportRuler */.rL }, { type: core/* ChangeDetectorRef */.sBO }, { type: core/* NgZone */.R0b }, { type: fesm2015_core/* ErrorStateMatcher */.rD }, { type: core/* ElementRef */.SBq }, { type: bidi/* Directionality */.Is, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: fesm2015_forms/* NgForm */.F, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: fesm2015_forms/* FormGroupDirective */.sg, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: MatFormField, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_FORM_FIELD]
            }] }, { type: fesm2015_forms/* NgControl */.a5, decorators: [{
                type: core/* Self */.PiD
            }, {
                type: core/* Optional */.FiY
            }] }, { type: String, decorators: [{
                type: core/* Attribute */.ahi,
                args: ['tabindex']
            }] }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [MAT_SELECT_SCROLL_STRATEGY]
            }] }, { type: a11y/* LiveAnnouncer */.Kd }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_SELECT_CONFIG]
            }] }]; }, { ariaLabel: [{
            type: core/* Input */.IIB,
            args: ['aria-label']
        }], openedChange: [{
            type: core/* Output */.r_U
        }], _openedStream: [{
            type: core/* Output */.r_U,
            args: ['opened']
        }], _closedStream: [{
            type: core/* Output */.r_U,
            args: ['closed']
        }], selectionChange: [{
            type: core/* Output */.r_U
        }], valueChange: [{
            type: core/* Output */.r_U
        }], id: [{
            type: core/* Input */.IIB
        }], placeholder: [{
            type: core/* Input */.IIB
        }], required: [{
            type: core/* Input */.IIB
        }], multiple: [{
            type: core/* Input */.IIB
        }], disableOptionCentering: [{
            type: core/* Input */.IIB
        }], compareWith: [{
            type: core/* Input */.IIB
        }], value: [{
            type: core/* Input */.IIB
        }], typeaheadDebounceInterval: [{
            type: core/* Input */.IIB
        }], trigger: [{
            type: core/* ViewChild */.i9L,
            args: ['trigger']
        }], panel: [{
            type: core/* ViewChild */.i9L,
            args: ['panel']
        }], _overlayDir: [{
            type: core/* ViewChild */.i9L,
            args: [overlay/* CdkConnectedOverlay */.pI]
        }], panelClass: [{
            type: core/* Input */.IIB
        }], ariaLabelledby: [{
            type: core/* Input */.IIB,
            args: ['aria-labelledby']
        }], errorStateMatcher: [{
            type: core/* Input */.IIB
        }], sortComparator: [{
            type: core/* Input */.IIB
        }] }); })();
class MatSelect extends _MatSelectBase {
    constructor() {
        super(...arguments);
        /** The scroll position of the overlay panel, calculated to center the selected option. */
        this._scrollTop = 0;
        /** The cached font-size of the trigger element. */
        this._triggerFontSize = 0;
        /** The value of the select panel's transform-origin property. */
        this._transformOrigin = 'top';
        /**
         * The y-offset of the overlay panel in relation to the trigger's top start corner.
         * This must be adjusted to align the selected option text over the trigger text.
         * when the panel opens. Will change based on the y-position of the selected option.
         */
        this._offsetY = 0;
        this._positions = [
            {
                originX: 'start',
                originY: 'top',
                overlayX: 'start',
                overlayY: 'top',
            },
            {
                originX: 'start',
                originY: 'bottom',
                overlayX: 'start',
                overlayY: 'bottom',
            },
        ];
    }
    /**
     * Calculates the scroll position of the select's overlay panel.
     *
     * Attempts to center the selected option in the panel. If the option is
     * too high or too low in the panel to be scrolled to the center, it clamps the
     * scroll position to the min or max scroll positions respectively.
     */
    _calculateOverlayScroll(selectedIndex, scrollBuffer, maxScroll) {
        const itemHeight = this._getItemHeight();
        const optionOffsetFromScrollTop = itemHeight * selectedIndex;
        const halfOptionHeight = itemHeight / 2;
        // Starts at the optionOffsetFromScrollTop, which scrolls the option to the top of the
        // scroll container, then subtracts the scroll buffer to scroll the option down to
        // the center of the overlay panel. Half the option height must be re-added to the
        // scrollTop so the option is centered based on its middle, not its top edge.
        const optimalScrollPosition = optionOffsetFromScrollTop - scrollBuffer + halfOptionHeight;
        return Math.min(Math.max(0, optimalScrollPosition), maxScroll);
    }
    ngOnInit() {
        super.ngOnInit();
        this._viewportRuler.change().pipe((0,takeUntil/* takeUntil */.R)(this._destroy)).subscribe(() => {
            if (this.panelOpen) {
                this._triggerRect = this.trigger.nativeElement.getBoundingClientRect();
                this._changeDetectorRef.markForCheck();
            }
        });
    }
    open() {
        if (super._canOpen()) {
            super.open();
            this._triggerRect = this.trigger.nativeElement.getBoundingClientRect();
            // Note: The computed font-size will be a string pixel value (e.g. "16px").
            // `parseInt` ignores the trailing 'px' and converts this to a number.
            this._triggerFontSize =
                parseInt(getComputedStyle(this.trigger.nativeElement).fontSize || '0');
            this._calculateOverlayPosition();
            // Set the font size on the panel element once it exists.
            this._ngZone.onStable.pipe((0,take/* take */.q)(1)).subscribe(() => {
                if (this._triggerFontSize && this._overlayDir.overlayRef &&
                    this._overlayDir.overlayRef.overlayElement) {
                    this._overlayDir.overlayRef.overlayElement.style.fontSize = `${this._triggerFontSize}px`;
                }
            });
        }
    }
    /** Scrolls the active option into view. */
    _scrollOptionIntoView(index) {
        const labelCount = (0,fesm2015_core/* _countGroupLabelsBeforeOption */.CB)(index, this.options, this.optionGroups);
        const itemHeight = this._getItemHeight();
        if (index === 0 && labelCount === 1) {
            // If we've got one group label before the option and we're at the top option,
            // scroll the list to the top. This is better UX than scrolling the list to the
            // top of the option, because it allows the user to read the top group's label.
            this.panel.nativeElement.scrollTop = 0;
        }
        else {
            this.panel.nativeElement.scrollTop = (0,fesm2015_core/* _getOptionScrollPosition */.jH)((index + labelCount) * itemHeight, itemHeight, this.panel.nativeElement.scrollTop, SELECT_PANEL_MAX_HEIGHT);
        }
    }
    _positioningSettled() {
        this._calculateOverlayOffsetX();
        this.panel.nativeElement.scrollTop = this._scrollTop;
    }
    _panelDoneAnimating(isOpen) {
        if (this.panelOpen) {
            this._scrollTop = 0;
        }
        else {
            this._overlayDir.offsetX = 0;
            this._changeDetectorRef.markForCheck();
        }
        super._panelDoneAnimating(isOpen);
    }
    _getChangeEvent(value) {
        return new MatSelectChange(this, value);
    }
    /**
     * Sets the x-offset of the overlay panel in relation to the trigger's top start corner.
     * This must be adjusted to align the selected option text over the trigger text when
     * the panel opens. Will change based on LTR or RTL text direction. Note that the offset
     * can't be calculated until the panel has been attached, because we need to know the
     * content width in order to constrain the panel within the viewport.
     */
    _calculateOverlayOffsetX() {
        const overlayRect = this._overlayDir.overlayRef.overlayElement.getBoundingClientRect();
        const viewportSize = this._viewportRuler.getViewportSize();
        const isRtl = this._isRtl();
        const paddingWidth = this.multiple ? SELECT_MULTIPLE_PANEL_PADDING_X + SELECT_PANEL_PADDING_X :
            SELECT_PANEL_PADDING_X * 2;
        let offsetX;
        // Adjust the offset, depending on the option padding.
        if (this.multiple) {
            offsetX = SELECT_MULTIPLE_PANEL_PADDING_X;
        }
        else if (this.disableOptionCentering) {
            offsetX = SELECT_PANEL_PADDING_X;
        }
        else {
            let selected = this._selectionModel.selected[0] || this.options.first;
            offsetX = selected && selected.group ? SELECT_PANEL_INDENT_PADDING_X : SELECT_PANEL_PADDING_X;
        }
        // Invert the offset in LTR.
        if (!isRtl) {
            offsetX *= -1;
        }
        // Determine how much the select overflows on each side.
        const leftOverflow = 0 - (overlayRect.left + offsetX - (isRtl ? paddingWidth : 0));
        const rightOverflow = overlayRect.right + offsetX - viewportSize.width
            + (isRtl ? 0 : paddingWidth);
        // If the element overflows on either side, reduce the offset to allow it to fit.
        if (leftOverflow > 0) {
            offsetX += leftOverflow + SELECT_PANEL_VIEWPORT_PADDING;
        }
        else if (rightOverflow > 0) {
            offsetX -= rightOverflow + SELECT_PANEL_VIEWPORT_PADDING;
        }
        // Set the offset directly in order to avoid having to go through change detection and
        // potentially triggering "changed after it was checked" errors. Round the value to avoid
        // blurry content in some browsers.
        this._overlayDir.offsetX = Math.round(offsetX);
        this._overlayDir.overlayRef.updatePosition();
    }
    /**
     * Calculates the y-offset of the select's overlay panel in relation to the
     * top start corner of the trigger. It has to be adjusted in order for the
     * selected option to be aligned over the trigger when the panel opens.
     */
    _calculateOverlayOffsetY(selectedIndex, scrollBuffer, maxScroll) {
        const itemHeight = this._getItemHeight();
        const optionHeightAdjustment = (itemHeight - this._triggerRect.height) / 2;
        const maxOptionsDisplayed = Math.floor(SELECT_PANEL_MAX_HEIGHT / itemHeight);
        let optionOffsetFromPanelTop;
        // Disable offset if requested by user by returning 0 as value to offset
        if (this.disableOptionCentering) {
            return 0;
        }
        if (this._scrollTop === 0) {
            optionOffsetFromPanelTop = selectedIndex * itemHeight;
        }
        else if (this._scrollTop === maxScroll) {
            const firstDisplayedIndex = this._getItemCount() - maxOptionsDisplayed;
            const selectedDisplayIndex = selectedIndex - firstDisplayedIndex;
            // The first item is partially out of the viewport. Therefore we need to calculate what
            // portion of it is shown in the viewport and account for it in our offset.
            let partialItemHeight = itemHeight - (this._getItemCount() * itemHeight - SELECT_PANEL_MAX_HEIGHT) % itemHeight;
            // Because the panel height is longer than the height of the options alone,
            // there is always extra padding at the top or bottom of the panel. When
            // scrolled to the very bottom, this padding is at the top of the panel and
            // must be added to the offset.
            optionOffsetFromPanelTop = selectedDisplayIndex * itemHeight + partialItemHeight;
        }
        else {
            // If the option was scrolled to the middle of the panel using a scroll buffer,
            // its offset will be the scroll buffer minus the half height that was added to
            // center it.
            optionOffsetFromPanelTop = scrollBuffer - itemHeight / 2;
        }
        // The final offset is the option's offset from the top, adjusted for the height difference,
        // multiplied by -1 to ensure that the overlay moves in the correct direction up the page.
        // The value is rounded to prevent some browsers from blurring the content.
        return Math.round(optionOffsetFromPanelTop * -1 - optionHeightAdjustment);
    }
    /**
     * Checks that the attempted overlay position will fit within the viewport.
     * If it will not fit, tries to adjust the scroll position and the associated
     * y-offset so the panel can open fully on-screen. If it still won't fit,
     * sets the offset back to 0 to allow the fallback position to take over.
     */
    _checkOverlayWithinViewport(maxScroll) {
        const itemHeight = this._getItemHeight();
        const viewportSize = this._viewportRuler.getViewportSize();
        const topSpaceAvailable = this._triggerRect.top - SELECT_PANEL_VIEWPORT_PADDING;
        const bottomSpaceAvailable = viewportSize.height - this._triggerRect.bottom - SELECT_PANEL_VIEWPORT_PADDING;
        const panelHeightTop = Math.abs(this._offsetY);
        const totalPanelHeight = Math.min(this._getItemCount() * itemHeight, SELECT_PANEL_MAX_HEIGHT);
        const panelHeightBottom = totalPanelHeight - panelHeightTop - this._triggerRect.height;
        if (panelHeightBottom > bottomSpaceAvailable) {
            this._adjustPanelUp(panelHeightBottom, bottomSpaceAvailable);
        }
        else if (panelHeightTop > topSpaceAvailable) {
            this._adjustPanelDown(panelHeightTop, topSpaceAvailable, maxScroll);
        }
        else {
            this._transformOrigin = this._getOriginBasedOnOption();
        }
    }
    /** Adjusts the overlay panel up to fit in the viewport. */
    _adjustPanelUp(panelHeightBottom, bottomSpaceAvailable) {
        // Browsers ignore fractional scroll offsets, so we need to round.
        const distanceBelowViewport = Math.round(panelHeightBottom - bottomSpaceAvailable);
        // Scrolls the panel up by the distance it was extending past the boundary, then
        // adjusts the offset by that amount to move the panel up into the viewport.
        this._scrollTop -= distanceBelowViewport;
        this._offsetY -= distanceBelowViewport;
        this._transformOrigin = this._getOriginBasedOnOption();
        // If the panel is scrolled to the very top, it won't be able to fit the panel
        // by scrolling, so set the offset to 0 to allow the fallback position to take
        // effect.
        if (this._scrollTop <= 0) {
            this._scrollTop = 0;
            this._offsetY = 0;
            this._transformOrigin = `50% bottom 0px`;
        }
    }
    /** Adjusts the overlay panel down to fit in the viewport. */
    _adjustPanelDown(panelHeightTop, topSpaceAvailable, maxScroll) {
        // Browsers ignore fractional scroll offsets, so we need to round.
        const distanceAboveViewport = Math.round(panelHeightTop - topSpaceAvailable);
        // Scrolls the panel down by the distance it was extending past the boundary, then
        // adjusts the offset by that amount to move the panel down into the viewport.
        this._scrollTop += distanceAboveViewport;
        this._offsetY += distanceAboveViewport;
        this._transformOrigin = this._getOriginBasedOnOption();
        // If the panel is scrolled to the very bottom, it won't be able to fit the
        // panel by scrolling, so set the offset to 0 to allow the fallback position
        // to take effect.
        if (this._scrollTop >= maxScroll) {
            this._scrollTop = maxScroll;
            this._offsetY = 0;
            this._transformOrigin = `50% top 0px`;
            return;
        }
    }
    /** Calculates the scroll position and x- and y-offsets of the overlay panel. */
    _calculateOverlayPosition() {
        const itemHeight = this._getItemHeight();
        const items = this._getItemCount();
        const panelHeight = Math.min(items * itemHeight, SELECT_PANEL_MAX_HEIGHT);
        const scrollContainerHeight = items * itemHeight;
        // The farthest the panel can be scrolled before it hits the bottom
        const maxScroll = scrollContainerHeight - panelHeight;
        // If no value is selected we open the popup to the first item.
        let selectedOptionOffset;
        if (this.empty) {
            selectedOptionOffset = 0;
        }
        else {
            selectedOptionOffset =
                Math.max(this.options.toArray().indexOf(this._selectionModel.selected[0]), 0);
        }
        selectedOptionOffset += (0,fesm2015_core/* _countGroupLabelsBeforeOption */.CB)(selectedOptionOffset, this.options, this.optionGroups);
        // We must maintain a scroll buffer so the selected option will be scrolled to the
        // center of the overlay panel rather than the top.
        const scrollBuffer = panelHeight / 2;
        this._scrollTop = this._calculateOverlayScroll(selectedOptionOffset, scrollBuffer, maxScroll);
        this._offsetY = this._calculateOverlayOffsetY(selectedOptionOffset, scrollBuffer, maxScroll);
        this._checkOverlayWithinViewport(maxScroll);
    }
    /** Sets the transform origin point based on the selected option. */
    _getOriginBasedOnOption() {
        const itemHeight = this._getItemHeight();
        const optionHeightAdjustment = (itemHeight - this._triggerRect.height) / 2;
        const originY = Math.abs(this._offsetY) - optionHeightAdjustment + itemHeight / 2;
        return `50% ${originY}px 0px`;
    }
    /** Calculates the height of the select's options. */
    _getItemHeight() {
        return this._triggerFontSize * SELECT_ITEM_HEIGHT_EM;
    }
    /** Calculates the amount of items in the select. This includes options and group labels. */
    _getItemCount() {
        return this.options.length + this.optionGroups.length;
    }
}
MatSelect.ɵfac = /*@__PURE__*/ function () { let ɵMatSelect_BaseFactory; return function MatSelect_Factory(t) { return (ɵMatSelect_BaseFactory || (ɵMatSelect_BaseFactory = core/* ɵɵgetInheritedFactory */.n5z(MatSelect)))(t || MatSelect); }; }();
MatSelect.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatSelect, selectors: [["mat-select"]], contentQueries: function MatSelect_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, MAT_SELECT_TRIGGER, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, fesm2015_core/* MatOption */.ey, 5);
        core/* ɵɵcontentQuery */.Suo(dirIndex, fesm2015_core/* MAT_OPTGROUP */.K7, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.customTrigger = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.options = _t);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.optionGroups = _t);
    } }, hostAttrs: ["role", "combobox", "aria-autocomplete", "none", "aria-haspopup", "true", 1, "mat-select"], hostVars: 20, hostBindings: function MatSelect_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("keydown", function MatSelect_keydown_HostBindingHandler($event) { return ctx._handleKeydown($event); })("focus", function MatSelect_focus_HostBindingHandler() { return ctx._onFocus(); })("blur", function MatSelect_blur_HostBindingHandler() { return ctx._onBlur(); });
    } if (rf & 2) {
        core/* ɵɵattribute */.uIk("id", ctx.id)("tabindex", ctx.tabIndex)("aria-controls", ctx.panelOpen ? ctx.id + "-panel" : null)("aria-expanded", ctx.panelOpen)("aria-label", ctx.ariaLabel || null)("aria-required", ctx.required.toString())("aria-disabled", ctx.disabled.toString())("aria-invalid", ctx.errorState)("aria-describedby", ctx._ariaDescribedby || null)("aria-activedescendant", ctx._getAriaActiveDescendant());
        core/* ɵɵclassProp */.ekj("mat-select-disabled", ctx.disabled)("mat-select-invalid", ctx.errorState)("mat-select-required", ctx.required)("mat-select-empty", ctx.empty)("mat-select-multiple", ctx.multiple);
    } }, inputs: { disabled: "disabled", disableRipple: "disableRipple", tabIndex: "tabIndex" }, exportAs: ["matSelect"], features: [core/* ɵɵProvidersFeature */._Bn([
            { provide: MatFormFieldControl, useExisting: MatSelect },
            { provide: fesm2015_core/* MAT_OPTION_PARENT_COMPONENT */.HF, useExisting: MatSelect }
        ]), core/* ɵɵInheritDefinitionFeature */.qOj], ngContentSelectors: select_c3, decls: 9, vars: 12, consts: [["cdk-overlay-origin", "", 1, "mat-select-trigger", 3, "click"], ["origin", "cdkOverlayOrigin", "trigger", ""], [1, "mat-select-value", 3, "ngSwitch"], ["class", "mat-select-placeholder mat-select-min-line", 4, "ngSwitchCase"], ["class", "mat-select-value-text", 3, "ngSwitch", 4, "ngSwitchCase"], [1, "mat-select-arrow-wrapper"], [1, "mat-select-arrow"], ["cdk-connected-overlay", "", "cdkConnectedOverlayLockPosition", "", "cdkConnectedOverlayHasBackdrop", "", "cdkConnectedOverlayBackdropClass", "cdk-overlay-transparent-backdrop", 3, "cdkConnectedOverlayPanelClass", "cdkConnectedOverlayScrollStrategy", "cdkConnectedOverlayOrigin", "cdkConnectedOverlayOpen", "cdkConnectedOverlayPositions", "cdkConnectedOverlayMinWidth", "cdkConnectedOverlayOffsetY", "backdropClick", "attach", "detach"], [1, "mat-select-placeholder", "mat-select-min-line"], [1, "mat-select-value-text", 3, "ngSwitch"], ["class", "mat-select-min-line", 4, "ngSwitchDefault"], [4, "ngSwitchCase"], [1, "mat-select-min-line"], [1, "mat-select-panel-wrap"], ["role", "listbox", "tabindex", "-1", 3, "ngClass", "keydown"], ["panel", ""]], template: function MatSelect_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(select_c2);
        core/* ɵɵelementStart */.TgZ(0, "div", 0, 1);
        core/* ɵɵlistener */.NdJ("click", function MatSelect_Template_div_click_0_listener() { return ctx.toggle(); });
        core/* ɵɵelementStart */.TgZ(3, "div", 2);
        core/* ɵɵtemplate */.YNc(4, MatSelect_span_4_Template, 2, 1, "span", 3);
        core/* ɵɵtemplate */.YNc(5, MatSelect_span_5_Template, 3, 2, "span", 4);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(6, "div", 5);
        core/* ɵɵelement */._UZ(7, "div", 6);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(8, MatSelect_ng_template_8_Template, 4, 14, "ng-template", 7);
        core/* ɵɵlistener */.NdJ("backdropClick", function MatSelect_Template_ng_template_backdropClick_8_listener() { return ctx.close(); })("attach", function MatSelect_Template_ng_template_attach_8_listener() { return ctx._onAttached(); })("detach", function MatSelect_Template_ng_template_detach_8_listener() { return ctx.close(); });
    } if (rf & 2) {
        const _r0 = core/* ɵɵreference */.MAs(1);
        core/* ɵɵattribute */.uIk("aria-owns", ctx.panelOpen ? ctx.id + "-panel" : null);
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("ngSwitch", ctx.empty);
        core/* ɵɵattribute */.uIk("id", ctx._valueId);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngSwitchCase", true);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngSwitchCase", false);
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("cdkConnectedOverlayPanelClass", ctx._overlayPanelClass)("cdkConnectedOverlayScrollStrategy", ctx._scrollStrategy)("cdkConnectedOverlayOrigin", _r0)("cdkConnectedOverlayOpen", ctx.panelOpen)("cdkConnectedOverlayPositions", ctx._positions)("cdkConnectedOverlayMinWidth", ctx._triggerRect == null ? null : ctx._triggerRect.width)("cdkConnectedOverlayOffsetY", ctx._offsetY);
    } }, directives: [overlay/* CdkOverlayOrigin */.xu, common/* NgSwitch */.RF, common/* NgSwitchCase */.n9, overlay/* CdkConnectedOverlay */.pI, common/* NgSwitchDefault */.ED, common/* NgClass */.mk], styles: [".mat-select{display:inline-block;width:100%;outline:none}.mat-select-trigger{display:inline-table;cursor:pointer;position:relative;box-sizing:border-box}.mat-select-disabled .mat-select-trigger{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;cursor:default}.mat-select-value{display:table-cell;max-width:0;width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.mat-select-value-text{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.mat-select-arrow-wrapper{display:table-cell;vertical-align:middle}.mat-form-field-appearance-fill .mat-select-arrow-wrapper{transform:translateY(-50%)}.mat-form-field-appearance-outline .mat-select-arrow-wrapper{transform:translateY(-25%)}.mat-form-field-appearance-standard.mat-form-field-has-label .mat-select:not(.mat-select-empty) .mat-select-arrow-wrapper{transform:translateY(-50%)}.mat-form-field-appearance-standard .mat-select.mat-select-empty .mat-select-arrow-wrapper{transition:transform 400ms cubic-bezier(0.25, 0.8, 0.25, 1)}._mat-animation-noopable.mat-form-field-appearance-standard .mat-select.mat-select-empty .mat-select-arrow-wrapper{transition:none}.mat-select-arrow{width:0;height:0;border-left:5px solid transparent;border-right:5px solid transparent;border-top:5px solid;margin:0 4px}.mat-select-panel-wrap{flex-basis:100%}.mat-select-panel{min-width:112px;max-width:280px;overflow:auto;-webkit-overflow-scrolling:touch;padding-top:0;padding-bottom:0;max-height:256px;min-width:100%;border-radius:4px;outline:0}.cdk-high-contrast-active .mat-select-panel{outline:solid 1px}.mat-select-panel .mat-optgroup-label,.mat-select-panel .mat-option{font-size:inherit;line-height:3em;height:3em}.mat-form-field-type-mat-select:not(.mat-form-field-disabled) .mat-form-field-flex{cursor:pointer}.mat-form-field-type-mat-select .mat-form-field-label{width:calc(100% - 18px)}.mat-select-placeholder{transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}._mat-animation-noopable .mat-select-placeholder{transition:none}.mat-form-field-hide-placeholder .mat-select-placeholder{color:transparent;-webkit-text-fill-color:transparent;transition:none;display:block}.mat-select-min-line:empty::before{content:\" \";white-space:pre;width:1px;display:inline-block;opacity:0}\n"], encapsulation: 2, data: { animation: [
            matSelectAnimations.transformPanelWrap,
            matSelectAnimations.transformPanel
        ] }, changeDetection: 0 });
MatSelect.propDecorators = {
    options: [{ type: core/* ContentChildren */.AcB, args: [fesm2015_core/* MatOption */.ey, { descendants: true },] }],
    optionGroups: [{ type: core/* ContentChildren */.AcB, args: [fesm2015_core/* MAT_OPTGROUP */.K7, { descendants: true },] }],
    customTrigger: [{ type: core/* ContentChild */.aQ5, args: [MAT_SELECT_TRIGGER,] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSelect, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-select',
                exportAs: 'matSelect',
                template: "<!--\n Note that the select trigger element specifies `aria-owns` pointing to the listbox overlay.\n While aria-owns is not required for the ARIA 1.2 `role=\"combobox\"` interaction pattern,\n it fixes an issue with VoiceOver when the select appears inside of an `aria-model=\"true\"`\n element (e.g. a dialog). Without this `aria-owns`, the `aria-modal` on a dialog prevents\n VoiceOver from \"seeing\" the select's listbox overlay for aria-activedescendant.\n Using `aria-owns` re-parents the select overlay so that it works again.\n See https://github.com/angular/components/issues/20694\n-->\n<div cdk-overlay-origin\n     [attr.aria-owns]=\"panelOpen ? id + '-panel' : null\"\n     class=\"mat-select-trigger\"\n     (click)=\"toggle()\"\n     #origin=\"cdkOverlayOrigin\"\n     #trigger>\n  <div class=\"mat-select-value\" [ngSwitch]=\"empty\" [attr.id]=\"_valueId\">\n    <span class=\"mat-select-placeholder mat-select-min-line\" *ngSwitchCase=\"true\">{{placeholder}}</span>\n    <span class=\"mat-select-value-text\" *ngSwitchCase=\"false\" [ngSwitch]=\"!!customTrigger\">\n      <span class=\"mat-select-min-line\" *ngSwitchDefault>{{triggerValue}}</span>\n      <ng-content select=\"mat-select-trigger\" *ngSwitchCase=\"true\"></ng-content>\n    </span>\n  </div>\n\n  <div class=\"mat-select-arrow-wrapper\"><div class=\"mat-select-arrow\"></div></div>\n</div>\n\n<ng-template\n  cdk-connected-overlay\n  cdkConnectedOverlayLockPosition\n  cdkConnectedOverlayHasBackdrop\n  cdkConnectedOverlayBackdropClass=\"cdk-overlay-transparent-backdrop\"\n  [cdkConnectedOverlayPanelClass]=\"_overlayPanelClass\"\n  [cdkConnectedOverlayScrollStrategy]=\"_scrollStrategy\"\n  [cdkConnectedOverlayOrigin]=\"origin\"\n  [cdkConnectedOverlayOpen]=\"panelOpen\"\n  [cdkConnectedOverlayPositions]=\"_positions\"\n  [cdkConnectedOverlayMinWidth]=\"_triggerRect?.width!\"\n  [cdkConnectedOverlayOffsetY]=\"_offsetY\"\n  (backdropClick)=\"close()\"\n  (attach)=\"_onAttached()\"\n  (detach)=\"close()\">\n  <div class=\"mat-select-panel-wrap\" [@transformPanelWrap]>\n    <div\n      #panel\n      role=\"listbox\"\n      tabindex=\"-1\"\n      class=\"mat-select-panel {{ _getPanelTheme() }}\"\n      [attr.id]=\"id + '-panel'\"\n      [attr.aria-multiselectable]=\"multiple\"\n      [attr.aria-label]=\"ariaLabel || null\"\n      [attr.aria-labelledby]=\"_getPanelAriaLabelledby()\"\n      [ngClass]=\"panelClass\"\n      [@transformPanel]=\"multiple ? 'showing-multiple' : 'showing'\"\n      (@transformPanel.done)=\"_panelDoneAnimatingStream.next($event.toState)\"\n      [style.transformOrigin]=\"_transformOrigin\"\n      [style.font-size.px]=\"_triggerFontSize\"\n      (keydown)=\"_handleKeydown($event)\">\n      <ng-content></ng-content>\n    </div>\n  </div>\n</ng-template>\n",
                inputs: ['disabled', 'disableRipple', 'tabIndex'],
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                host: {
                    'role': 'combobox',
                    'aria-autocomplete': 'none',
                    // TODO(crisbeto): the value for aria-haspopup should be `listbox`, but currently it's difficult
                    // to sync into Google, because of an outdated automated a11y check which flags it as an invalid
                    // value. At some point we should try to switch it back to being `listbox`.
                    'aria-haspopup': 'true',
                    'class': 'mat-select',
                    '[attr.id]': 'id',
                    '[attr.tabindex]': 'tabIndex',
                    '[attr.aria-controls]': 'panelOpen ? id + "-panel" : null',
                    '[attr.aria-expanded]': 'panelOpen',
                    '[attr.aria-label]': 'ariaLabel || null',
                    '[attr.aria-required]': 'required.toString()',
                    '[attr.aria-disabled]': 'disabled.toString()',
                    '[attr.aria-invalid]': 'errorState',
                    '[attr.aria-describedby]': '_ariaDescribedby || null',
                    '[attr.aria-activedescendant]': '_getAriaActiveDescendant()',
                    '[class.mat-select-disabled]': 'disabled',
                    '[class.mat-select-invalid]': 'errorState',
                    '[class.mat-select-required]': 'required',
                    '[class.mat-select-empty]': 'empty',
                    '[class.mat-select-multiple]': 'multiple',
                    '(keydown)': '_handleKeydown($event)',
                    '(focus)': '_onFocus()',
                    '(blur)': '_onBlur()'
                },
                animations: [
                    matSelectAnimations.transformPanelWrap,
                    matSelectAnimations.transformPanel
                ],
                providers: [
                    { provide: MatFormFieldControl, useExisting: MatSelect },
                    { provide: fesm2015_core/* MAT_OPTION_PARENT_COMPONENT */.HF, useExisting: MatSelect }
                ],
                styles: [".mat-select{display:inline-block;width:100%;outline:none}.mat-select-trigger{display:inline-table;cursor:pointer;position:relative;box-sizing:border-box}.mat-select-disabled .mat-select-trigger{-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;cursor:default}.mat-select-value{display:table-cell;max-width:0;width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.mat-select-value-text{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.mat-select-arrow-wrapper{display:table-cell;vertical-align:middle}.mat-form-field-appearance-fill .mat-select-arrow-wrapper{transform:translateY(-50%)}.mat-form-field-appearance-outline .mat-select-arrow-wrapper{transform:translateY(-25%)}.mat-form-field-appearance-standard.mat-form-field-has-label .mat-select:not(.mat-select-empty) .mat-select-arrow-wrapper{transform:translateY(-50%)}.mat-form-field-appearance-standard .mat-select.mat-select-empty .mat-select-arrow-wrapper{transition:transform 400ms cubic-bezier(0.25, 0.8, 0.25, 1)}._mat-animation-noopable.mat-form-field-appearance-standard .mat-select.mat-select-empty .mat-select-arrow-wrapper{transition:none}.mat-select-arrow{width:0;height:0;border-left:5px solid transparent;border-right:5px solid transparent;border-top:5px solid;margin:0 4px}.mat-select-panel-wrap{flex-basis:100%}.mat-select-panel{min-width:112px;max-width:280px;overflow:auto;-webkit-overflow-scrolling:touch;padding-top:0;padding-bottom:0;max-height:256px;min-width:100%;border-radius:4px;outline:0}.cdk-high-contrast-active .mat-select-panel{outline:solid 1px}.mat-select-panel .mat-optgroup-label,.mat-select-panel .mat-option{font-size:inherit;line-height:3em;height:3em}.mat-form-field-type-mat-select:not(.mat-form-field-disabled) .mat-form-field-flex{cursor:pointer}.mat-form-field-type-mat-select .mat-form-field-label{width:calc(100% - 18px)}.mat-select-placeholder{transition:color 400ms 133.3333333333ms cubic-bezier(0.25, 0.8, 0.25, 1)}._mat-animation-noopable .mat-select-placeholder{transition:none}.mat-form-field-hide-placeholder .mat-select-placeholder{color:transparent;-webkit-text-fill-color:transparent;transition:none;display:block}.mat-select-min-line:empty::before{content:\" \";white-space:pre;width:1px;display:inline-block;opacity:0}\n"]
            }]
    }], null, { options: [{
            type: core/* ContentChildren */.AcB,
            args: [fesm2015_core/* MatOption */.ey, { descendants: true }]
        }], optionGroups: [{
            type: core/* ContentChildren */.AcB,
            args: [fesm2015_core/* MAT_OPTGROUP */.K7, { descendants: true }]
        }], customTrigger: [{
            type: core/* ContentChild */.aQ5,
            args: [MAT_SELECT_TRIGGER]
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatSelectModule {
}
MatSelectModule.ɵfac = function MatSelectModule_Factory(t) { return new (t || MatSelectModule)(); };
MatSelectModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatSelectModule });
MatSelectModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [MAT_SELECT_SCROLL_STRATEGY_PROVIDER], imports: [[
            common/* CommonModule */.ez,
            overlay/* OverlayModule */.U8,
            fesm2015_core/* MatOptionModule */.Ng,
            fesm2015_core/* MatCommonModule */.BQ,
        ], scrolling/* CdkScrollableModule */.ZD,
        MatFormFieldModule,
        fesm2015_core/* MatOptionModule */.Ng,
        fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatSelectModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [
                    common/* CommonModule */.ez,
                    overlay/* OverlayModule */.U8,
                    fesm2015_core/* MatOptionModule */.Ng,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                exports: [
                    scrolling/* CdkScrollableModule */.ZD,
                    MatFormFieldModule,
                    MatSelect,
                    MatSelectTrigger,
                    fesm2015_core/* MatOptionModule */.Ng,
                    fesm2015_core/* MatCommonModule */.BQ
                ],
                declarations: [MatSelect, MatSelectTrigger],
                providers: [MAT_SELECT_SCROLL_STRATEGY_PROVIDER]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatSelectModule, { declarations: function () { return [MatSelect, MatSelectTrigger]; }, imports: function () { return [common/* CommonModule */.ez,
        overlay/* OverlayModule */.U8,
        fesm2015_core/* MatOptionModule */.Ng,
        fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [scrolling/* CdkScrollableModule */.ZD,
        MatFormFieldModule, MatSelect, MatSelectTrigger, fesm2015_core/* MatOptionModule */.Ng,
        fesm2015_core/* MatCommonModule */.BQ]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=select.js.map
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/radio.js
var fesm2015_radio = __webpack_require__(2613);
;// CONCATENATED MODULE: ./src/app/pages/race-analysis/race-analysis.component.ts




















const race_analysis_component_c0 = ["chart"];
function RaceAnalysisComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 18);
    core/* ɵɵelementStart */.TgZ(2, "div", 19);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 20);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 21);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r7 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r7.comment);
} }
function RaceAnalysisComponent_ng_template_22_div_0_tr_15_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 28);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 29);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r10 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r10.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r10.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r10.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r10.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r10.fastestLap);
} }
function RaceAnalysisComponent_ng_template_22_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r13 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 23);
    core/* ɵɵlistener */.NdJ("matSortChange", function RaceAnalysisComponent_ng_template_22_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r13); const ctx_r12 = core/* ɵɵnextContext */.oxw(2); return ctx_r12.sortResultData($event); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 24);
    core/* ɵɵtext */._uU(5, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "th", 25);
    core/* ɵɵtext */._uU(7, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "th", 26);
    core/* ɵɵtext */._uU(9, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "th", 25);
    core/* ɵɵtext */._uU(11, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 27);
    core/* ɵɵtext */._uU(13, "Fastest lap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "tbody");
    core/* ɵɵtemplate */.YNc(15, RaceAnalysisComponent_ng_template_22_div_0_tr_15_Template, 11, 5, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r8 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(15);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r8.driversTab1);
} }
function RaceAnalysisComponent_ng_template_22_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, RaceAnalysisComponent_ng_template_22_div_0_Template, 16, 1, "div", 22);
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r2.driversTab1 !== undefined);
} }
function RaceAnalysisComponent_ng_template_24_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 31);
    core/* ɵɵelement */._UZ(4, "apx-chart", 35);
    core/* ɵɵelement */._UZ(5, "br");
    core/* ɵɵtext */._uU(6, "*Laps which are 5+ seconds slower than fastest driver of that lap are removed from the chart");
    core/* ɵɵelement */._UZ(7, "br");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r14 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("series", ctx_r14.lapTimesChart.series)("chart", ctx_r14.lapTimesChart.chart)("dataLabels", ctx_r14.defDataLabels)("grid", ctx_r14.defGrid)("stroke", ctx_r14.defStroke)("title", ctx_r14.lapTimesChart.title)("yaxis", ctx_r14.lapTimesChart.yAxis)("xaxis", ctx_r14.lapXaxis1)("tooltip", ctx_r14.lapTimesChart.tooltip);
} }
function RaceAnalysisComponent_ng_template_24_div_14_mat_option_10_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-option", 45);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const driver_r20 = ctx.$implicit;
    const i_r21 = ctx.index;
    core/* ɵɵproperty */.Q6J("value", i_r21);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate1 */.hij(" ", driver_r20.Name, " ");
} }
function RaceAnalysisComponent_ng_template_24_div_14_mat_option_15_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-option", 45);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const driver_r22 = ctx.$implicit;
    const i_r23 = ctx.index;
    core/* ɵɵproperty */.Q6J("value", i_r23);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate1 */.hij(" ", driver_r22.Name, " ");
} }
function RaceAnalysisComponent_ng_template_24_div_14_tr_26_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 46);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td", 47);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 48);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td", 49);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const row_r24 = ctx.$implicit;
    const i_r25 = ctx.index;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(i_r25 + 1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(row_r24.driver1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngClass", row_r24.difference <= 0 ? "bg-success" : "bg-danger");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(row_r24.differenceString);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(row_r24.driver2);
} }
function RaceAnalysisComponent_ng_template_24_div_14_Template(rf, ctx) { if (rf & 1) {
    const _r27 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 37);
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelement */._UZ(7, "th", 38);
    core/* ɵɵelementStart */.TgZ(8, "th", 39);
    core/* ɵɵelementStart */.TgZ(9, "mat-select", 40);
    core/* ɵɵlistener */.NdJ("selectionChange", function RaceAnalysisComponent_ng_template_24_div_14_Template_mat_select_selectionChange_9_listener() { core/* ɵɵrestoreView */.CHM(_r27); const ctx_r26 = core/* ɵɵnextContext */.oxw(2); return ctx_r26.updateComparisonTable(); })("valueChange", function RaceAnalysisComponent_ng_template_24_div_14_Template_mat_select_valueChange_9_listener($event) { core/* ɵɵrestoreView */.CHM(_r27); const ctx_r28 = core/* ɵɵnextContext */.oxw(2); return ctx_r28.selectedDriver1 = $event; });
    core/* ɵɵtemplate */.YNc(10, RaceAnalysisComponent_ng_template_24_div_14_mat_option_10_Template, 2, 2, "mat-option", 41);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 42);
    core/* ɵɵtext */._uU(12, "vs");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 39);
    core/* ɵɵelementStart */.TgZ(14, "mat-select", 40);
    core/* ɵɵlistener */.NdJ("selectionChange", function RaceAnalysisComponent_ng_template_24_div_14_Template_mat_select_selectionChange_14_listener() { core/* ɵɵrestoreView */.CHM(_r27); const ctx_r29 = core/* ɵɵnextContext */.oxw(2); return ctx_r29.updateComparisonTable(); })("valueChange", function RaceAnalysisComponent_ng_template_24_div_14_Template_mat_select_valueChange_14_listener($event) { core/* ɵɵrestoreView */.CHM(_r27); const ctx_r30 = core/* ɵɵnextContext */.oxw(2); return ctx_r30.selectedDriver2 = $event; });
    core/* ɵɵtemplate */.YNc(15, RaceAnalysisComponent_ng_template_24_div_14_mat_option_15_Template, 2, 2, "mat-option", 41);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "tr");
    core/* ɵɵelementStart */.TgZ(17, "th", 38);
    core/* ɵɵtext */._uU(18, "Lap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 43);
    core/* ɵɵtext */._uU(20, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 42);
    core/* ɵɵtext */._uU(22, "Diff");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "th", 44);
    core/* ɵɵtext */._uU(24, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(25, "tbody");
    core/* ɵɵtemplate */.YNc(26, RaceAnalysisComponent_ng_template_24_div_14_tr_26_Template, 9, 5, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r15 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(9);
    core/* ɵɵproperty */.Q6J("value", ctx_r15.selectedDriver1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r15.drivers);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("value", ctx_r15.selectedDriver2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r15.drivers);
    core/* ɵɵadvance */.xp6(11);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r15.comparisonTable.rows);
} }
function RaceAnalysisComponent_ng_template_24_div_15_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 50);
    core/* ɵɵelementStart */.TgZ(4, "h2");
    core/* ɵɵtext */._uU(5, "Lap times will be available here soon...");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function RaceAnalysisComponent_ng_template_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 31);
    core/* ɵɵelement */._UZ(4, "apx-chart", 32);
    core/* ɵɵelement */._UZ(5, "br");
    core/* ɵɵelement */._UZ(6, "br");
    core/* ɵɵelement */._UZ(7, "br");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "div", 3);
    core/* ɵɵelementStart */.TgZ(9, "div", 4);
    core/* ɵɵelementStart */.TgZ(10, "div", 30);
    core/* ɵɵelementStart */.TgZ(11, "div", 31);
    core/* ɵɵelement */._UZ(12, "apx-chart", 33);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(13, RaceAnalysisComponent_ng_template_24_div_13_Template, 8, 9, "div", 34);
    core/* ɵɵtemplate */.YNc(14, RaceAnalysisComponent_ng_template_24_div_14_Template, 27, 5, "div", 34);
    core/* ɵɵtemplate */.YNc(15, RaceAnalysisComponent_ng_template_24_div_15_Template, 6, 0, "div", 34);
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("series", ctx_r3.lapPosChart.series)("chart", ctx_r3.defChart)("dataLabels", ctx_r3.defDataLabels)("grid", ctx_r3.defGrid)("stroke", ctx_r3.defStroke)("title", ctx_r3.lapPosChart.title)("yaxis", ctx_r3.reverseYAxis)("xaxis", ctx_r3.lapXaxis);
    core/* ɵɵadvance */.xp6(8);
    core/* ɵɵproperty */.Q6J("title", ctx_r3.lapTyreChart.title)("series", ctx_r3.lapTyreChart.series)("chart", ctx_r3.lapTyreChart.chart)("fill", ctx_r3.lapTyreChart.fill)("legend", ctx_r3.lapTyreChart.legend)("plotOptions", ctx_r3.lapTyreChart.plotOptions)("xaxis", ctx_r3.lapTyreChart.xaxis)("tooltip", ctx_r3.lapTyreChart.tooltip);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.status == 1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.status == 1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.status == 2);
} }
function RaceAnalysisComponent_ng_template_26_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 51);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 31);
    core/* ɵɵelement */._UZ(4, "apx-chart", 52);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 51);
    core/* ɵɵelementStart */.TgZ(6, "div", 30);
    core/* ɵɵelementStart */.TgZ(7, "div", 31);
    core/* ɵɵelement */._UZ(8, "apx-chart", 53);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "div", 51);
    core/* ɵɵelementStart */.TgZ(10, "div", 30);
    core/* ɵɵelementStart */.TgZ(11, "div", 31);
    core/* ɵɵelement */._UZ(12, "apx-chart", 52);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "div", 51);
    core/* ɵɵelementStart */.TgZ(14, "div", 30);
    core/* ɵɵelementStart */.TgZ(15, "div", 31);
    core/* ɵɵelement */._UZ(16, "apx-chart", 52);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "div", 51);
    core/* ɵɵelementStart */.TgZ(18, "div", 30);
    core/* ɵɵelementStart */.TgZ(19, "div", 31);
    core/* ɵɵelement */._UZ(20, "apx-chart", 52);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "div", 51);
    core/* ɵɵelementStart */.TgZ(22, "div", 30);
    core/* ɵɵelementStart */.TgZ(23, "div", 31);
    core/* ɵɵelement */._UZ(24, "apx-chart", 52);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.performanceChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("stroke", ctx_r4.defStroke)("title", ctx_r4.performanceChart.title)("plotOptions", ctx_r4.barPlotOptions);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.throttleChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("title", ctx_r4.throttleChart.title)("plotOptions", ctx_r4.barPlotOptions);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.brakeChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("stroke", ctx_r4.defStroke)("title", ctx_r4.brakeChart.title)("plotOptions", ctx_r4.barPlotOptions);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.steeringChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("stroke", ctx_r4.defStroke)("title", ctx_r4.steeringChart.title)("plotOptions", ctx_r4.barPlotOptions);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.gforceLongChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("stroke", ctx_r4.defStroke)("title", ctx_r4.gforceLongChart.title)("plotOptions", ctx_r4.barPlotOptions);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r4.defLegend)("series", ctx_r4.gforceLatChart.series)("chart", ctx_r4.barChart)("xaxis", ctx_r4.driverCodesXaxis)("dataLabels", ctx_r4.defDataLabels)("grid", ctx_r4.defGrid)("stroke", ctx_r4.defStroke)("title", ctx_r4.gforceLatChart.title)("plotOptions", ctx_r4.barPlotOptions);
} }
function RaceAnalysisComponent_ng_template_28_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 31);
    core/* ɵɵelement */._UZ(4, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 3);
    core/* ɵɵelementStart */.TgZ(6, "div", 4);
    core/* ɵɵelementStart */.TgZ(7, "div", 30);
    core/* ɵɵelementStart */.TgZ(8, "div", 31);
    core/* ɵɵelement */._UZ(9, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "div", 3);
    core/* ɵɵelementStart */.TgZ(11, "div", 4);
    core/* ɵɵelementStart */.TgZ(12, "div", 30);
    core/* ɵɵelementStart */.TgZ(13, "div", 31);
    core/* ɵɵelement */._UZ(14, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "div", 3);
    core/* ɵɵelementStart */.TgZ(16, "div", 4);
    core/* ɵɵelementStart */.TgZ(17, "div", 30);
    core/* ɵɵelementStart */.TgZ(18, "div", 31);
    core/* ɵɵelement */._UZ(19, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(20, "div", 3);
    core/* ɵɵelementStart */.TgZ(21, "div", 4);
    core/* ɵɵelementStart */.TgZ(22, "div", 30);
    core/* ɵɵelementStart */.TgZ(23, "div", 31);
    core/* ɵɵelement */._UZ(24, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(25, "div", 3);
    core/* ɵɵelementStart */.TgZ(26, "div", 4);
    core/* ɵɵelementStart */.TgZ(27, "div", 30);
    core/* ɵɵelementStart */.TgZ(28, "div", 31);
    core/* ɵɵelement */._UZ(29, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(30, "div", 3);
    core/* ɵɵelementStart */.TgZ(31, "div", 4);
    core/* ɵɵelementStart */.TgZ(32, "div", 30);
    core/* ɵɵelementStart */.TgZ(33, "div", 31);
    core/* ɵɵelement */._UZ(34, "apx-chart", 54);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.airTemperatureChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.airTemperatureChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.trackTemperatureChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.trackTemperatureChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.rainingChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.rainingChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.humidityChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.humidityChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.pressureChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.pressureChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.windspeedChart.series)("chart", ctx_r5.defChart)("xaxis", ctx_r5.defXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.windspeedChart.title);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("legend", ctx_r5.defLegend)("series", ctx_r5.windDirChart.series)("chart", ctx_r5.radarChart)("xaxis", ctx_r5.radarXaxis)("dataLabels", ctx_r5.defDataLabels)("grid", ctx_r5.defGrid)("stroke", ctx_r5.defStroke)("title", ctx_r5.windDirChart.title);
} }
function RaceAnalysisComponent_ng_template_30_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 61);
    core/* ɵɵelementStart */.TgZ(1, "div", 30);
    core/* ɵɵelementStart */.TgZ(2, "mat-button-toggle", 45);
    core/* ɵɵelementStart */.TgZ(3, "div", 62);
    core/* ɵɵelementStart */.TgZ(4, "p", 63);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(6, "img", 64);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "div", 65);
    core/* ɵɵelementStart */.TgZ(8, "p", 66);
    core/* ɵɵtext */._uU(9);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const driver_r32 = ctx.$implicit;
    const ctx_r31 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵstyleMap */.Akn("background-color: #" + driver_r32.Color);
    core/* ɵɵpropertyInterpolate */.s9C("value", driver_r32.Name);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(driver_r32.FullName);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵpropertyInterpolate */.s9C("src", "assets/img/drivers/" + ctx_r31.year + "/" + driver_r32.Initials + ".png", core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(driver_r32.Num);
} }
function RaceAnalysisComponent_ng_template_30_Template(rf, ctx) { if (rf & 1) {
    const _r34 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 3);
    core/* ɵɵelementStart */.TgZ(1, "div", 4);
    core/* ɵɵelementStart */.TgZ(2, "div", 30);
    core/* ɵɵelementStart */.TgZ(3, "div", 31);
    core/* ɵɵelementStart */.TgZ(4, "mat-radio-group", 55);
    core/* ɵɵlistener */.NdJ("ngModelChange", function RaceAnalysisComponent_ng_template_30_Template_mat_radio_group_ngModelChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r34); const ctx_r33 = core/* ɵɵnextContext */.oxw(); return ctx_r33.selectedTab5Radio = $event; });
    core/* ɵɵelementStart */.TgZ(5, "mat-radio-button", 56);
    core/* ɵɵlistener */.NdJ("change", function RaceAnalysisComponent_ng_template_30_Template_mat_radio_button_change_5_listener($event) { core/* ɵɵrestoreView */.CHM(_r34); const ctx_r35 = core/* ɵɵnextContext */.oxw(); return ctx_r35.radioChange($event); });
    core/* ɵɵtext */._uU(6, "By final position");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "mat-radio-button", 57);
    core/* ɵɵlistener */.NdJ("change", function RaceAnalysisComponent_ng_template_30_Template_mat_radio_button_change_7_listener($event) { core/* ɵɵrestoreView */.CHM(_r34); const ctx_r36 = core/* ɵɵnextContext */.oxw(); return ctx_r36.radioChange($event); });
    core/* ɵɵtext */._uU(8, "By starting position");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "span");
    core/* ɵɵelementStart */.TgZ(10, "mat-radio-button", 58);
    core/* ɵɵlistener */.NdJ("change", function RaceAnalysisComponent_ng_template_30_Template_mat_radio_button_change_10_listener($event) { core/* ɵɵrestoreView */.CHM(_r34); const ctx_r37 = core/* ɵɵnextContext */.oxw(); return ctx_r37.radioChange($event); });
    core/* ɵɵtext */._uU(11, "By fastest lap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "div", 59);
    core/* ɵɵtemplate */.YNc(13, RaceAnalysisComponent_ng_template_30_div_13_Template, 10, 7, "div", 60);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r6.selectedTab5Radio);
    core/* ɵɵadvance */.xp6(9);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r6.driversTab5);
} }
class RaceAnalysisComponent {
    constructor(restService, utilityService) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.steering = [];
        this.gforceLat = [];
        this.gforceLong = [];
        this.brake = [];
        this.performance = [];
        this.throttle = [];
        this.driverCodes = [];
        this.selectedTab5Radio = "position";
        this.driverCodesSortedByFinalPosition = [];
        this.lapTimesUpperLimit = 0;
        this.selectedDriver1 = 0;
        this.selectedDriver2 = 1;
        this.comparisonTable = new ComparisonTable();
        this.defLegend = {
            position: 'bottom',
            horizontalAlign: 'left',
        };
        this.defChart = {
            height: 350,
            type: 'line',
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.barChart = {
            height: 600,
            type: 'bar',
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.radarChart = {
            height: 350,
            type: 'radar',
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.defDataLabels = {
            enabled: false
        };
        this.defStroke = {
            curve: 'straight'
        };
        this.defGrid = {
            borderColor: '#e7e7e7',
            row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            }
        };
        this.reverseYAxis = {
            min: 1,
            // tickAmount:6 ,
            decimalsInFloat: 0,
            reversed: true,
            axisTicks: {
                show: true
            },
            axisBorder: {
                show: true,
                color: "#008FFB"
            },
            title: {
                text: "Position",
            }
        };
        this.radarXaxis = {
            categories: [360, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340],
            tickAmount: 1,
            title: {
                text: 'Degrees'
            },
        };
        this.lapXaxis = {
            labels: {
                formatter: function (value, timestamp, opts) {
                    var y = +value - 1;
                    return "" + y;
                }
            },
            title: {
                text: "Lap",
            }
        };
        this.lapXaxis1 = {
            title: {
                text: "Lap",
            },
        };
        this.numberXaxis = {
            type: "numeric"
        };
        this.barPlotOptions = {
            bar: {
                horizontal: true,
                barHeight: "50%",
                rangeBarGroupRows: true
            }
        };
        this.observableList = restService.getRaceAnalysis();
        this.observableList.subscribe({
            next: data => {
                this.status = data.status;
                this.drivers = data.driverData;
                this.lapTimesLowerLimit = this.getLowerLimit();
                this.driversTab1 = this.sortDrivers(this.drivers, "position", "asc");
                this.maxLaps = this.driversTab1[0].lapByLapData.positions.length;
                this.driversTab5 = this.sortDrivers(this.drivers, "position", "asc");
                this.updateComparisonTable();
                this.title = data.title;
                this.utilityService.setTitle(this.title);
                this.year = data.year;
                this.weatherDataList = data.weatherChartData;
                this.setXAxis();
                this.setTrackTemperature();
                this.setAirTemperature();
                this.setRainingChart();
                this.setWindspeedChart();
                this.setWindDirChart();
                this.setHumidityChart();
                this.setPressureChart();
                this.setSteering();
                this.setGforceLat();
                this.setGforceLong();
                this.setBrake();
                this.setPerformance();
                this.setThrottle();
                this.fastestLaps = this.setFastestLaps(this.drivers);
                this.setDriverCharts();
                return data;
            }
        });
        this.utilityService.reloadPosts(6);
    }
    sortDrivers(drivers, number, direction) {
        let sorted = [];
        let undefineds = 1;
        if (direction == "asc") {
            switch (number) {
                case "position":
                    drivers.forEach((driver) => {
                        sorted[driver.position - 1] = driver;
                    });
                    return sorted;
                    break;
                case "startingPosition":
                    drivers.forEach((driver) => {
                        sorted[driver.startingPosition - 1] = driver;
                    });
                    return sorted;
                    break;
                case "fastestLap":
                    drivers.forEach((driver) => {
                        if (driver.fastestLapPosition != null) {
                            sorted[driver.fastestLapPosition - 1] = driver;
                        }
                        else {
                            sorted[drivers.length - undefineds++] = driver;
                        }
                    });
                    return sorted;
                    break;
            }
        }
        if (direction == "desc") {
            undefineds = 0;
            switch (number) {
                case "position":
                    drivers.forEach((driver) => {
                        sorted[drivers.length - driver.position] = driver;
                    });
                    return sorted;
                    break;
                case "startingPosition":
                    drivers.forEach((driver) => {
                        sorted[drivers.length - driver.startingPosition] = driver;
                    });
                    return sorted;
                    break;
                case "fastestLap":
                    drivers.forEach((driver) => {
                        if (driver.fastestLapPosition != null) {
                            sorted[drivers.length - driver.fastestLapPosition] = driver;
                        }
                        else {
                            sorted[undefineds++] = driver;
                        }
                    });
                    return sorted;
                    break;
            }
        }
        return this.driversTab1;
    }
    setXAxis() {
        this.defXaxis = {
            categories: this.weatherDataList[0].xAxis,
            tickAmount: 6,
            title: {
                text: 'minutes'
            },
        };
        this.driverCodesXaxis = {
            categories: this.driverCodes,
            tickAmount: 6,
            title: {
                text: 'Score'
            },
        };
    }
    setDriverCharts() {
        let positionsByLapchartSeries = [];
        let timesByLapchartSeries = [];
        let tyresByLapchartSeries = [];
        for (let driver of this.drivers) {
            let color = driver.Color;
            if (color == "FFFFFF") {
                color = "888888";
            }
            positionsByLapchartSeries.push({
                name: driver.Initials,
                data: driver.lapByLapData.positions,
                color: "#" + color
            });
            let filteredLapsNoSlowLaps = this.filterOutSlowLaps(driver.lapByLapData.lapTimesYms);
            timesByLapchartSeries.push({
                name: driver.Initials,
                data: filteredLapsNoSlowLaps,
                color: "#" + color
            });
            this.steering.push(driver.steering);
            this.gforceLat.push(driver.gforceLat);
            this.gforceLong.push(driver.gforceLong);
            this.brake.push(driver.brake);
            this.performance.push(driver.performance);
            this.throttle.push(driver.throttle);
            this.driverCodes.push(driver.Initials);
        }
        for (let tab of this.driversTab1) {
            this.driverCodesSortedByFinalPosition.push(tab.Initials);
            let lowerLimit = 0;
            for (let tyre of tab.lapByLapData.tyres) {
                tyresByLapchartSeries.push({
                    name: tyre.duration + ' LAPS ON ('
                        + tyre.type + ')',
                    data: [
                        {
                            x: tab.Initials,
                            y: [
                                lowerLimit,
                                lowerLimit + tyre.duration
                            ]
                        }
                    ],
                    color: RaceAnalysisComponent.getColor(tyre.type)
                });
                lowerLimit = lowerLimit + tyre.duration;
            }
        }
        this.lapPosChart = {
            series: positionsByLapchartSeries,
            title: {
                text: 'Position by lap',
                align: 'center'
            },
        };
        this.lapTimesChart = {
            series: timesByLapchartSeries,
            title: {
                text: 'Lap times',
                align: 'center'
            },
            chart: {
                height: 1000,
                type: 'line',
                animations: {
                    enabled: false
                },
                zoom: {
                    enabled: false,
                    type: "x",
                    autoScaleYaxis: true
                },
                toolbar: ChartUtility.defaultToolbar
            },
            yAxis: {
                tickAmount: 10,
                min: this.lapTimesLowerLimit,
                max: this.lapTimesUpperLimit,
                decimalsInFloat: 0,
                reversed: true,
                labels: {
                    show: true,
                    formatter: (val, opts) => {
                        var date = new Date(val);
                        var h = date.getHours();
                        var m = date.getMinutes();
                        var s = date.getSeconds();
                        return m + "m " + s + "s ";
                    }
                },
                axisTicks: {
                    show: true
                },
                axisBorder: {
                    show: true,
                    color: "#008FFB"
                },
                title: {
                    text: "LapTimes",
                }
            },
            tooltip: {
                y: {
                    formatter: (val, opts) => this.drivers[opts.seriesIndex].lapByLapData.lapTimesY[opts.dataPointIndex],
                    title: {
                        formatter: (seriesName) => seriesName,
                    },
                }
            }
        };
        this.lapTyreChart = {
            series: tyresByLapchartSeries,
            title: {
                text: 'Tyres by lap',
                align: 'center'
            },
            chart: {
                height: 500,
                type: "rangeBar",
                zoom: {
                    enabled: false
                },
                toolbar: ChartUtility.defaultToolbar
            },
            plotOptions: {
                bar: {
                    horizontal: true,
                    borderRadius: 10,
                    barHeight: "90%",
                    rangeBarGroupRows: true
                }
            },
            fill: {
                type: "solid"
            },
            xaxis: {
                type: "datetime",
                title: {
                    text: "Lap",
                }
            },
            legend: {
                show: false,
                position: "right"
            },
            tooltip: {
                custom: function (opts) {
                    const fromYear = opts.y1;
                    const toYear = opts.y2;
                    const values = opts.ctx.rangeBar.getTooltipValues(opts);
                    return ('<div class="apexcharts-tooltip-rangebar">' +
                        '<div> <span class="series-name" style="color: ' +
                        values.color +
                        '">' + values.seriesName +
                        "</span></div>" +
                        '<div> <span class="category">' +
                        ' </span> <span class="value start-value">' +
                        fromYear +
                        '</span> <span class="separator">-</span> <span class="value end-value">' +
                        toYear +
                        "</span></div>" +
                        "</div>");
                }
            }
        };
    }
    setSteering() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Steering score',
                data: this.steering
            }];
        this.steeringChart = {
            series: chartSeries,
            title: {
                text: 'Steering',
                align: 'center'
            },
        };
    }
    setGforceLat() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Cornering score',
                data: this.gforceLat
            }];
        this.gforceLatChart = {
            series: chartSeries,
            title: {
                text: 'Cornering (lateral G-Force)',
                align: 'center'
            },
        };
    }
    setGforceLong() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Aggression score',
                data: this.gforceLong
            }];
        this.gforceLongChart = {
            series: chartSeries,
            title: {
                text: 'Aggression (longitudinal G-Force)',
                align: 'center'
            },
        };
    }
    setBrake() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Brake score',
                data: this.brake
            }];
        this.brakeChart = {
            series: chartSeries,
            title: {
                text: 'Brake',
                align: 'center'
            },
        };
    }
    setPerformance() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Performance score',
                data: this.performance
            }];
        this.performanceChart = {
            series: chartSeries,
            title: {
                text: 'Performance',
                align: 'center'
            },
        };
    }
    setThrottle() {
        let chartSeries = [];
        chartSeries = [{
                name: 'Throttle score',
                data: this.throttle
            }];
        this.throttleChart = {
            series: chartSeries,
            title: {
                text: 'Throttle',
                align: 'center'
            },
        };
    }
    setAirTemperature() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yAir,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yAir,
                color: 'grey'
            });
        }
        this.airTemperatureChart = {
            series: chartSeries,
            title: {
                text: 'Air temperature (°C)',
                align: 'center'
            },
        };
    }
    setTrackTemperature() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yTrack,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yTrack,
                color: 'grey'
            });
        }
        this.trackTemperatureChart = {
            series: chartSeries,
            title: {
                text: 'Track Temperature (°C)',
                align: 'center'
            },
        };
    }
    setRainingChart() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yRaining,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yRaining,
                color: 'grey'
            });
        }
        this.rainingChart = {
            series: chartSeries,
            title: {
                text: 'Rainfall (mm)',
                align: 'center'
            },
        };
    }
    setWindspeedChart() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yWindSpeed,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yWindSpeed,
                color: 'grey'
            });
        }
        this.windspeedChart = {
            series: chartSeries,
            title: {
                text: 'Wind Speed',
                align: 'center'
            },
        };
    }
    setWindDirChart() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yWindDir,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yWindDir,
                color: 'grey'
            });
        }
        this.windDirChart = {
            series: chartSeries,
            title: {
                text: 'Wind direction',
                align: 'center'
            },
        };
    }
    setHumidityChart() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yHumidity,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yHumidity,
                color: 'grey'
            });
        }
        this.humidityChart = {
            series: chartSeries,
            title: {
                text: 'Humidity (%)',
                align: 'center'
            },
        };
    }
    setPressureChart() {
        let chartSeries = [];
        chartSeries.push({
            name: '' + this.weatherDataList[0].name,
            data: this.weatherDataList[0].yPressure,
        });
        for (let x = 1; x < this.weatherDataList.length; x++) {
            chartSeries.push({
                name: '' + this.weatherDataList[x].name,
                data: this.weatherDataList[x].yPressure,
                color: 'grey'
            });
        }
        this.pressureChart = {
            series: chartSeries,
            title: {
                text: 'Air pressure (hPa)',
                align: 'center'
            },
        };
    }
    static getColor(type) {
        switch (type) {
            case 'H':
                // return "#f0f0f0";
                return "#b0b0b0";
            case 'M':
                return "#ffd100";
            case 'S':
                return "#da291c";
            case 'I':
                return "#43b02a";
            case 'W':
                return "#0068ad";
            default:
                return '#000000';
        }
    }
    radioChange(event) {
        this.driversTab5 = this.sortDrivers(this.drivers, event.value, 'asc');
    }
    sortResultData(sort) {
        this.driversTab1 = this.sortDrivers(this.drivers, sort.active, sort.direction);
    }
    getLowerLimit() {
        let fastestLap = this.sortDrivers(this.drivers, 'fastestLap', 'asc')[0].fastestLap;
        let milisecondsString = "" + this.stringToMiliseconds(fastestLap);
        milisecondsString = milisecondsString.substring(0, milisecondsString.length - 3) + '000';
        return Number(milisecondsString);
    }
    stringToMiliseconds(stringTime) {
        let array1 = stringTime.split(":");
        let miliseconds = +array1[0] * 60000;
        let array2 = array1[1].split(".");
        miliseconds += +array2[0] * 1000;
        miliseconds += +array2[1];
        return miliseconds;
    }
    filterOutSlowLaps(lapTimes) {
        let output = lapTimes.slice();
        for (let n = 0; n <= output.length; n++) {
            if (output[n] - this.fastestLaps[n] > 5000) {
                output[n] = null;
            }
            else {
                if (output[n] > this.lapTimesUpperLimit) {
                    this.lapTimesUpperLimit = output[n];
                }
            }
        }
        return output;
    }
    filterOutSlowLapsOld(lapTimes) {
        let output = lapTimes.slice();
        for (let n = 0; n <= output.length; n++) {
            if (output[n - 1] - output[n] > 15000) {
                //output[n-1]=null;
            }
            else {
                if (output[n - 1] > this.lapTimesUpperLimit) {
                    this.lapTimesUpperLimit = output[n - 1];
                }
            }
        }
        return output;
    }
    setFastestLaps(drivers) {
        this.maxLaps;
        let fastestLaps = [];
        for (let i = 0; i < this.maxLaps; i++) {
            let fastestLap = 3600000;
            for (let driver of drivers) {
                if (driver.lapByLapData.lapTimesYms[i] < fastestLap) {
                    fastestLap = driver.lapByLapData.lapTimesYms[i];
                }
            }
            fastestLaps.push(fastestLap);
        }
        return fastestLaps;
    }
    updateComparisonTable() {
        this.comparisonTable.driver1Code = this.drivers[this.selectedDriver1].Initials;
        this.comparisonTable.driver1Name = this.drivers[this.selectedDriver1].FullName;
        this.comparisonTable.driver2Code = this.drivers[this.selectedDriver2].Initials;
        this.comparisonTable.driver2Name = this.drivers[this.selectedDriver2].FullName;
        this.comparisonTable.rowCount = Math.max(this.drivers[this.selectedDriver1].lapByLapData.lapTimesYms.length, this.drivers[this.selectedDriver2].lapByLapData.lapTimesYms.length);
        for (let n = 0; n < this.comparisonTable.rowCount; n++) {
            this.comparisonTable.rows[n] = new ComparisonTableRow(this.drivers[this.selectedDriver1], this.drivers[this.selectedDriver2], n);
        }
    }
}
RaceAnalysisComponent.ɵfac = function RaceAnalysisComponent_Factory(t) { return new (t || RaceAnalysisComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t)); };
RaceAnalysisComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: RaceAnalysisComponent, selectors: [["race-analysis-cmp"]], viewQuery: function RaceAnalysisComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(race_analysis_component_c0, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.chart = _t.first);
    } }, decls: 31, vars: 3, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], ["label", "Final result"], ["matTabContent", ""], ["label", "Lap-by-Lap"], ["label", "Scores"], ["label", "Weather"], ["label", "Drivers"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [4, "ngIf"], ["matSort", "", 1, "table", "table-striped", 3, "matSortChange"], ["mat-sort-header", "position", "scope", "col"], ["scope", "col"], ["scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["mat-sort-header", "fastestLap", "scope", "col"], ["scope", "row"], [1, "d-sm-none", "d-none", "d-lg-table-cell"], [1, "card"], [1, "card-body-padded"], [3, "series", "chart", "dataLabels", "grid", "stroke", "title", "yaxis", "xaxis"], [3, "title", "series", "chart", "fill", "legend", "plotOptions", "xaxis", "tooltip"], ["class", "row", 4, "ngIf"], [3, "series", "chart", "dataLabels", "grid", "stroke", "title", "yaxis", "xaxis", "tooltip"], [1, "card-body-padded", "table-responsive"], [1, "table", "table-striped"], ["colspan", "1", "scope", "col", 2, "width", "35px"], ["colspan", "1", "scope", "col", 1, "custom-mat-select"], [3, "value", "selectionChange", "valueChange"], [3, "value", 4, "ngFor", "ngForOf"], ["colspan", "1", "scope", "col", 1, "text-center", 2, "width", "35px"], ["colspan", "1", "scope", "col", 1, "text-right"], ["colspan", "1", "scope", "col"], [3, "value"], ["colspan", "1", "scope", "row"], ["colspan", "1", 1, "text-right"], ["colspan", "1", 1, "width-7rem", "text-right", 3, "ngClass"], ["colspan", "1"], [1, "card-body-padded", "text-center"], [1, "col-md-12", "col-lg-6"], [3, "legend", "series", "chart", "xaxis", "dataLabels", "grid", "stroke", "title", "plotOptions"], [3, "legend", "series", "chart", "xaxis", "dataLabels", "grid", "title", "plotOptions"], [3, "legend", "series", "chart", "xaxis", "dataLabels", "grid", "stroke", "title"], ["aria-label", "Select an option", 3, "ngModel", "ngModelChange"], ["checked", "", "value", "position", 1, "div-padded-right-2", 3, "change"], ["value", "startingPosition", 1, "div-padded-right-2", 3, "change"], ["value", "fastestLap", 1, "div-padded-right-2", 3, "change"], [1, "row", "col-padding-1"], ["class", "col-lg-2 col-6", 4, "ngFor", "ngForOf"], [1, "col-lg-2", "col-6"], [1, "card-body"], [1, "driver-text-smaller"], ["onerror", "this.src='assets/img/drivers/default.png'", "alt", "", 3, "src"], [1, "card-footer"], [1, "driver-text-def"]], template: function RaceAnalysisComponent_Template(rf, ctx) { if (rf & 1) {
        const _r38 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function RaceAnalysisComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function RaceAnalysisComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function RaceAnalysisComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(6); });
        core/* ɵɵtext */._uU(10, "Post");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function RaceAnalysisComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(6); });
        core/* ɵɵtext */._uU(12, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, RaceAnalysisComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function RaceAnalysisComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r38); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function RaceAnalysisComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r38); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "mat-tab-group");
        core/* ɵɵelementStart */.TgZ(21, "mat-tab", 12);
        core/* ɵɵtemplate */.YNc(22, RaceAnalysisComponent_ng_template_22_Template, 1, 1, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(23, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(24, RaceAnalysisComponent_ng_template_24_Template, 16, 19, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(25, "mat-tab", 15);
        core/* ɵɵtemplate */.YNc(26, RaceAnalysisComponent_ng_template_26_Template, 25, 53, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(27, "mat-tab", 16);
        core/* ɵɵtemplate */.YNc(28, RaceAnalysisComponent_ng_template_28_Template, 35, 56, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(29, "mat-tab", 17);
        core/* ɵɵtemplate */.YNc(30, RaceAnalysisComponent_ng_template_30_Template, 14, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, common/* NgIf */.O5, MatSort, MatSortHeader, ng_apexcharts/* ChartComponent */.x, MatSelect, fesm2015_core/* MatOption */.ey, common/* NgClass */.mk, fesm2015_radio/* MatRadioGroup */.VQ, fesm2015_radio/* MatRadioButton */.U0, MatButtonToggle], encapsulation: 2 });

// EXTERNAL MODULE: ./src/environments/environment.ts
var environment = __webpack_require__(2340);
// EXTERNAL MODULE: ./node_modules/@angular/platform-browser/__ivy_ngcc__/fesm2015/platform-browser.js
var platform_browser = __webpack_require__(9075);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/mergeMap.js
var mergeMap = __webpack_require__(9773);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/operators/tap.js
var tap = __webpack_require__(8307);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/Subscriber.js
var Subscriber = __webpack_require__(7393);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/scheduler/async.js
var scheduler_async = __webpack_require__(3637);
;// CONCATENATED MODULE: ./node_modules/rxjs/_esm2015/internal/operators/throttle.js

const defaultThrottleConfig = {
    leading: true,
    trailing: false
};
function throttle(durationSelector, config = defaultThrottleConfig) {
    return (source) => source.lift(new ThrottleOperator(durationSelector, !!config.leading, !!config.trailing));
}
class ThrottleOperator {
    constructor(durationSelector, leading, trailing) {
        this.durationSelector = durationSelector;
        this.leading = leading;
        this.trailing = trailing;
    }
    call(subscriber, source) {
        return source.subscribe(new ThrottleSubscriber(subscriber, this.durationSelector, this.leading, this.trailing));
    }
}
class ThrottleSubscriber extends (/* unused pure expression or super */ null && (SimpleOuterSubscriber)) {
    constructor(destination, durationSelector, _leading, _trailing) {
        super(destination);
        this.destination = destination;
        this.durationSelector = durationSelector;
        this._leading = _leading;
        this._trailing = _trailing;
        this._hasValue = false;
    }
    _next(value) {
        this._hasValue = true;
        this._sendValue = value;
        if (!this._throttled) {
            if (this._leading) {
                this.send();
            }
            else {
                this.throttle(value);
            }
        }
    }
    send() {
        const { _hasValue, _sendValue } = this;
        if (_hasValue) {
            this.destination.next(_sendValue);
            this.throttle(_sendValue);
        }
        this._hasValue = false;
        this._sendValue = undefined;
    }
    throttle(value) {
        const duration = this.tryDurationSelector(value);
        if (!!duration) {
            this.add(this._throttled = innerSubscribe(duration, new SimpleInnerSubscriber(this)));
        }
    }
    tryDurationSelector(value) {
        try {
            return this.durationSelector(value);
        }
        catch (err) {
            this.destination.error(err);
            return null;
        }
    }
    throttlingDone() {
        const { _throttled, _trailing } = this;
        if (_throttled) {
            _throttled.unsubscribe();
        }
        this._throttled = undefined;
        if (_trailing) {
            this.send();
        }
    }
    notifyNext() {
        this.throttlingDone();
    }
    notifyComplete() {
        this.throttlingDone();
    }
}
//# sourceMappingURL=throttle.js.map
;// CONCATENATED MODULE: ./node_modules/rxjs/_esm2015/internal/operators/throttleTime.js



function throttleTime(duration, scheduler = scheduler_async/* async */.P, config = defaultThrottleConfig) {
    return (source) => source.lift(new ThrottleTimeOperator(duration, scheduler, config.leading, config.trailing));
}
class ThrottleTimeOperator {
    constructor(duration, scheduler, leading, trailing) {
        this.duration = duration;
        this.scheduler = scheduler;
        this.leading = leading;
        this.trailing = trailing;
    }
    call(subscriber, source) {
        return source.subscribe(new ThrottleTimeSubscriber(subscriber, this.duration, this.scheduler, this.leading, this.trailing));
    }
}
class ThrottleTimeSubscriber extends Subscriber/* Subscriber */.L {
    constructor(destination, duration, scheduler, leading, trailing) {
        super(destination);
        this.duration = duration;
        this.scheduler = scheduler;
        this.leading = leading;
        this.trailing = trailing;
        this._hasTrailingValue = false;
        this._trailingValue = null;
    }
    _next(value) {
        if (this.throttled) {
            if (this.trailing) {
                this._trailingValue = value;
                this._hasTrailingValue = true;
            }
        }
        else {
            this.add(this.throttled = this.scheduler.schedule(dispatchNext, this.duration, { subscriber: this }));
            if (this.leading) {
                this.destination.next(value);
            }
            else if (this.trailing) {
                this._trailingValue = value;
                this._hasTrailingValue = true;
            }
        }
    }
    _complete() {
        if (this._hasTrailingValue) {
            this.destination.next(this._trailingValue);
            this.destination.complete();
        }
        else {
            this.destination.complete();
        }
    }
    clearThrottle() {
        const throttled = this.throttled;
        if (throttled) {
            if (this.trailing && this._hasTrailingValue) {
                this.destination.next(this._trailingValue);
                this._trailingValue = null;
                this._hasTrailingValue = false;
            }
            throttled.unsubscribe();
            this.remove(throttled);
            this.throttled = null;
        }
    }
}
function dispatchNext(arg) {
    const { subscriber } = arg;
    subscriber.clearThrottle();
}
//# sourceMappingURL=throttleTime.js.map
;// CONCATENATED MODULE: ./node_modules/ngx-infinite-scroll/__ivy_ngcc__/modules/ngx-infinite-scroll.js




/**
 * @fileoverview added by tsickle
 * Generated from: src/services/ngx-ins-utils.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @param {?} selector
 * @param {?} scrollWindow
 * @param {?} defaultElement
 * @param {?} fromRoot
 * @return {?}
 */

function resolveContainerElement(selector, scrollWindow, defaultElement, fromRoot) {
    /** @type {?} */
    const hasWindow = window && !!window.document && window.document.documentElement;
    /** @type {?} */
    let container = hasWindow && scrollWindow ? window : defaultElement;
    if (selector) {
        /** @type {?} */
        const containerIsString = selector && hasWindow && typeof selector === 'string';
        container = containerIsString
            ? findElement(selector, defaultElement.nativeElement, fromRoot)
            : selector;
        if (!container) {
            throw new Error('ngx-infinite-scroll {resolveContainerElement()}: selector for');
        }
    }
    return container;
}
/**
 * @param {?} selector
 * @param {?} customRoot
 * @param {?} fromRoot
 * @return {?}
 */
function findElement(selector, customRoot, fromRoot) {
    /** @type {?} */
    const rootEl = fromRoot ? window.document : customRoot;
    return rootEl.querySelector(selector);
}
/**
 * @param {?} prop
 * @return {?}
 */
function inputPropChanged(prop) {
    return prop && !prop.firstChange;
}
/**
 * @return {?}
 */
function hasWindowDefined() {
    return typeof window !== 'undefined';
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/axis-resolver.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/** @type {?} */
const VerticalProps = {
    clientHeight: "clientHeight",
    offsetHeight: "offsetHeight",
    scrollHeight: "scrollHeight",
    pageYOffset: "pageYOffset",
    offsetTop: "offsetTop",
    scrollTop: "scrollTop",
    top: "top"
};
/** @type {?} */
const HorizontalProps = {
    clientHeight: "clientWidth",
    offsetHeight: "offsetWidth",
    scrollHeight: "scrollWidth",
    pageYOffset: "pageXOffset",
    offsetTop: "offsetLeft",
    scrollTop: "scrollLeft",
    top: "left"
};
class AxisResolver {
    /**
     * @param {?=} vertical
     */
    constructor(vertical = true) {
        this.vertical = vertical;
        this.propsMap = vertical ? VerticalProps : HorizontalProps;
    }
    /**
     * @return {?}
     */
    clientHeightKey() {
        return this.propsMap.clientHeight;
    }
    /**
     * @return {?}
     */
    offsetHeightKey() {
        return this.propsMap.offsetHeight;
    }
    /**
     * @return {?}
     */
    scrollHeightKey() {
        return this.propsMap.scrollHeight;
    }
    /**
     * @return {?}
     */
    pageYOffsetKey() {
        return this.propsMap.pageYOffset;
    }
    /**
     * @return {?}
     */
    offsetTopKey() {
        return this.propsMap.offsetTop;
    }
    /**
     * @return {?}
     */
    scrollTopKey() {
        return this.propsMap.scrollTop;
    }
    /**
     * @return {?}
     */
    topKey() {
        return this.propsMap.top;
    }
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/event-trigger.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @record
 */

/**
 * @record
 */

/**
 * @record
 */

/**
 * @record
 */

/**
 * @param {?} alwaysCallback
 * @param {?} shouldFireScrollEvent
 * @param {?} isTriggeredCurrentTotal
 * @return {?}
 */
function shouldTriggerEvents(alwaysCallback, shouldFireScrollEvent, isTriggeredCurrentTotal) {
    if (alwaysCallback && shouldFireScrollEvent) {
        return true;
    }
    if (!isTriggeredCurrentTotal && shouldFireScrollEvent) {
        return true;
    }
    return false;
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/position-resolver.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @param {?} __0
 * @return {?}
 */
function createResolver({ windowElement, axis }) {
    return createResolverWithContainer({ axis, isWindow: isElementWindow(windowElement) }, windowElement);
}
/**
 * @param {?} resolver
 * @param {?} windowElement
 * @return {?}
 */
function createResolverWithContainer(resolver, windowElement) {
    /** @type {?} */
    const container = resolver.isWindow || (windowElement && !windowElement.nativeElement)
        ? windowElement
        : windowElement.nativeElement;
    return Object.assign(Object.assign({}, resolver), { container });
}
/**
 * @param {?} windowElement
 * @return {?}
 */
function isElementWindow(windowElement) {
    /** @type {?} */
    const isWindow = ['Window', 'global'].some((/**
     * @param {?} obj
     * @return {?}
     */
    (obj) => Object.prototype.toString.call(windowElement).includes(obj)));
    return isWindow;
}
/**
 * @param {?} isContainerWindow
 * @param {?} windowElement
 * @return {?}
 */
function getDocumentElement(isContainerWindow, windowElement) {
    return isContainerWindow ? windowElement.document.documentElement : null;
}
/**
 * @param {?} element
 * @param {?} resolver
 * @return {?}
 */
function calculatePoints(element, resolver) {
    /** @type {?} */
    const height = extractHeightForElement(resolver);
    return resolver.isWindow
        ? calculatePointsForWindow(height, element, resolver)
        : calculatePointsForElement(height, element, resolver);
}
/**
 * @param {?} height
 * @param {?} element
 * @param {?} resolver
 * @return {?}
 */
function calculatePointsForWindow(height, element, resolver) {
    const { axis, container, isWindow } = resolver;
    const { offsetHeightKey, clientHeightKey } = extractHeightPropKeys(axis);
    // scrolled until now / current y point
    /** @type {?} */
    const scrolled = height +
        getElementPageYOffset(getDocumentElement(isWindow, container), axis, isWindow);
    // total height / most bottom y point
    /** @type {?} */
    const nativeElementHeight = getElementHeight(element.nativeElement, isWindow, offsetHeightKey, clientHeightKey);
    /** @type {?} */
    const totalToScroll = getElementOffsetTop(element.nativeElement, axis, isWindow) +
        nativeElementHeight;
    return { height, scrolled, totalToScroll, isWindow };
}
/**
 * @param {?} height
 * @param {?} element
 * @param {?} resolver
 * @return {?}
 */
function calculatePointsForElement(height, element, resolver) {
    const { axis, container } = resolver;
    // perhaps use container.offsetTop instead of 'scrollTop'
    /** @type {?} */
    const scrolled = container[axis.scrollTopKey()];
    /** @type {?} */
    const totalToScroll = container[axis.scrollHeightKey()];
    return { height, scrolled, totalToScroll, isWindow: false };
}
/**
 * @param {?} axis
 * @return {?}
 */
function extractHeightPropKeys(axis) {
    return {
        offsetHeightKey: axis.offsetHeightKey(),
        clientHeightKey: axis.clientHeightKey()
    };
}
/**
 * @param {?} __0
 * @return {?}
 */
function extractHeightForElement({ container, isWindow, axis }) {
    const { offsetHeightKey, clientHeightKey } = extractHeightPropKeys(axis);
    return getElementHeight(container, isWindow, offsetHeightKey, clientHeightKey);
}
/**
 * @param {?} elem
 * @param {?} isWindow
 * @param {?} offsetHeightKey
 * @param {?} clientHeightKey
 * @return {?}
 */
function getElementHeight(elem, isWindow, offsetHeightKey, clientHeightKey) {
    if (isNaN(elem[offsetHeightKey])) {
        /** @type {?} */
        const docElem = getDocumentElement(isWindow, elem);
        return docElem ? docElem[clientHeightKey] : 0;
    }
    else {
        return elem[offsetHeightKey];
    }
}
/**
 * @param {?} elem
 * @param {?} axis
 * @param {?} isWindow
 * @return {?}
 */
function getElementOffsetTop(elem, axis, isWindow) {
    /** @type {?} */
    const topKey = axis.topKey();
    // elem = elem.nativeElement;
    if (!elem.getBoundingClientRect) {
        // || elem.css('none')) {
        return;
    }
    return (elem.getBoundingClientRect()[topKey] +
        getElementPageYOffset(elem, axis, isWindow));
}
/**
 * @param {?} elem
 * @param {?} axis
 * @param {?} isWindow
 * @return {?}
 */
function getElementPageYOffset(elem, axis, isWindow) {
    /** @type {?} */
    const pageYOffset = axis.pageYOffsetKey();
    /** @type {?} */
    const scrollTop = axis.scrollTopKey();
    /** @type {?} */
    const offsetTop = axis.offsetTopKey();
    if (isNaN(window.pageYOffset)) {
        return getDocumentElement(isWindow, elem)[scrollTop];
    }
    else if (elem.ownerDocument) {
        return elem.ownerDocument.defaultView[pageYOffset];
    }
    else {
        return elem[offsetTop];
    }
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/scroll-resolver.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @param {?} container
 * @param {?} distance
 * @param {?} scrollingDown
 * @return {?}
 */
function shouldFireScrollEvent(container, distance, scrollingDown) {
    /** @type {?} */
    let remaining;
    /** @type {?} */
    let containerBreakpoint;
    if (container.totalToScroll <= 0) {
        return false;
    }
    /** @type {?} */
    const scrolledUntilNow = container.isWindow ? container.scrolled : container.height + container.scrolled;
    if (scrollingDown) {
        remaining =
            (container.totalToScroll - scrolledUntilNow) / container.totalToScroll;
        containerBreakpoint = distance.down / 10;
    }
    else {
        /** @type {?} */
        const totalHiddenContentHeight = container.scrolled + (container.totalToScroll - scrolledUntilNow);
        remaining = container.scrolled / totalHiddenContentHeight;
        containerBreakpoint = distance.up / 10;
    }
    /** @type {?} */
    const shouldFireEvent = remaining <= containerBreakpoint;
    return shouldFireEvent;
}
/**
 * @param {?} lastScrollPosition
 * @param {?} container
 * @return {?}
 */
function isScrollingDownwards(lastScrollPosition, container) {
    return lastScrollPosition < container.scrolled;
}
/**
 * @param {?} lastScrollPosition
 * @param {?} container
 * @param {?} distance
 * @return {?}
 */
function getScrollStats(lastScrollPosition, container, distance) {
    /** @type {?} */
    const scrollDown = isScrollingDownwards(lastScrollPosition, container);
    return {
        fire: shouldFireScrollEvent(container, distance, scrollDown),
        scrollDown
    };
}
/**
 * @param {?} position
 * @param {?} scrollState
 * @return {?}
 */

/**
 * @param {?} totalToScroll
 * @param {?} scrollState
 * @return {?}
 */

/**
 * @param {?} scrollState
 * @return {?}
 */

/**
 * @param {?} scroll
 * @param {?} scrollState
 * @param {?} triggered
 * @param {?} isScrollingDown
 * @return {?}
 */

/**
 * @param {?} totalToScroll
 * @param {?} scrollState
 * @param {?} isScrollingDown
 * @return {?}
 */

/**
 * @param {?} scrollState
 * @param {?} scrolledUntilNow
 * @param {?} totalToScroll
 * @return {?}
 */

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/scroll-state.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class ScrollState {
    /**
     * @param {?} __0
     */
    constructor({ totalToScroll }) {
        this.lastScrollPosition = 0;
        this.lastTotalToScroll = 0;
        this.totalToScroll = 0;
        this.triggered = {
            down: 0,
            up: 0
        };
        this.totalToScroll = totalToScroll;
    }
    /**
     * @param {?} position
     * @return {?}
     */
    updateScrollPosition(position) {
        return (this.lastScrollPosition = position);
    }
    /**
     * @param {?} totalToScroll
     * @return {?}
     */
    updateTotalToScroll(totalToScroll) {
        if (this.lastTotalToScroll !== totalToScroll) {
            this.lastTotalToScroll = this.totalToScroll;
            this.totalToScroll = totalToScroll;
        }
    }
    /**
     * @param {?} scrolledUntilNow
     * @param {?} totalToScroll
     * @return {?}
     */
    updateScroll(scrolledUntilNow, totalToScroll) {
        this.updateScrollPosition(scrolledUntilNow);
        this.updateTotalToScroll(totalToScroll);
    }
    /**
     * @param {?} scroll
     * @param {?} isScrollingDown
     * @return {?}
     */
    updateTriggeredFlag(scroll, isScrollingDown) {
        if (isScrollingDown) {
            this.triggered.down = scroll;
        }
        else {
            this.triggered.up = scroll;
        }
    }
    /**
     * @param {?} totalToScroll
     * @param {?} isScrollingDown
     * @return {?}
     */
    isTriggeredScroll(totalToScroll, isScrollingDown) {
        return isScrollingDown
            ? this.triggered.down === totalToScroll
            : this.triggered.up === totalToScroll;
    }
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/services/scroll-register.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @param {?} config
 * @return {?}
 */
function createScroller(config) {
    const { scrollContainer, scrollWindow, element, fromRoot } = config;
    /** @type {?} */
    const resolver = createResolver({
        axis: new AxisResolver(!config.horizontal),
        windowElement: resolveContainerElement(scrollContainer, scrollWindow, element, fromRoot)
    });
    /** @type {?} */
    const scrollState = new ScrollState({
        totalToScroll: calculatePoints(element, resolver)
    });
    /** @type {?} */
    const options = {
        container: resolver.container,
        throttle: config.throttle
    };
    /** @type {?} */
    const distance = {
        up: config.upDistance,
        down: config.downDistance
    };
    return attachScrollEvent(options).pipe((0,mergeMap/* mergeMap */.zg)((/**
     * @return {?}
     */
    () => (0,of.of)(calculatePoints(element, resolver)))), (0,map/* map */.U)((/**
     * @param {?} positionStats
     * @return {?}
     */
    (positionStats) => toInfiniteScrollParams(scrollState.lastScrollPosition, positionStats, distance))), (0,tap/* tap */.b)((/**
     * @param {?} __0
     * @return {?}
     */
    ({ stats }) => scrollState.updateScroll(stats.scrolled, stats.totalToScroll))), (0,filter/* filter */.h)((/**
     * @param {?} __0
     * @return {?}
     */
    ({ fire, scrollDown, stats: { totalToScroll } }) => shouldTriggerEvents(config.alwaysCallback, fire, scrollState.isTriggeredScroll(totalToScroll, scrollDown)))), (0,tap/* tap */.b)((/**
     * @param {?} __0
     * @return {?}
     */
    ({ scrollDown, stats: { totalToScroll } }) => {
        scrollState.updateTriggeredFlag(totalToScroll, scrollDown);
    })), (0,map/* map */.U)(toInfiniteScrollAction));
}
/**
 * @param {?} options
 * @return {?}
 */
function attachScrollEvent(options) {
    /** @type {?} */
    let obs = (0,fromEvent/* fromEvent */.R)(options.container, 'scroll');
    // For an unknown reason calling `sampleTime()` causes trouble for many users, even with `options.throttle = 0`.
    // Let's avoid calling the function unless needed.
    // Replacing with throttleTime seems to solve the problem
    // See https://github.com/orizens/ngx-infinite-scroll/issues/198
    if (options.throttle) {
        obs = obs.pipe(throttleTime(options.throttle));
    }
    return obs;
}
/**
 * @param {?} lastScrollPosition
 * @param {?} stats
 * @param {?} distance
 * @return {?}
 */
function toInfiniteScrollParams(lastScrollPosition, stats, distance) {
    const { scrollDown, fire } = getScrollStats(lastScrollPosition, stats, distance);
    return {
        scrollDown,
        fire,
        stats
    };
}
/** @type {?} */
const InfiniteScrollActions = {
    DOWN: '[NGX_ISE] DOWN',
    UP: '[NGX_ISE] UP'
};
/**
 * @param {?} response
 * @return {?}
 */
function toInfiniteScrollAction(response) {
    const { scrollDown, stats: { scrolled: currentScrollPosition } } = response;
    return {
        type: scrollDown ? InfiniteScrollActions.DOWN : InfiniteScrollActions.UP,
        payload: {
            currentScrollPosition
        }
    };
}

/**
 * @fileoverview added by tsickle
 * Generated from: src/modules/infinite-scroll.directive.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class InfiniteScrollDirective {
    /**
     * @param {?} element
     * @param {?} zone
     */
    constructor(element, zone) {
        this.element = element;
        this.zone = zone;
        this.scrolled = new core/* EventEmitter */.vpe();
        this.scrolledUp = new core/* EventEmitter */.vpe();
        this.infiniteScrollDistance = 2;
        this.infiniteScrollUpDistance = 1.5;
        this.infiniteScrollThrottle = 150;
        this.infiniteScrollDisabled = false;
        this.infiniteScrollContainer = null;
        this.scrollWindow = true;
        this.immediateCheck = false;
        this.horizontal = false;
        this.alwaysCallback = false;
        this.fromRoot = false;
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        if (!this.infiniteScrollDisabled) {
            this.setup();
        }
    }
    /**
     * @param {?} __0
     * @return {?}
     */
    ngOnChanges({ infiniteScrollContainer, infiniteScrollDisabled, infiniteScrollDistance }) {
        /** @type {?} */
        const containerChanged = inputPropChanged(infiniteScrollContainer);
        /** @type {?} */
        const disabledChanged = inputPropChanged(infiniteScrollDisabled);
        /** @type {?} */
        const distanceChanged = inputPropChanged(infiniteScrollDistance);
        /** @type {?} */
        const shouldSetup = (!disabledChanged && !this.infiniteScrollDisabled) ||
            (disabledChanged && !infiniteScrollDisabled.currentValue) || distanceChanged;
        if (containerChanged || disabledChanged || distanceChanged) {
            this.destroyScroller();
            if (shouldSetup) {
                this.setup();
            }
        }
    }
    /**
     * @return {?}
     */
    setup() {
        if (hasWindowDefined()) {
            this.zone.runOutsideAngular((/**
             * @return {?}
             */
            () => {
                this.disposeScroller = createScroller({
                    fromRoot: this.fromRoot,
                    alwaysCallback: this.alwaysCallback,
                    disable: this.infiniteScrollDisabled,
                    downDistance: this.infiniteScrollDistance,
                    element: this.element,
                    horizontal: this.horizontal,
                    scrollContainer: this.infiniteScrollContainer,
                    scrollWindow: this.scrollWindow,
                    throttle: this.infiniteScrollThrottle,
                    upDistance: this.infiniteScrollUpDistance
                }).subscribe((/**
                 * @param {?} payload
                 * @return {?}
                 */
                (payload) => this.zone.run((/**
                 * @return {?}
                 */
                () => this.handleOnScroll(payload)))));
            }));
        }
    }
    /**
     * @param {?} __0
     * @return {?}
     */
    handleOnScroll({ type, payload }) {
        switch (type) {
            case InfiniteScrollActions.DOWN:
                return this.scrolled.emit(payload);
            case InfiniteScrollActions.UP:
                return this.scrolledUp.emit(payload);
            default:
                return;
        }
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.destroyScroller();
    }
    /**
     * @return {?}
     */
    destroyScroller() {
        if (this.disposeScroller) {
            this.disposeScroller.unsubscribe();
        }
    }
}
InfiniteScrollDirective.ɵfac = function InfiniteScrollDirective_Factory(t) { return new (t || InfiniteScrollDirective)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b)); };
InfiniteScrollDirective.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: InfiniteScrollDirective, selectors: [["", "infiniteScroll", ""], ["", "infinite-scroll", ""], ["", "data-infinite-scroll", ""]], inputs: { infiniteScrollDistance: "infiniteScrollDistance", infiniteScrollUpDistance: "infiniteScrollUpDistance", infiniteScrollThrottle: "infiniteScrollThrottle", infiniteScrollDisabled: "infiniteScrollDisabled", infiniteScrollContainer: "infiniteScrollContainer", scrollWindow: "scrollWindow", immediateCheck: "immediateCheck", horizontal: "horizontal", alwaysCallback: "alwaysCallback", fromRoot: "fromRoot" }, outputs: { scrolled: "scrolled", scrolledUp: "scrolledUp" }, features: [core/* ɵɵNgOnChangesFeature */.TTD] });
/** @nocollapse */
InfiniteScrollDirective.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: core/* NgZone */.R0b }
];
InfiniteScrollDirective.propDecorators = {
    scrolled: [{ type: core/* Output */.r_U }],
    scrolledUp: [{ type: core/* Output */.r_U }],
    infiniteScrollDistance: [{ type: core/* Input */.IIB }],
    infiniteScrollUpDistance: [{ type: core/* Input */.IIB }],
    infiniteScrollThrottle: [{ type: core/* Input */.IIB }],
    infiniteScrollDisabled: [{ type: core/* Input */.IIB }],
    infiniteScrollContainer: [{ type: core/* Input */.IIB }],
    scrollWindow: [{ type: core/* Input */.IIB }],
    immediateCheck: [{ type: core/* Input */.IIB }],
    horizontal: [{ type: core/* Input */.IIB }],
    alwaysCallback: [{ type: core/* Input */.IIB }],
    fromRoot: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(InfiniteScrollDirective, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[infiniteScroll], [infinite-scroll], [data-infinite-scroll]'
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: core/* NgZone */.R0b }]; }, { scrolled: [{
            type: core/* Output */.r_U
        }], scrolledUp: [{
            type: core/* Output */.r_U
        }], infiniteScrollDistance: [{
            type: core/* Input */.IIB
        }], infiniteScrollUpDistance: [{
            type: core/* Input */.IIB
        }], infiniteScrollThrottle: [{
            type: core/* Input */.IIB
        }], infiniteScrollDisabled: [{
            type: core/* Input */.IIB
        }], infiniteScrollContainer: [{
            type: core/* Input */.IIB
        }], scrollWindow: [{
            type: core/* Input */.IIB
        }], immediateCheck: [{
            type: core/* Input */.IIB
        }], horizontal: [{
            type: core/* Input */.IIB
        }], alwaysCallback: [{
            type: core/* Input */.IIB
        }], fromRoot: [{
            type: core/* Input */.IIB
        }] }); })();

/**
 * @fileoverview added by tsickle
 * Generated from: src/modules/ngx-infinite-scroll.module.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
class InfiniteScrollModule {
}
InfiniteScrollModule.ɵfac = function InfiniteScrollModule_Factory(t) { return new (t || InfiniteScrollModule)(); };
InfiniteScrollModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: InfiniteScrollModule });
InfiniteScrollModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [], imports: [[]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(InfiniteScrollModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                declarations: [InfiniteScrollDirective],
                exports: [InfiniteScrollDirective],
                imports: [],
                providers: []
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(InfiniteScrollModule, { declarations: [InfiniteScrollDirective], exports: [InfiniteScrollDirective] }); })();

/**
 * @fileoverview added by tsickle
 * Generated from: src/ngx-infinite-scroll.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * Generated from: public-api.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * Angular library starter.
 * Build an Angular library compatible with AoT compilation & Tree shaking.
 * Written by Roberto Simonetti.
 * MIT license.
 * https://github.com/robisim74/angular-library-starter
 */
/**
 * Entry point for all public APIs of the package.
 */

/**
 * @fileoverview added by tsickle
 * Generated from: ngx-infinite-scroll.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=ngx-infinite-scroll.js.map
;// CONCATENATED MODULE: ./src/app/pages/instagram-feed/instagram-feed.component.ts















function InstagramFeedComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 19);
    core/* ɵɵelementStart */.TgZ(2, "div", 20);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 21);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 22);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r8 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r8.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r8.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r8.comment);
} }
function InstagramFeedComponent_ng_template_22_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 23);
} }
function InstagramFeedComponent_ng_template_23_div_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelementStart */.TgZ(1, "div", 28);
    core/* ɵɵelementStart */.TgZ(2, "div", 4);
    core/* ɵɵelementStart */.TgZ(3, "div", 29);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 30);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "div", 29);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "a", 31);
    core/* ɵɵelement */._UZ(10, "img", 32);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "div", 33);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r12 = ctx.$implicit;
    const ctx_r9 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r12.username, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \uD83E\uDDE1", post_r12.likes, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r12.location, "\u200B ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", ctx_r9.getInstagramUrl(post_r12.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", ctx_r9.getUrl(post_r12.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r12.caption, " ");
} }
function InstagramFeedComponent_ng_template_23_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelementStart */.TgZ(1, "div", 28);
    core/* ɵɵelementStart */.TgZ(2, "div", 4);
    core/* ɵɵelementStart */.TgZ(3, "div", 29);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 30);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "div", 29);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "a", 31);
    core/* ɵɵelement */._UZ(10, "img", 32);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "div", 33);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r13 = ctx.$implicit;
    const ctx_r10 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r13.username, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \uD83E\uDDE1", post_r13.likes, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r13.location, "\u200B ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", ctx_r10.getInstagramUrl(post_r13.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", ctx_r10.getUrl(post_r13.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r13.caption, " ");
} }
function InstagramFeedComponent_ng_template_23_div_6_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelementStart */.TgZ(1, "div", 28);
    core/* ɵɵelementStart */.TgZ(2, "div", 4);
    core/* ɵɵelementStart */.TgZ(3, "div", 29);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 30);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "div", 29);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "a", 31);
    core/* ɵɵelement */._UZ(10, "img", 32);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "div", 33);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r14 = ctx.$implicit;
    const ctx_r11 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r14.username, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \uD83E\uDDE1", post_r14.likes, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r14.location, "\u200B ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", ctx_r11.getInstagramUrl(post_r14.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", ctx_r11.getUrl(post_r14.code), core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r14.caption, " ");
} }
function InstagramFeedComponent_ng_template_23_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 4);
    core/* ɵɵelementStart */.TgZ(1, "div", 24);
    core/* ɵɵtemplate */.YNc(2, InstagramFeedComponent_ng_template_23_div_2_Template, 13, 6, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 26);
    core/* ɵɵtemplate */.YNc(4, InstagramFeedComponent_ng_template_23_div_4_Template, 13, 6, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 26);
    core/* ɵɵtemplate */.YNc(6, InstagramFeedComponent_ng_template_23_div_6_Template, 13, 6, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r3.instaPosts.first);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r3.instaPosts.second);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r3.instaPosts.third);
} }
function InstagramFeedComponent_ng_template_25_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 34);
} }
function InstagramFeedComponent_ng_template_26_div_2_div_7_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 42);
    core/* ɵɵelementStart */.TgZ(1, "a", 31);
    core/* ɵɵelement */._UZ(2, "img", 43);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 33);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r17 = core/* ɵɵnextContext */.oxw().$implicit;
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", post_r17.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", post_r17.mediaUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r17.text, " ");
} }
function InstagramFeedComponent_ng_template_26_div_2_div_8_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "a", 31);
    core/* ɵɵelementStart */.TgZ(2, "div", 45);
    core/* ɵɵelement */._UZ(3, "img", 43);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 46);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r17 = core/* ɵɵnextContext */.oxw().$implicit;
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", post_r17.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("src", post_r17.userPicture, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r17.text, " ");
} }
function InstagramFeedComponent_ng_template_26_div_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelementStart */.TgZ(1, "div", 37);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 38);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 39);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(7, InstagramFeedComponent_ng_template_26_div_2_div_7_Template, 5, 3, "div", 40);
    core/* ɵɵtemplate */.YNc(8, InstagramFeedComponent_ng_template_26_div_2_div_8_Template, 6, 3, "div", 41);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r17 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r17.username, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \uD83E\uDDE1", post_r17.favoriteCount, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \u21D5", post_r17.retweetCount, " ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", post_r17.mediaUrl != null);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", post_r17.mediaUrl == null);
} }
function InstagramFeedComponent_ng_template_26_div_4_div_7_a_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "a", 31);
    core/* ɵɵelement */._UZ(1, "img", 43);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r22 = core/* ɵɵnextContext */.oxw(2).$implicit;
    core/* ɵɵproperty */.Q6J("href", post_r22.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", post_r22.mediaUrl, core/* ɵɵsanitizeUrl */.LSH);
} }
function InstagramFeedComponent_ng_template_26_div_4_div_7_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 42);
    core/* ɵɵtemplate */.YNc(1, InstagramFeedComponent_ng_template_26_div_4_div_7_a_1_Template, 2, 2, "a", 47);
    core/* ɵɵelementStart */.TgZ(2, "div", 33);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r22 = core/* ɵɵnextContext */.oxw().$implicit;
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", post_r22.mediaUrl != null);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r22.text, " ");
} }
function InstagramFeedComponent_ng_template_26_div_4_div_8_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "a", 31);
    core/* ɵɵelementStart */.TgZ(2, "div", 45);
    core/* ɵɵelement */._UZ(3, "img", 43);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 46);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r22 = core/* ɵɵnextContext */.oxw().$implicit;
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", post_r22.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("src", post_r22.userPicture, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r22.text, " ");
} }
function InstagramFeedComponent_ng_template_26_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelementStart */.TgZ(1, "div", 37);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 38);
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 39);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(7, InstagramFeedComponent_ng_template_26_div_4_div_7_Template, 4, 2, "div", 40);
    core/* ɵɵtemplate */.YNc(8, InstagramFeedComponent_ng_template_26_div_4_div_8_Template, 6, 3, "div", 41);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r22 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r22.username, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \uD83E\uDDE1", post_r22.favoriteCount, " ");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" \u21D5", post_r22.retweetCount, " ");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", post_r22.mediaUrl != null);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", post_r22.mediaUrl == null);
} }
function InstagramFeedComponent_ng_template_26_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 35);
    core/* ɵɵelementStart */.TgZ(1, "div", 36);
    core/* ɵɵtemplate */.YNc(2, InstagramFeedComponent_ng_template_26_div_2_Template, 9, 5, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵtemplate */.YNc(4, InstagramFeedComponent_ng_template_26_div_4_Template, 9, 5, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r5.twitterPosts.first);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r5.twitterPosts.second);
} }
function InstagramFeedComponent_ng_template_28_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 48);
} }
function InstagramFeedComponent_ng_template_29_div_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelement */._UZ(1, "div", 37);
    core/* ɵɵelementStart */.TgZ(2, "div", 42);
    core/* ɵɵelementStart */.TgZ(3, "a", 31);
    core/* ɵɵelement */._UZ(4, "img", 43);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 49);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r31 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("href", post_r31.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", post_r31.imageUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r31.title, " ");
} }
function InstagramFeedComponent_ng_template_29_div_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 27);
    core/* ɵɵelement */._UZ(1, "div", 37);
    core/* ɵɵelementStart */.TgZ(2, "div", 42);
    core/* ɵɵelementStart */.TgZ(3, "a", 31);
    core/* ɵɵelement */._UZ(4, "img", 43);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 49);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r32 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("href", post_r32.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("src", post_r32.imageUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij(" ", post_r32.title, " ");
} }
function InstagramFeedComponent_ng_template_29_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 35);
    core/* ɵɵelementStart */.TgZ(1, "div", 36);
    core/* ɵɵtemplate */.YNc(2, InstagramFeedComponent_ng_template_29_div_2_Template, 7, 3, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵtemplate */.YNc(4, InstagramFeedComponent_ng_template_29_div_4_Template, 7, 3, "div", 25);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r7 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r7.redditPosts.first);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r7.redditPosts.second);
} }
class InstagramFeedComponent {
    constructor(restService, utilityService, sanitizer) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.sanitizer = sanitizer;
        this.instaPageNum = 0;
        this.twitterpageNum = 0;
        this.redditpageNum = 0;
        this.tabIndex = 0;
        this.utilityService.setTitle("Image feed");
        this.getFeed();
        this.utilityService.reloadPosts(7);
    }
    safe(post) {
        const url = 'https://www.instagram.com/p/' + post.code + '/embed';
        return this.sanitizer.bypassSecurityTrustResourceUrl(url);
    }
    onScrollDown() {
        if (this.tabIndex == 0) {
            this.onInstagramScrollDown();
        }
        if (this.tabIndex == 1) {
            this.onTwitterScrollDown();
        }
    }
    tabChanged(tabChangeEvent) {
        this.tabIndex = tabChangeEvent.index;
    }
    onInstagramScrollDown() {
        this.instaPageNum++;
        this.updateInstagramFeed();
    }
    onTwitterScrollDown() {
        this.twitterpageNum++;
        this.updateTwitterFeed();
    }
    onRedditScrollDown() {
        this.redditpageNum++;
        this.updateRedditFeed();
    }
    getFeed() {
        let observableEvents = this.restService.getInstagramPostsPaged(this.instaPageNum);
        observableEvents.subscribe({
            next: data => {
                this.instaPosts = data;
                return data;
            }
        });
        let observableTweets = this.restService.getTwitterPostsPaged(this.twitterpageNum);
        observableTweets.subscribe({
            next: data => {
                this.twitterPosts = data;
                return data;
            }
        });
        let observableReddits = this.restService.getRedditPostsPaged(this.redditpageNum);
        observableReddits.subscribe({
            next: data => {
                this.redditPosts = data;
                return data;
            }
        });
    }
    updateInstagramFeed() {
        let observableEvents = this.restService.getInstagramPostsPaged(this.instaPageNum);
        observableEvents.subscribe({
            next: data => {
                let posts2 = data;
                this.instaPosts.first = this.instaPosts.first.concat(posts2.first);
                this.instaPosts.second = this.instaPosts.second.concat(posts2.second);
                this.instaPosts.third = this.instaPosts.third.concat(posts2.third);
                return data;
            }
        });
    }
    updateTwitterFeed() {
        let observableTweets = this.restService.getTwitterPostsPaged(this.twitterpageNum);
        observableTweets.subscribe({
            next: data => {
                let posts2 = data;
                this.twitterPosts.first = this.twitterPosts.first.concat(posts2.first);
                this.twitterPosts.second = this.twitterPosts.second.concat(posts2.second);
                return data;
            }
        });
    }
    updateRedditFeed() {
        let observableReddits = this.restService.getRedditPostsPaged(this.redditpageNum);
        observableReddits.subscribe({
            next: data => {
                let posts2 = data;
                this.redditPosts.first = this.redditPosts.first.concat(posts2.first);
                this.redditPosts.second = this.redditPosts.second.concat(posts2.second);
                return data;
            }
        });
    }
    getUrl(code) {
        return environment/* environment.f1exposureUrl */.N.f1exposureUrl + 'getImage/' + code;
    }
    getInstagramUrl(code) {
        return 'https://www.instagram.com/p/' + code;
    }
    safeStreamableSource(url) {
        return this.sanitizer.bypassSecurityTrustResourceUrl(url);
    }
}
InstagramFeedComponent.ɵfac = function InstagramFeedComponent_Factory(t) { return new (t || InstagramFeedComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(platform_browser/* DomSanitizer */.H7)); };
InstagramFeedComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: InstagramFeedComponent, selectors: [["instagram-feed-cmp"]], decls: 30, vars: 4, consts: [["infinite-scroll", "", 3, "infiniteScrollDistance", "scrolled"], ["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], [3, "selectedTabChange"], ["label", "Instagram"], ["mat-tab-label", ""], ["matTabContent", ""], ["label", "Twitter"], ["label", "Reddit"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], ["src", "assets/img/instagram-icon.ico"], [1, "col-lg-4", "col-12", "no-right-padding"], ["class", "instagram-post-margin", 4, "ngFor", "ngForOf"], [1, "col-lg-4", "col-12", "no-padding"], [1, "instagram-post-margin"], [1, "container-fluid"], [1, "col-lg-4", "col-4", "instagram-post-info", "single-line"], [1, "col-lg-4", "col-4", "instagram-post-info"], ["target", "_blank", 3, "href"], [1, "w-100", 3, "src"], [1, "instagram-post-info"], ["src", "assets/img/twitter-icon.png"], [1, "row", "not-container-fluid"], [1, "col-lg-6", "col-12", "no-padding"], [1, "div-half", "twitter-post-info"], [1, "div-quarter", "twitter-post-info"], [1, "div-quarter", "twitter-post-info", "single-line"], ["class", "text-center", 4, "ngIf"], ["class", "div-left-header bg-dark-blue", 4, "ngIf"], [1, "text-center"], [3, "src"], [1, "div-left-header", "bg-dark-blue"], [1, "aaa"], [1, "bg-dark-blue", "tweet-without-image"], ["target", "_blank", 3, "href", 4, "ngIf"], ["src", "assets/img/reddit-icon.png"], [1, "reddit-post-info"]], template: function InstagramFeedComponent_Template(rf, ctx) { if (rf & 1) {
        const _r33 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵlistener */.NdJ("scrolled", function InstagramFeedComponent_Template_div_scrolled_0_listener() { return ctx.onScrollDown(); });
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 1);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 2, 3);
        core/* ɵɵlistener */.NdJ("openedChange", function InstagramFeedComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 4);
        core/* ɵɵelementStart */.TgZ(5, "div", 5);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 6);
        core/* ɵɵlistener */.NdJ("ngModelChange", function InstagramFeedComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 7);
        core/* ɵɵelementStart */.TgZ(9, "button", 8);
        core/* ɵɵlistener */.NdJ("click", function InstagramFeedComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(7); });
        core/* ɵɵtext */._uU(10, "Post ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 8);
        core/* ɵɵlistener */.NdJ("click", function InstagramFeedComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(7); });
        core/* ɵɵtext */._uU(12, " Reload ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, InstagramFeedComponent_div_13_Template, 8, 3, "div", 9);
        core/* ɵɵelementStart */.TgZ(14, "div", 10);
        core/* ɵɵelementStart */.TgZ(15, "button", 11);
        core/* ɵɵlistener */.NdJ("click", function InstagramFeedComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r33); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 12);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 10);
        core/* ɵɵelementStart */.TgZ(18, "button", 11);
        core/* ɵɵlistener */.NdJ("click", function InstagramFeedComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r33); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 12);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "mat-tab-group", 13);
        core/* ɵɵlistener */.NdJ("selectedTabChange", function InstagramFeedComponent_Template_mat_tab_group_selectedTabChange_20_listener($event) { return ctx.tabChanged($event); });
        core/* ɵɵelementStart */.TgZ(21, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(22, InstagramFeedComponent_ng_template_22_Template, 1, 0, "ng-template", 15);
        core/* ɵɵtemplate */.YNc(23, InstagramFeedComponent_ng_template_23_Template, 7, 3, "ng-template", 16);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(24, "mat-tab", 17);
        core/* ɵɵtemplate */.YNc(25, InstagramFeedComponent_ng_template_25_Template, 1, 0, "ng-template", 15);
        core/* ɵɵtemplate */.YNc(26, InstagramFeedComponent_ng_template_26_Template, 5, 2, "ng-template", 16);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(27, "mat-tab", 18);
        core/* ɵɵtemplate */.YNc(28, InstagramFeedComponent_ng_template_28_Template, 1, 0, "ng-template", 15);
        core/* ɵɵtemplate */.YNc(29, InstagramFeedComponent_ng_template_29_Template, 5, 2, "ng-template", 16);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵproperty */.Q6J("infiniteScrollDistance", 2);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
    } }, directives: [InfiniteScrollDirective, sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabLabel */.uD, tabs/* MatTabContent */.Vc, common/* NgIf */.O5], encapsulation: 2 });

// EXTERNAL MODULE: ./node_modules/@ng-bootstrap/ng-bootstrap/__ivy_ngcc__/fesm2015/ng-bootstrap.js + 7 modules
var ng_bootstrap = __webpack_require__(674);
;// CONCATENATED MODULE: ./src/app/config/carousel-config.ts




function NgbdCarouselConfig_ngb_carousel_0_ng_template_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 2);
    core/* ɵɵelementStart */.TgZ(1, "div", 3);
    core/* ɵɵelementStart */.TgZ(2, "h3");
    core/* ɵɵtext */._uU(3, "10 seconds between slides...");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "This carousel uses customized default values.");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r1 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("src", ctx_r1.images[0], core/* ɵɵsanitizeUrl */.LSH);
} }
function NgbdCarouselConfig_ngb_carousel_0_ng_template_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 4);
    core/* ɵɵelementStart */.TgZ(1, "div", 3);
    core/* ɵɵelementStart */.TgZ(2, "h3");
    core/* ɵɵtext */._uU(3, "No mouse events...");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "This carousel doesn't pause or resume on mouse events");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("src", ctx_r2.images[1], core/* ɵɵsanitizeUrl */.LSH);
} }
function NgbdCarouselConfig_ngb_carousel_0_ng_template_3_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 5);
    core/* ɵɵelementStart */.TgZ(1, "div", 3);
    core/* ɵɵelementStart */.TgZ(2, "h3");
    core/* ɵɵtext */._uU(3, "No keyboard...");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "This carousel uses customized default values.");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("src", ctx_r3.images[2], core/* ɵɵsanitizeUrl */.LSH);
} }
function NgbdCarouselConfig_ngb_carousel_0_ng_template_4_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "img", 6);
    core/* ɵɵelementStart */.TgZ(1, "div", 3);
    core/* ɵɵelementStart */.TgZ(2, "h3");
    core/* ɵɵtext */._uU(3, "And no wrap after last slide.");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "This carousel uses customized default values.");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("src", ctx_r4.images[3], core/* ɵɵsanitizeUrl */.LSH);
} }
function NgbdCarouselConfig_ngb_carousel_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "ngb-carousel");
    core/* ɵɵtemplate */.YNc(1, NgbdCarouselConfig_ngb_carousel_0_ng_template_1_Template, 6, 1, "ng-template", 1);
    core/* ɵɵtemplate */.YNc(2, NgbdCarouselConfig_ngb_carousel_0_ng_template_2_Template, 6, 1, "ng-template", 1);
    core/* ɵɵtemplate */.YNc(3, NgbdCarouselConfig_ngb_carousel_0_ng_template_3_Template, 6, 1, "ng-template", 1);
    core/* ɵɵtemplate */.YNc(4, NgbdCarouselConfig_ngb_carousel_0_ng_template_4_Template, 6, 1, "ng-template", 1);
    core/* ɵɵelementEnd */.qZA();
} }
class NgbdCarouselConfig {
    constructor(config) {
        this.images = [1, 2, 3, 4].map(() => `https://picsum.photos/900/500?random&t=${Math.random()}`);
        // customize default values of carousels used by this component tree
        config.interval = 3000;
        config.wrap = true;
        config.keyboard = false;
        config.pauseOnHover = false;
    }
}
NgbdCarouselConfig.ɵfac = function NgbdCarouselConfig_Factory(t) { return new (t || NgbdCarouselConfig)(core/* ɵɵdirectiveInject */.Y36(ng_bootstrap/* NgbCarouselConfig */.Lu)); };
NgbdCarouselConfig.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: NgbdCarouselConfig, selectors: [["ngbd-carousel-config"]], features: [core/* ɵɵProvidersFeature */._Bn([ng_bootstrap/* NgbCarouselConfig */.Lu] // add NgbCarouselConfig to the component providers
        )], decls: 1, vars: 1, consts: [[4, "ngIf"], ["ngbSlide", ""], ["alt", "Random first slide", 1, "img-fluid", 3, "src"], [1, "carousel-caption"], ["alt", "Random second slide", 1, "img-fluid", 3, "src"], ["alt", "Random third slide", 1, "img-fluid", 3, "src"], ["alt", "Random fourth slide", 1, "img-fluid", 3, "src"]], template: function NgbdCarouselConfig_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵtemplate */.YNc(0, NgbdCarouselConfig_ngb_carousel_0_Template, 5, 0, "ngb-carousel", 0);
    } if (rf & 2) {
        core/* ɵɵproperty */.Q6J("ngIf", ctx.images);
    } }, directives: [common/* NgIf */.O5, ng_bootstrap/* NgbCarousel */.uo, ng_bootstrap/* NgbSlide */.xl], styles: [".img-fluid[_ngcontent-%COMP%]{min-width:100%}"] });

;// CONCATENATED MODULE: ./src/app/model/AwsContent.ts
class AwsContent {
}
class AwsComment {
}

;// CONCATENATED MODULE: ./src/app/shared/dialog/submit-content-dialog.component.ts













class SubmitContentDialog {
    constructor(restService, utilityService, router, dialogRef) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.router = router;
        this.dialogRef = dialogRef;
        this.newContent = new AwsContent();
    }
    sendMessage() {
        this.newContent.username = localStorage.getItem('f1-chosen-nickname');
        let observableStandings = this.restService.postContent(this.newContent);
        observableStandings.subscribe({
            next: data => {
                this.newContent = new AwsContent();
                this.utilityService.pushToastrMessage("Posted.");
                this.dialogRef.close();
                this.router.navigate(['aws'], { queryParams: { code: data } });
                return data;
            },
            error: error => {
                this.utilityService.pushToastrMessage("Error occured.");
                console.error('There was an error!', error);
            }
        });
    }
}
SubmitContentDialog.ɵfac = function SubmitContentDialog_Factory(t) { return new (t || SubmitContentDialog)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(router/* Router */.F0), core/* ɵɵdirectiveInject */.Y36(MatDialogRef)); };
SubmitContentDialog.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: SubmitContentDialog, selectors: [["submit-content-dialog"]], decls: 18, vars: 3, consts: [["mat-dialog-content", "", 1, "min-dialog-size", "overflow-hidden"], [1, "row"], [1, "col-lg-12", "col-md-6", "col-sm-6"], [1, "col-md-12"], ["placeholder", "title", "type", "text", 1, "form-control", 3, "ngModel", "ngModelChange"], ["placeholder", "url", "type", "text", 1, "form-control", 3, "ngModel", "ngModelChange"], [1, "form-group"], ["matInput", "", 1, "form-control", "message-text-area-50vh", 3, "ngModel", "ngModelChange"], [1, "row", "align-bottom"], ["mat-raised-button", "", 1, "post-comment-button", "bg-success", "text-white", 3, "click"]], template: function SubmitContentDialog_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵelementStart */.TgZ(1, "div", 1);
        core/* ɵɵelementStart */.TgZ(2, "div", 2);
        core/* ɵɵelementStart */.TgZ(3, "div", 1);
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "label");
        core/* ɵɵtext */._uU(6, "Title");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(7, "input", 4);
        core/* ɵɵlistener */.NdJ("ngModelChange", function SubmitContentDialog_Template_input_ngModelChange_7_listener($event) { return ctx.newContent.title = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "label");
        core/* ɵɵtext */._uU(9, "URL (not required)");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(10, "input", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function SubmitContentDialog_Template_input_ngModelChange_10_listener($event) { return ctx.newContent.url = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "div", 6);
        core/* ɵɵelementStart */.TgZ(12, "label");
        core/* ɵɵtext */._uU(13, "Text");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(14, "textarea", 7);
        core/* ɵɵlistener */.NdJ("ngModelChange", function SubmitContentDialog_Template_textarea_ngModelChange_14_listener($event) { return ctx.newContent.textContent = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(15, "div", 8);
        core/* ɵɵelementStart */.TgZ(16, "button", 9);
        core/* ɵɵlistener */.NdJ("click", function SubmitContentDialog_Template_button_click_16_listener() { return ctx.sendMessage(); });
        core/* ɵɵtext */._uU(17, " POST ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(7);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.newContent.title);
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.newContent.url);
        core/* ɵɵadvance */.xp6(4);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.newContent.textContent);
    } }, directives: [MatDialogContent, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, MatInput, fesm2015_button/* MatButton */.lW], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/poweredByAws/powered-by-aws.component.ts















function PoweredByAwsComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 19);
    core/* ɵɵelementStart */.TgZ(2, "div", 20);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 21);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 22);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r3 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r3.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r3.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r3.comment);
} }
function PoweredByAwsComponent_tr_29_Template(rf, ctx) { if (rf & 1) {
    const _r7 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "tr", 23);
    core/* ɵɵelement */._UZ(1, "td", 24);
    core/* ɵɵelementStart */.TgZ(2, "td", 25);
    core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_tr_29_Template_td_click_2_listener() { const restoredCtx = core/* ɵɵrestoreView */.CHM(_r7); const post_r4 = restoredCtx.$implicit; const ctx_r6 = core/* ɵɵnextContext */.oxw(); return ctx_r6.openPost(post_r4.code); });
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "td", 26);
    core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_tr_29_Template_td_click_4_listener() { const restoredCtx = core/* ɵɵrestoreView */.CHM(_r7); const post_r4 = restoredCtx.$implicit; const ctx_r8 = core/* ɵɵnextContext */.oxw(); return ctx_r8.openPost(post_r4.code); });
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "td", 24);
    core/* ɵɵelementStart */.TgZ(7, "button", 27);
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(9, "td", 24);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const post_r4 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(post_r4.title);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate1 */.hij("by ", post_r4.username, "");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngClass", post_r4.url == null ? "btn-outline-info" : "btn-outline-danger");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate1 */.hij("", post_r4.commentCount, " ");
} }
class PoweredByAwsComponent {
    constructor(restService, utilityService, router, dialog) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.router = router;
        this.dialog = dialog;
        this.utilityService.setTitle("Latest");
        let observableEvents = this.restService.getAwsContent(0);
        observableEvents.subscribe({
            next: data => {
                this.posts = data;
                return data;
            }
        });
        this.utilityService.reloadPosts(9);
    }
    submit() {
        this.dialog.open(SubmitContentDialog);
    }
    openPost(code) {
        this.router.navigate(['aws'], { queryParams: { code: code } });
    }
}
PoweredByAwsComponent.ɵfac = function PoweredByAwsComponent_Factory(t) { return new (t || PoweredByAwsComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(router/* Router */.F0), core/* ɵɵdirectiveInject */.Y36(MatDialog)); };
PoweredByAwsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: PoweredByAwsComponent, selectors: [["powered-by-aws-cmp"]], decls: 30, vars: 4, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], [1, "height-fill-screen"], [1, "col-lg-12", "col-md-6", "col-sm-6"], [1, "card", "card-stats"], [1, "post-comment", "align-middle"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-white", 3, "click"], [1, "table", "table-striped"], ["class", "hover-effects-bold-row", 4, "ngFor", "ngForOf"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], [1, "hover-effects-bold-row"], [1, "width-10"], [3, "click"], [1, "width-7rem", 3, "click"], [1, "btn", "btn-sm", "btn-round", "btn-icon", 3, "ngClass"]], template: function PoweredByAwsComponent_Template(rf, ctx) { if (rf & 1) {
        const _r9 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function PoweredByAwsComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function PoweredByAwsComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(9); });
        core/* ɵɵtext */._uU(10, "Post");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(9); });
        core/* ɵɵtext */._uU(12, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, PoweredByAwsComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r9); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r9); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "div", 12);
        core/* ɵɵelementStart */.TgZ(21, "div", 3);
        core/* ɵɵelementStart */.TgZ(22, "div", 13);
        core/* ɵɵelementStart */.TgZ(23, "div", 14);
        core/* ɵɵelementStart */.TgZ(24, "div", 15);
        core/* ɵɵelementStart */.TgZ(25, "button", 16);
        core/* ɵɵlistener */.NdJ("click", function PoweredByAwsComponent_Template_button_click_25_listener() { return ctx.submit(); });
        core/* ɵɵtext */._uU(26, "Submit new");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(27, "table", 17);
        core/* ɵɵelementStart */.TgZ(28, "tbody");
        core/* ɵɵtemplate */.YNc(29, PoweredByAwsComponent_tr_29_Template, 10, 4, "tr", 18);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
        core/* ɵɵadvance */.xp6(16);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.posts);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, common/* NgClass */.mk], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/poweredByAws/aws.component.ts















function AwsComponent_div_10_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 14);
    core/* ɵɵelementStart */.TgZ(1, "div", 15);
    core/* ɵɵelement */._UZ(2, "i", 16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} }
function AwsComponent_div_11_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 14);
    core/* ɵɵelementStart */.TgZ(1, "a", 17);
    core/* ɵɵelementStart */.TgZ(2, "div", 18);
    core/* ɵɵelementStart */.TgZ(3, "div", 15);
    core/* ɵɵelement */._UZ(4, "i", 19);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "span", 8);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r1 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("href", ctx_r1.post.url, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r1.getDomain());
} }
function AwsComponent_div_31_Template(rf, ctx) { if (rf & 1) {
    const _r5 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 20);
    core/* ɵɵelementStart */.TgZ(2, "div");
    core/* ɵɵelementStart */.TgZ(3, "div", 21);
    core/* ɵɵlistener */.NdJ("click", function AwsComponent_div_31_Template_div_click_3_listener() { const restoredCtx = core/* ɵɵrestoreView */.CHM(_r5); const comment_r3 = restoredCtx.$implicit; const ctx_r4 = core/* ɵɵnextContext */.oxw(); return ctx_r4.addressUser(comment_r3.username); });
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 22);
    core/* ɵɵlistener */.NdJ("click", function AwsComponent_div_31_Template_div_click_5_listener() { const restoredCtx = core/* ɵɵrestoreView */.CHM(_r5); const comment_r3 = restoredCtx.$implicit; const ctx_r6 = core/* ɵɵnextContext */.oxw(); return ctx_r6.addressUser("" + comment_r3.id); });
    core/* ɵɵtext */._uU(6);
    core/* ɵɵpipe */.ALo(7, "date");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "div", 23);
    core/* ɵɵtext */._uU(9);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r3 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate */.Oqu(comment_r3.username);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate2 */.AsE("", core/* ɵɵpipeBind2 */.xi3(7, 4, comment_r3.timestampCreated, "medium"), " - No.", comment_r3.id, "");
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r3.textContent);
} }
class AwsComponent {
    constructor(restService, utilityService, route, router, dialog) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.route = route;
        this.router = router;
        this.dialog = dialog;
        this.commentText = '';
        this.currentReloadCounter = 1;
        this.minimumReloadCounterValue = 0;
    }
    ngOnInit() {
        this.utilityService.disablePeriodicFetchOfComments();
        this.route.queryParams.subscribe(params => {
            let selectedCode = params['code'];
            let observableEvents = this.restService.getAwsPost(selectedCode);
            observableEvents.subscribe({
                next: data => {
                    this.post = data;
                    if (this.post == null) {
                        this.goToAwsList();
                    }
                    else {
                        this.utilityService.setTitle(this.post.title);
                    }
                },
                error: error => {
                    console.error('There was an error!', error);
                }
            });
        });
        this.commentsReloader = setInterval(() => { this.getAwsCommentsPeriodically(); }, 20000);
        this.minimumReloadCounterValue = 0;
        this.currentReloadCounter = 1;
    }
    ngOnDestroy() {
        this.minimumReloadCounterValue = 0;
        this.currentReloadCounter = 1;
        clearInterval(this.commentsReloader);
    }
    submit() {
        let dialogRef = this.dialog.open(SubmitContentDialog);
    }
    goToAwsList() {
        this.router.navigate(['poweredByAws']);
    }
    postAwsComment() {
        let awsComment = new AwsComment();
        if (this.commentText.trim() != '') {
            awsComment.contentCode = this.post.code;
            awsComment.textContent = this.commentText;
            awsComment.username = localStorage.getItem('f1-chosen-nickname');
            let observable = this.restService.postAwsComment(awsComment);
            observable.subscribe({
                next: data => {
                    this.post.comments = data;
                    this.commentText = '';
                },
                error: error => {
                    console.error('There was an error!', error);
                    this.utilityService.pushToastrErrorMessage("ERROR OCCURED. Comment not posted.");
                }
            });
            this.minimumReloadCounterValue = 0;
            this.currentReloadCounter = 1;
        }
    }
    reloadAwsComments() {
        this.getAwsComments();
        this.minimumReloadCounterValue = 0;
        this.currentReloadCounter = 1;
    }
    getAwsCommentsPeriodically() {
        if (this.currentReloadCounter > this.minimumReloadCounterValue) {
            this.getAwsComments();
            this.minimumReloadCounterValue++;
            this.currentReloadCounter = 0;
        }
        else {
            this.currentReloadCounter++;
        }
    }
    getAwsComments() {
        let observable = this.restService.getAwsComments(this.post.code);
        observable.subscribe({
            next: data => {
                this.post.comments = data;
            },
            error: error => {
                console.error('There was an error!', error);
            }
        });
    }
    addressUser(text) {
        this.commentText = this.commentText + "@" + text + " - ";
    }
    getDomain() {
        let url = this.post.url;
        let start = 0;
        if (this.post.url.indexOf('//') > 0) {
            start = this.post.url.indexOf('//') + 2;
        }
        url = this.post.url.substring(start);
        if (url.indexOf('/') > 0) {
            url = url.substring(0, url.indexOf('/'));
        }
        return url;
    }
    myFunc() {
        alert('tempo1');
    }
}
AwsComponent.ɵfac = function AwsComponent_Factory(t) { return new (t || AwsComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(router/* ActivatedRoute */.gz), core/* ɵɵdirectiveInject */.Y36(router/* Router */.F0), core/* ɵɵdirectiveInject */.Y36(MatDialog)); };
AwsComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: AwsComponent, selectors: [["aws-cmp"]], decls: 32, vars: 10, consts: [[1, "height-fill-screen"], [1, "div-padded-left-right-1"], [1, "card", "card-stats"], [1, "card-body"], [1, "row"], [1, "col-12", "col-lg-12", "div-padded-always-left-right-1-2", "text-right"], ["class", "col-12 col-lg-2 no-right-padding", 4, "ngIf"], [1, "pre-line", "col-12", "col-lg-10", "div-padded-always-left-right-1-2"], [1, "teko-text-small"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area-100", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "bg-success", "text-white", 3, "click"], [4, "ngFor", "ngForOf"], [1, "col-12", "col-lg-2", "no-right-padding"], [1, "icon-big", "text-center", "icon-warning"], [1, "nc-icon", "nc-align-left-2", "text-success"], ["target", "_blank", 3, "href"], [1, "text-center"], [1, "nc-icon", "nc-globe", "text-success"], [1, "basic-comment"], [1, "one-aws", 3, "click"], [1, "two-aws", 3, "click"], [1, "basic-comment-body"]], template: function AwsComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "div", 0);
        core/* ɵɵelementStart */.TgZ(2, "div", 1);
        core/* ɵɵelementStart */.TgZ(3, "div", 2);
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div", 5);
        core/* ɵɵtext */._uU(7);
        core/* ɵɵpipe */.ALo(8, "date");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(9, "div", 4);
        core/* ɵɵtemplate */.YNc(10, AwsComponent_div_10_Template, 3, 0, "div", 6);
        core/* ɵɵtemplate */.YNc(11, AwsComponent_div_11_Template, 7, 2, "div", 6);
        core/* ɵɵelementStart */.TgZ(12, "div", 7);
        core/* ɵɵelementStart */.TgZ(13, "div");
        core/* ɵɵtext */._uU(14);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(15, "div");
        core/* ɵɵelement */._UZ(16, "br");
        core/* ɵɵelementStart */.TgZ(17, "span", 8);
        core/* ɵɵtext */._uU(18, "Kind regards,");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(19, "br");
        core/* ɵɵelementStart */.TgZ(20, "span", 8);
        core/* ɵɵtext */._uU(21);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(22, "div", 4);
        core/* ɵɵelementStart */.TgZ(23, "div", 9);
        core/* ɵɵelementStart */.TgZ(24, "div");
        core/* ɵɵelementStart */.TgZ(25, "textarea", 10);
        core/* ɵɵlistener */.NdJ("ngModelChange", function AwsComponent_Template_textarea_ngModelChange_25_listener($event) { return ctx.commentText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(26, "div", 11);
        core/* ɵɵelementStart */.TgZ(27, "button", 12);
        core/* ɵɵlistener */.NdJ("click", function AwsComponent_Template_button_click_27_listener() { return ctx.postAwsComment(); });
        core/* ɵɵtext */._uU(28, "Comment");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(29, "button", 12);
        core/* ɵɵlistener */.NdJ("click", function AwsComponent_Template_button_click_29_listener() { return ctx.reloadAwsComments(); });
        core/* ɵɵtext */._uU(30, "Reload");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(31, AwsComponent_div_31_Template, 10, 7, "div", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(7);
        core/* ɵɵtextInterpolate1 */.hij("", core/* ɵɵpipeBind2 */.xi3(8, 7, ctx.post.timestampCreated, "medium"), " ");
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.post.url == null);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.post.url != null);
        core/* ɵɵadvance */.xp6(3);
        core/* ɵɵtextInterpolate1 */.hij("", ctx.post.textContent, " ");
        core/* ɵɵadvance */.xp6(7);
        core/* ɵɵtextInterpolate1 */.hij("", ctx.post.username, ".");
        core/* ɵɵadvance */.xp6(4);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.commentText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.post.comments);
    } }, directives: [common/* NgIf */.O5, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg], pipes: [common/* DatePipe */.uU], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/model/commentModeration.ts
class CommentModeration {
}

// EXTERNAL MODULE: ./node_modules/ng2-adsense/__ivy_ngcc__/fesm2015/ng2-adsense.js
var ng2_adsense = __webpack_require__(5425);
;// CONCATENATED MODULE: ./src/app/pages/moderator/moderator.component.ts

















function ModeratorComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    const _r6 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 11);
    core/* ɵɵelementStart */.TgZ(1, "p");
    core/* ɵɵtext */._uU(2, "Are you a moderator");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtext */._uU(3, " adtest1-- ");
    core/* ɵɵelement */._UZ(4, "ng-adsense", 12);
    core/* ɵɵtext */._uU(5, " -- ");
    core/* ɵɵelementStart */.TgZ(6, "mat-form-field");
    core/* ɵɵelementStart */.TgZ(7, "mat-select", 13);
    core/* ɵɵlistener */.NdJ("valueChange", function ModeratorComponent_div_13_Template_mat_select_valueChange_7_listener($event) { core/* ɵɵrestoreView */.CHM(_r6); const ctx_r5 = core/* ɵɵnextContext */.oxw(); return ctx_r5.answer1 = $event; });
    core/* ɵɵelementStart */.TgZ(8, "mat-option", 14);
    core/* ɵɵtext */._uU(9, "No");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "mat-option", 15);
    core/* ɵɵtext */._uU(11, "Yes");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r0 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵproperty */.Q6J("adClient", "ca-pub-1302230400221331")("adSlot", 5686017586)("display", "inline-block")("adFormat", "horizontal")("width", 320)("height", 108);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("value", ctx_r0.answer1);
} }
function ModeratorComponent_div_14_Template(rf, ctx) { if (rf & 1) {
    const _r8 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 11);
    core/* ɵɵtext */._uU(1, " adtest2--- ");
    core/* ɵɵelement */._UZ(2, "ng-adsense", 16);
    core/* ɵɵtext */._uU(3, " -- ");
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "What is your wage?");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "mat-form-field");
    core/* ɵɵelementStart */.TgZ(7, "mat-select", 17);
    core/* ɵɵlistener */.NdJ("selectionChange", function ModeratorComponent_div_14_Template_mat_select_selectionChange_7_listener() { core/* ɵɵrestoreView */.CHM(_r8); const ctx_r7 = core/* ɵɵnextContext */.oxw(); return ctx_r7.verification(); })("ngModelChange", function ModeratorComponent_div_14_Template_mat_select_ngModelChange_7_listener($event) { core/* ɵɵrestoreView */.CHM(_r8); const ctx_r9 = core/* ɵɵnextContext */.oxw(); return ctx_r9.answer2 = $event; })("valueChange", function ModeratorComponent_div_14_Template_mat_select_valueChange_7_listener($event) { core/* ɵɵrestoreView */.CHM(_r8); const ctx_r10 = core/* ɵɵnextContext */.oxw(); return ctx_r10.answer2 = $event; });
    core/* ɵɵelementStart */.TgZ(8, "mat-option", 18);
    core/* ɵɵtext */._uU(9, "1\u20AC per action");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(10, "mat-option", 15);
    core/* ɵɵtext */._uU(11, "1000 \u20AC");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "mat-option", 19);
    core/* ɵɵtext */._uU(13, "1 Pepe");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "mat-option", 20);
    core/* ɵɵtext */._uU(15, "1 BTC");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "mat-option", 21);
    core/* ɵɵtext */._uU(17, "I do it for free!");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r1 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("adClient", "ca-pub-1302230400221331")("adSlot", 5686017586)("display", "inline-block")("adFormat", "vertical");
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r1.answer2)("value", ctx_r1.answer2);
} }
function ModeratorComponent_div_15_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 11);
    core/* ɵɵtext */._uU(1, " adtest3a--- ");
    core/* ɵɵelement */._UZ(2, "ng-adsense", 16);
    core/* ɵɵtext */._uU(3, " -- ");
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "Verification");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "p", 22);
    core/* ɵɵtext */._uU(7, "FAILED");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("adClient", "ca-pub-1302230400221331")("adSlot", 5686017586)("display", "inline-block")("adFormat", "auto");
} }
function ModeratorComponent_div_16_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 11);
    core/* ɵɵtext */._uU(1, " adtest3a--- ");
    core/* ɵɵelement */._UZ(2, "ng-adsense", 23);
    core/* ɵɵtext */._uU(3, " -- ");
    core/* ɵɵelementStart */.TgZ(4, "p");
    core/* ɵɵtext */._uU(5, "Verification");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "p", 24);
    core/* ɵɵtext */._uU(7, "SUCCESS");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("adClient", "ca-pub-1302230400221331")("adSlot", 5686017586)("adFormat", "auto");
} }
function ModeratorComponent_div_17_mat_option_38_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-option", 38);
    core/* ɵɵtext */._uU(1);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const option_r12 = ctx.$implicit;
    core/* ɵɵproperty */.Q6J("value", option_r12);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate1 */.hij(" ", option_r12, " ");
} }
function ModeratorComponent_div_17_Template(rf, ctx) { if (rf & 1) {
    const _r14 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div", 1);
    core/* ɵɵelementStart */.TgZ(1, "div", 2);
    core/* ɵɵelementStart */.TgZ(2, "div", 3);
    core/* ɵɵelementStart */.TgZ(3, "span");
    core/* ɵɵtext */._uU(4, "MODERATION HUB ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelement */._UZ(5, "hr");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 4);
    core/* ɵɵelementStart */.TgZ(7, "div", 25);
    core/* ɵɵelementStart */.TgZ(8, "div", 26);
    core/* ɵɵelementStart */.TgZ(9, "div", 27);
    core/* ɵɵtext */._uU(10, "Select panel");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "mat-radio-group", 28);
    core/* ɵɵlistener */.NdJ("ngModelChange", function ModeratorComponent_div_17_Template_mat_radio_group_ngModelChange_11_listener($event) { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r13 = core/* ɵɵnextContext */.oxw(); return ctx_r13.moderation.panel = $event; });
    core/* ɵɵelementStart */.TgZ(12, "div");
    core/* ɵɵelementStart */.TgZ(13, "mat-radio-button", 29);
    core/* ɵɵtext */._uU(14, "Sidebar");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "div");
    core/* ɵɵelementStart */.TgZ(16, "mat-radio-button", 30);
    core/* ɵɵtext */._uU(17, "AWS");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "div", 26);
    core/* ɵɵelementStart */.TgZ(19, "div", 27);
    core/* ɵɵtext */._uU(20, "Select action");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "mat-radio-group", 31);
    core/* ɵɵlistener */.NdJ("change", function ModeratorComponent_div_17_Template_mat_radio_group_change_21_listener($event) { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r15 = core/* ɵɵnextContext */.oxw(); return ctx_r15.actionChoice($event); })("ngModelChange", function ModeratorComponent_div_17_Template_mat_radio_group_ngModelChange_21_listener($event) { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r16 = core/* ɵɵnextContext */.oxw(); return ctx_r16.moderation.action = $event; });
    core/* ɵɵelementStart */.TgZ(22, "div");
    core/* ɵɵelementStart */.TgZ(23, "mat-radio-button", 30);
    core/* ɵɵtext */._uU(24, "Delete comment");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(25, "div");
    core/* ɵɵelementStart */.TgZ(26, "mat-radio-button", 29);
    core/* ɵɵtext */._uU(27, "Restore comment");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(28, "div", 26);
    core/* ɵɵelementStart */.TgZ(29, "div", 32);
    core/* ɵɵtext */._uU(30, "Comment ID:");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(31, "mat-form-field", 33);
    core/* ɵɵelementStart */.TgZ(32, "input", 34);
    core/* ɵɵlistener */.NdJ("ngModelChange", function ModeratorComponent_div_17_Template_input_ngModelChange_32_listener($event) { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r17 = core/* ɵɵnextContext */.oxw(); return ctx_r17.moderation.commentId = $event; });
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(33, "div", 26);
    core/* ɵɵelementStart */.TgZ(34, "div", 32);
    core/* ɵɵtext */._uU(35, "Reason");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(36, "mat-form-field");
    core/* ɵɵelementStart */.TgZ(37, "mat-select", 35);
    core/* ɵɵlistener */.NdJ("ngModelChange", function ModeratorComponent_div_17_Template_mat_select_ngModelChange_37_listener($event) { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r18 = core/* ɵɵnextContext */.oxw(); return ctx_r18.moderation.reason = $event; });
    core/* ɵɵtemplate */.YNc(38, ModeratorComponent_div_17_mat_option_38_Template, 2, 2, "mat-option", 36);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(39, "button", 37);
    core/* ɵɵlistener */.NdJ("click", function ModeratorComponent_div_17_Template_button_click_39_listener() { core/* ɵɵrestoreView */.CHM(_r14); const ctx_r19 = core/* ɵɵnextContext */.oxw(); return ctx_r19.executeModeratorAction(); });
    core/* ɵɵtext */._uU(40, "Execute");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵadvance */.xp6(11);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r4.moderation.panel);
    core/* ɵɵadvance */.xp6(10);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r4.moderation.action);
    core/* ɵɵadvance */.xp6(11);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r4.moderation.commentId);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("ngModel", ctx_r4.moderation.reason);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r4.reasons);
} }
class ModeratorComponent {
    constructor(restService, utilityService, toastr) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.toastr = toastr;
        this.reasonsToDelete = ['Unspecified', 'Spam', 'Hate speech', 'I don\'t like them', 'Suspected Australian'];
        this.reasonsToRestore = ['Unspecified', 'Mistake', 'They\'re sorry', 'I\'m sorry'];
        this.moderation = new CommentModeration();
        this.utilityService.setTitle("Restricted moderation area");
        this.answer1 = 0;
        this.answer2 = 0;
        this.verified = 0;
    }
    verification() {
        this.answer1 = 3;
        if (this.answer2 == 5 && this.verified == 0) {
            this.verified = 1;
        }
        else {
            this.verified = 2;
        }
    }
    executeModeratorAction() {
        if (isNaN(this.moderation.commentId)) {
            this.moderation.commentId = null;
        }
        if (this.moderation.panel == null || this.moderation.action == null || this.moderation.reason == null || this.moderation.commentId == null) {
            this.toastr.error('Panel, action, reason and commentId are mandatory', '', {
                timeOut: 4000,
                closeButton: false,
                enableHtml: false,
                toastClass: 'alert alert-danger',
                positionClass: 'toast-top-center'
            });
        }
        else {
            let observableEvent = this.restService.moderateComment(this.moderation);
            observableEvent.subscribe({
                next: data => {
                    this.response = data;
                    if (this.response.state > 0) {
                        this.moderation = new CommentModeration();
                        this.toastr.success(this.response.message, '', {
                            timeOut: 4000,
                            closeButton: false,
                            enableHtml: false,
                            toastClass: 'alert alert-info',
                            positionClass: 'toast-top-center'
                        });
                    }
                    if (this.response.state <= 0) {
                        this.toastr.error("Unable to find comment no." + this.moderation.commentId, '', {
                            timeOut: 4000,
                            closeButton: false,
                            enableHtml: false,
                            toastClass: 'alert alert-danger',
                            positionClass: 'toast-top-center'
                        });
                    }
                    return data;
                }
            });
        }
    }
    actionChoice(event) {
        this.moderation.reason = null;
        if (this.moderation.action == 2) {
            this.reasons = this.reasonsToDelete;
        }
        if (this.moderation.action == 1) {
            this.reasons = this.reasonsToRestore;
        }
    }
}
ModeratorComponent.ɵfac = function ModeratorComponent_Factory(t) { return new (t || ModeratorComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(ngx_toastr/* ToastrService */._W)); };
ModeratorComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: ModeratorComponent, selectors: [["moderator-cmp"]], decls: 18, vars: 5, consts: [[1, "height-fill-screen"], [1, "div-padded-left-2", "div-padded-top-1", "col-lg-12", "col-md-12", "col-sm-12"], [1, "card", "card-stats"], [1, "card-header"], [1, "card-body"], [1, "row"], [1, "col-lg-2", "col-2", "no-right-padding"], [1, "icon-big", "text-center", "icon-warning"], [1, "nc-icon", "nc-circle-10", "text-warning"], ["class", "col-lg-4 col-8", 4, "ngIf"], ["class", "div-padded-left-2 div-padded-top-1 col-lg-12 col-md-12 col-sm-12", 4, "ngIf"], [1, "col-lg-4", "col-8"], [3, "adClient", "adSlot", "display", "adFormat", "width", "height"], [3, "value", "valueChange"], ["default", "", "value", "1"], ["value", "2"], [3, "adClient", "adSlot", "display", "adFormat"], [3, "ngModel", "value", "selectionChange", "ngModelChange", "valueChange"], ["value", "1"], ["value", "3"], ["value", "4"], ["value", "5"], [1, "text-danger"], [3, "adClient", "adSlot", "adFormat"], [1, "text-success"], [1, "div-padded-2rem", "row"], [1, "col-12", "col-lg-3"], [1, "div-padded-bottom", "div-padded-right-2"], ["aria-label", "Select panel", 3, "ngModel", "ngModelChange"], ["value", "1", 1, "div-padded-right-2"], ["value", "2", 1, "div-padded-right-2"], ["aria-label", "Select action", 3, "ngModel", "change", "ngModelChange"], [1, "div-padded-right-2"], ["appearance", "fill", 1, "example-full-width"], ["matInput", "", "placeholder", "Enter comment ID...", "numbersOnly", "", 3, "ngModel", "ngModelChange"], [3, "ngModel", "ngModelChange"], [3, "value", 4, "ngFor", "ngForOf"], ["mat-raised-button", "", 1, "bg-info", "text-white", 3, "click"], [3, "value"]], template: function ModeratorComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "div", 0);
        core/* ɵɵelementStart */.TgZ(2, "div", 1);
        core/* ɵɵelementStart */.TgZ(3, "div", 2);
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "span");
        core/* ɵɵtext */._uU(6, "MODERATOR VERIFICATION ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelement */._UZ(7, "hr");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 4);
        core/* ɵɵelementStart */.TgZ(9, "div", 5);
        core/* ɵɵelementStart */.TgZ(10, "div", 6);
        core/* ɵɵelementStart */.TgZ(11, "div", 7);
        core/* ɵɵelement */._UZ(12, "i", 8);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, ModeratorComponent_div_13_Template, 12, 7, "div", 9);
        core/* ɵɵtemplate */.YNc(14, ModeratorComponent_div_14_Template, 18, 6, "div", 9);
        core/* ɵɵtemplate */.YNc(15, ModeratorComponent_div_15_Template, 8, 4, "div", 9);
        core/* ɵɵtemplate */.YNc(16, ModeratorComponent_div_16_Template, 8, 3, "div", 9);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(17, ModeratorComponent_div_17_Template, 41, 5, "div", 10);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(13);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.answer1 < 2);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.answer1 == 2);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.verified == 2);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.verified == 1);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.verified == 1);
    } }, directives: [common/* NgIf */.O5, ng2_adsense/* AdsenseComponent */.nq, MatFormField, MatSelect, fesm2015_core/* MatOption */.ey, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_radio/* MatRadioGroup */.VQ, fesm2015_radio/* MatRadioButton */.U0, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, common/* NgForOf */.sg, fesm2015_button/* MatButton */.lW], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/pages/upcoming-race-analysis/upcoming-race-analysis.component.ts

















const upcoming_race_analysis_component_c0 = ["chart"];
function UpcomingRaceAnalysisComponent_div_13_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "div", 20);
    core/* ɵɵelementStart */.TgZ(2, "div", 21);
    core/* ɵɵtext */._uU(3);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(4, "div", 22);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "div", 23);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const comment_r10 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵtextInterpolate */.Oqu(comment_r10.nickname);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r10.id);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(comment_r10.comment);
} }
function UpcomingRaceAnalysisComponent_ng_template_22_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 25);
    core/* ɵɵelementStart */.TgZ(1, "div");
    core/* ɵɵelementStart */.TgZ(2, "div", 26);
    core/* ɵɵelementStart */.TgZ(3, "h2");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "div", 27);
    core/* ɵɵelementStart */.TgZ(6, "a", 28);
    core/* ɵɵelement */._UZ(7, "img", 29);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r11 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r11.upcomingRaceAnalysis.circuitName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵpropertyInterpolate */.s9C("href", ctx_r11.upcomingRaceAnalysis.circuitUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵpropertyInterpolate */.s9C("src", ctx_r11.upcomingRaceAnalysis.imageUrl, core/* ɵɵsanitizeUrl */.LSH);
} }
function UpcomingRaceAnalysisComponent_ng_template_22_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_ng_template_22_div_0_Template, 8, 3, "div", 24);
} if (rf & 2) {
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r2.upcomingRaceAnalysis !== undefined);
} }
function UpcomingRaceAnalysisComponent_ng_template_24_div_0_div_1_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r17 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r17.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r17.driver);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r17.constructorName);
} }
function UpcomingRaceAnalysisComponent_ng_template_24_div_0_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 34);
    core/* ɵɵelementStart */.TgZ(1, "div", 35);
    core/* ɵɵelementStart */.TgZ(2, "div", 36);
    core/* ɵɵelementStart */.TgZ(3, "table", 37);
    core/* ɵɵelementStart */.TgZ(4, "thead");
    core/* ɵɵelementStart */.TgZ(5, "tr");
    core/* ɵɵelementStart */.TgZ(6, "th", 38);
    core/* ɵɵtext */._uU(7);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(8, "th", 39);
    core/* ɵɵtext */._uU(9);
    core/* ɵɵpipe */.ALo(10, "date");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "tr");
    core/* ɵɵelementStart */.TgZ(12, "th", 40);
    core/* ɵɵtext */._uU(13, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th");
    core/* ɵɵtext */._uU(15, "Driver");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th");
    core/* ɵɵtext */._uU(17, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_ng_template_24_div_0_div_1_tr_19_Template, 7, 3, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const race_r15 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(7);
    core/* ɵɵtextInterpolate2 */.AsE("", race_r15.season, " - round ", race_r15.round, "");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(core/* ɵɵpipeBind1 */.lcZ(10, 4, race_r15.date));
    core/* ɵɵadvance */.xp6(10);
    core/* ɵɵproperty */.Q6J("ngForOf", race_r15.basicResultList);
} }
function UpcomingRaceAnalysisComponent_ng_template_24_div_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 32);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_ng_template_24_div_0_div_1_Template, 20, 6, "div", 33);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r12 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r12.upcomingRaceAnalysis.basicRaces);
} }
function UpcomingRaceAnalysisComponent_ng_template_24_div_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 42);
    core/* ɵɵelementStart */.TgZ(1, "div", 35);
    core/* ɵɵelementStart */.TgZ(2, "div", 36);
    core/* ɵɵtext */._uU(3, " No F1 races on ");
    core/* ɵɵelementStart */.TgZ(4, "a", 43);
    core/* ɵɵtext */._uU(5);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtext */._uU(6, " in the last 10 years. ");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r13 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(4);
    core/* ɵɵpropertyInterpolate */.s9C("href", ctx_r13.upcomingRaceAnalysis.circuitUrl, core/* ɵɵsanitizeUrl */.LSH);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r13.upcomingRaceAnalysis.circuitName);
} }
function UpcomingRaceAnalysisComponent_ng_template_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_ng_template_24_div_0_Template, 2, 1, "div", 30);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_ng_template_24_div_1_Template, 7, 2, "div", 31);
} if (rf & 2) {
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.upcomingRaceAnalysis.basicRaces.length > 0);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.upcomingRaceAnalysis.basicRaces.length == 0);
} }
function UpcomingRaceAnalysisComponent_ng_template_26_div_0_tr_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 58);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "td", 58);
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td", 58);
    core/* ɵɵtext */._uU(16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r22 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.fastestLap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.fastestLapSector1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.fastestLapSector2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r22.fastestLapSector3);
} }
function UpcomingRaceAnalysisComponent_ng_template_26_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r25 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_ng_template_26_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r25); const ctx_r24 = core/* ɵɵnextContext */.oxw(2); return ctx_r24.sortMainResultData($event, 1); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results<");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 49);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 55);
    core/* ɵɵtext */._uU(18, "Sector 1");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 56);
    core/* ɵɵtext */._uU(20, "Sector 2");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 57);
    core/* ɵɵtext */._uU(22, "Sector 3");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "tbody");
    core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_ng_template_26_div_0_tr_24_Template, 17, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r19 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(24);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r19.upcomingRaceAnalysis.fp1);
} }
function UpcomingRaceAnalysisComponent_ng_template_26_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const lap_r26 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r26.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r26.driverName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r26.lapTime);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r26.lapNumber);
} }
function UpcomingRaceAnalysisComponent_ng_template_26_Template(rf, ctx) { if (rf & 1) {
    const _r29 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_ng_template_26_div_0_Template, 25, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 45);
    core/* ɵɵelementStart */.TgZ(2, "div", 46);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_ng_template_26_Template_table_matSortChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r29); const ctx_r28 = core/* ɵɵnextContext */.oxw(); return ctx_r28.sortResultData($event, 1); });
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 48);
    core/* ɵɵtext */._uU(8, "Recorded laps");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tr");
    core/* ɵɵelementStart */.TgZ(10, "th", 49);
    core/* ɵɵtext */._uU(11, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 50);
    core/* ɵɵtext */._uU(13, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th", 49);
    core/* ɵɵtext */._uU(15, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th", 51);
    core/* ɵɵtext */._uU(17, "Lap Number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_ng_template_26_tr_19_Template, 9, 4, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r4.upcomingRaceAnalysis.fp1 !== null);
    core/* ɵɵadvance */.xp6(19);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r4.upcomingRaceAnalysis.fp1Laps);
} }
function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_div_0_tr_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 58);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "td", 58);
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td", 58);
    core/* ɵɵtext */._uU(16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r34 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.fastestLap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.fastestLapSector1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.fastestLapSector2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r34.fastestLapSector3);
} }
function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r37 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r37); const ctx_r36 = core/* ɵɵnextContext */.oxw(3); return ctx_r36.sortMainResultData($event, 2); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 49);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 55);
    core/* ɵɵtext */._uU(18, "Sector 1");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 56);
    core/* ɵɵtext */._uU(20, "Sector 2");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 57);
    core/* ɵɵtext */._uU(22, "Sector 3");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "tbody");
    core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_div_0_tr_24_Template, 17, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r31 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(24);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r31.upcomingRaceAnalysis.fp2);
} }
function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const lap_r38 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r38.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r38.driverName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r38.lapTime);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r38.lapNumber);
} }
function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_Template(rf, ctx) { if (rf & 1) {
    const _r41 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_div_0_Template, 25, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 45);
    core/* ɵɵelementStart */.TgZ(2, "div", 46);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_Template_table_matSortChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r41); const ctx_r40 = core/* ɵɵnextContext */.oxw(2); return ctx_r40.sortResultData($event, 2); });
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 48);
    core/* ɵɵtext */._uU(8, "Recorded laps");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tr");
    core/* ɵɵelementStart */.TgZ(10, "th", 49);
    core/* ɵɵtext */._uU(11, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 50);
    core/* ɵɵtext */._uU(13, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th", 49);
    core/* ɵɵtext */._uU(15, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th", 51);
    core/* ɵɵtext */._uU(17, "Lap Number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_tr_19_Template, 9, 4, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r30 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r30.upcomingRaceAnalysis.fp2 !== null);
    core/* ɵɵadvance */.xp6(19);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r30.upcomingRaceAnalysis.fp2Laps);
} }
function UpcomingRaceAnalysisComponent_mat_tab_27_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-tab", 59);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_mat_tab_27_ng_template_1_Template, 20, 2, "ng-template", 13);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("disabled", ctx_r5.upcomingRaceAnalysis.fp2 == null);
} }
function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_div_0_tr_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 58);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "td", 58);
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td", 58);
    core/* ɵɵtext */._uU(16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r46 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.fastestLap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.fastestLapSector1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.fastestLapSector2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r46.fastestLapSector3);
} }
function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r49 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r49); const ctx_r48 = core/* ɵɵnextContext */.oxw(3); return ctx_r48.sortMainResultData($event, 3); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 49);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 55);
    core/* ɵɵtext */._uU(18, "Sector 1");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 56);
    core/* ɵɵtext */._uU(20, "Sector 2");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 57);
    core/* ɵɵtext */._uU(22, "Sector 3");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "tbody");
    core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_div_0_tr_24_Template, 17, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r43 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(24);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r43.upcomingRaceAnalysis.fp3);
} }
function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const lap_r50 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r50.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r50.driverName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r50.lapTime);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r50.lapNumber);
} }
function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_Template(rf, ctx) { if (rf & 1) {
    const _r53 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_div_0_Template, 25, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 45);
    core/* ɵɵelementStart */.TgZ(2, "div", 46);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_Template_table_matSortChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r53); const ctx_r52 = core/* ɵɵnextContext */.oxw(2); return ctx_r52.sortResultData($event, 3); });
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 48);
    core/* ɵɵtext */._uU(8, "Recorded laps");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tr");
    core/* ɵɵelementStart */.TgZ(10, "th", 49);
    core/* ɵɵtext */._uU(11, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 50);
    core/* ɵɵtext */._uU(13, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th", 49);
    core/* ɵɵtext */._uU(15, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th", 51);
    core/* ɵɵtext */._uU(17, "Lap Number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_tr_19_Template, 9, 4, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r42 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r42.upcomingRaceAnalysis.fp3 !== null);
    core/* ɵɵadvance */.xp6(19);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r42.upcomingRaceAnalysis.fp3Laps);
} }
function UpcomingRaceAnalysisComponent_mat_tab_28_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-tab", 60);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_mat_tab_28_ng_template_1_Template, 20, 2, "ng-template", 13);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r6 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("disabled", ctx_r6.upcomingRaceAnalysis.fp3 == null);
} }
function UpcomingRaceAnalysisComponent_ng_template_30_div_0_tr_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 58);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "td", 58);
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td", 58);
    core/* ɵɵtext */._uU(16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r57 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.fastestLap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.fastestLapSector1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.fastestLapSector2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r57.fastestLapSector3);
} }
function UpcomingRaceAnalysisComponent_ng_template_30_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r60 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_ng_template_30_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r60); const ctx_r59 = core/* ɵɵnextContext */.oxw(2); return ctx_r59.sortMainResultData($event, 4); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 49);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 55);
    core/* ɵɵtext */._uU(18, "Sector 1");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 56);
    core/* ɵɵtext */._uU(20, "Sector 2");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 57);
    core/* ɵɵtext */._uU(22, "Sector 3");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "tbody");
    core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_ng_template_30_div_0_tr_24_Template, 17, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r54 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵadvance */.xp6(24);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r54.upcomingRaceAnalysis.quali);
} }
function UpcomingRaceAnalysisComponent_ng_template_30_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const lap_r61 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r61.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r61.driverName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r61.lapTime);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r61.lapNumber);
} }
function UpcomingRaceAnalysisComponent_ng_template_30_Template(rf, ctx) { if (rf & 1) {
    const _r64 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_ng_template_30_div_0_Template, 25, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 45);
    core/* ɵɵelementStart */.TgZ(2, "div", 46);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_ng_template_30_Template_table_matSortChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r64); const ctx_r63 = core/* ɵɵnextContext */.oxw(); return ctx_r63.sortResultData($event, 4); });
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 48);
    core/* ɵɵtext */._uU(8, "Recorded laps");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tr");
    core/* ɵɵelementStart */.TgZ(10, "th", 49);
    core/* ɵɵtext */._uU(11, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 50);
    core/* ɵɵtext */._uU(13, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th", 49);
    core/* ɵɵtext */._uU(15, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th", 51);
    core/* ɵɵtext */._uU(17, "Lap Number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_ng_template_30_tr_19_Template, 9, 4, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r7 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r7.upcomingRaceAnalysis.quali !== null);
    core/* ɵɵadvance */.xp6(19);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r7.upcomingRaceAnalysis.qualiLaps);
} }
function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_div_0_tr_24_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td");
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 58);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "td", 58);
    core/* ɵɵtext */._uU(14);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "td", 58);
    core/* ɵɵtext */._uU(16);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r69 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.fastestLap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.fastestLapSector1);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.fastestLapSector2);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r69.fastestLapSector3);
} }
function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r72 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r72); const ctx_r71 = core/* ɵɵnextContext */.oxw(3); return ctx_r71.sortMainResultData($event, 2); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 49);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 55);
    core/* ɵɵtext */._uU(18, "Sector 1");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "th", 56);
    core/* ɵɵtext */._uU(20, "Sector 2");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(21, "th", 57);
    core/* ɵɵtext */._uU(22, "Sector 3");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(23, "tbody");
    core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_div_0_tr_24_Template, 17, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r66 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(24);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r66.upcomingRaceAnalysis.fp2);
} }
function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_tr_19_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td");
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const lap_r73 = ctx.$implicit;
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r73.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r73.driverName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r73.lapTime);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(lap_r73.lapNumber);
} }
function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_Template(rf, ctx) { if (rf & 1) {
    const _r76 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_div_0_Template, 25, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 45);
    core/* ɵɵelementStart */.TgZ(2, "div", 46);
    core/* ɵɵelementStart */.TgZ(3, "div", 36);
    core/* ɵɵelementStart */.TgZ(4, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_Template_table_matSortChange_4_listener($event) { core/* ɵɵrestoreView */.CHM(_r76); const ctx_r75 = core/* ɵɵnextContext */.oxw(2); return ctx_r75.sortResultData($event, 2); });
    core/* ɵɵelementStart */.TgZ(5, "thead");
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 48);
    core/* ɵɵtext */._uU(8, "Recorded laps");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "tr");
    core/* ɵɵelementStart */.TgZ(10, "th", 49);
    core/* ɵɵtext */._uU(11, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(12, "th", 50);
    core/* ɵɵtext */._uU(13, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(14, "th", 49);
    core/* ɵɵtext */._uU(15, "Lap Time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(16, "th", 51);
    core/* ɵɵtext */._uU(17, "Lap Number");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(18, "tbody");
    core/* ɵɵtemplate */.YNc(19, UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_tr_19_Template, 9, 4, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r65 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r65.upcomingRaceAnalysis.fp2 !== null);
    core/* ɵɵadvance */.xp6(19);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r65.upcomingRaceAnalysis.fp2Laps);
} }
function UpcomingRaceAnalysisComponent_mat_tab_31_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-tab", 59);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_mat_tab_31_ng_template_1_Template, 20, 2, "ng-template", 13);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r8 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("disabled", ctx_r8.upcomingRaceAnalysis.fp2 == null);
} }
function UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_div_0_tr_20_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "tr");
    core/* ɵɵelementStart */.TgZ(1, "th", 41);
    core/* ɵɵtext */._uU(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(3, "td");
    core/* ɵɵtext */._uU(4);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(5, "td", 58);
    core/* ɵɵtext */._uU(6);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(7, "td");
    core/* ɵɵtext */._uU(8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "td", 58);
    core/* ɵɵtext */._uU(10);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "td", 65);
    core/* ɵɵtext */._uU(12);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const standing_r80 = ctx.$implicit;
    const ctx_r79 = core/* ɵɵnextContext */.oxw(4);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r80.position);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r80.FullName);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r80.Team);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r80.finalGap);
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵtextInterpolate */.Oqu(standing_r80.fastestLap);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵstyleProp */.Udp("color", ctx_r79.getTyresClass(standing_r80.lapByLapData.tyres));
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵtextInterpolate */.Oqu(ctx_r79.getTyresString(standing_r80.lapByLapData.tyres));
} }
function UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r83 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "div");
    core/* ɵɵelementStart */.TgZ(1, "table", 47);
    core/* ɵɵlistener */.NdJ("matSortChange", function UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_div_0_Template_table_matSortChange_1_listener($event) { core/* ɵɵrestoreView */.CHM(_r83); const ctx_r82 = core/* ɵɵnextContext */.oxw(3); return ctx_r82.sortMainResultData($event, 5); });
    core/* ɵɵelementStart */.TgZ(2, "thead");
    core/* ɵɵelementStart */.TgZ(3, "tr");
    core/* ɵɵelementStart */.TgZ(4, "th", 52);
    core/* ɵɵtext */._uU(5, "Results");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(6, "tr");
    core/* ɵɵelementStart */.TgZ(7, "th", 49);
    core/* ɵɵtext */._uU(8, "#");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(9, "th", 53);
    core/* ɵɵtext */._uU(10, "Name");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(11, "th", 54);
    core/* ɵɵtext */._uU(12, "Team");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(13, "th", 53);
    core/* ɵɵtext */._uU(14, "Gap");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(15, "th", 63);
    core/* ɵɵtext */._uU(16, "Lap time");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(17, "th", 64);
    core/* ɵɵtext */._uU(18, "Tyre");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementStart */.TgZ(19, "tbody");
    core/* ɵɵtemplate */.YNc(20, UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_div_0_tr_20_Template, 13, 8, "tr", 8);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r78 = core/* ɵɵnextContext */.oxw(3);
    core/* ɵɵadvance */.xp6(20);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r78.upcomingRaceAnalysis.sprintQuali);
} }
function UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵtemplate */.YNc(0, UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_div_0_Template, 21, 1, "div", 44);
    core/* ɵɵelementStart */.TgZ(1, "div", 3);
    core/* ɵɵelementStart */.TgZ(2, "div", 4);
    core/* ɵɵelementStart */.TgZ(3, "div", 35);
    core/* ɵɵelementStart */.TgZ(4, "div", 36);
    core/* ɵɵelement */._UZ(5, "apx-chart", 62);
    core/* ɵɵelement */._UZ(6, "br");
    core/* ɵɵelement */._UZ(7, "br");
    core/* ɵɵelement */._UZ(8, "br");
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r77 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r77.upcomingRaceAnalysis.sprintQuali != null);
    core/* ɵɵadvance */.xp6(5);
    core/* ɵɵproperty */.Q6J("series", ctx_r77.lapPosChart.series)("chart", ctx_r77.defChart)("dataLabels", ctx_r77.defDataLabels)("grid", ctx_r77.defGrid)("stroke", ctx_r77.defStroke)("title", ctx_r77.lapPosChart.title)("yaxis", ctx_r77.reverseYAxis)("xaxis", ctx_r77.lapXaxis);
} }
function UpcomingRaceAnalysisComponent_mat_tab_32_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-tab", 61);
    core/* ɵɵtemplate */.YNc(1, UpcomingRaceAnalysisComponent_mat_tab_32_ng_template_1_Template, 9, 9, "ng-template", 13);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r9 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("disabled", ctx_r9.upcomingRaceAnalysis.sprintQuali == null);
} }
class UpcomingRaceAnalysisComponent {
    constructor(restService, utilityService, ref) {
        this.restService = restService;
        this.utilityService = utilityService;
        this.ref = ref;
        this.sprintMode = true;
        this.defChart = {
            height: 350,
            type: 'line',
            zoom: {
                enabled: false
            },
            toolbar: ChartUtility.defaultToolbar
        };
        this.defDataLabels = {
            enabled: false
        };
        this.defStroke = {
            curve: 'straight'
        };
        this.defGrid = {
            borderColor: '#e7e7e7',
            row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            }
        };
        this.reverseYAxis = {
            min: 1,
            // tickAmount:6 ,
            decimalsInFloat: 0,
            reversed: true,
            axisTicks: {
                show: true
            },
            axisBorder: {
                show: true,
                color: '#008FFB'
            },
            title: {
                text: 'Position',
            }
        };
        this.lapXaxis = {
            labels: {
                formatter: function (value, timestamp, opts) {
                    var y = +value - 1;
                    return '' + y;
                }
            },
            title: {
                text: 'Lap',
            }
        };
        const observable = restService.getUpcomingRaceAnalysis();
        observable.subscribe({
            next: data => {
                this.upcomingRaceAnalysis = data;
                this.utilityService.setTitle(this.upcomingRaceAnalysis.title);
                console.log(this.sprintMode);
                console.log(this.upcomingRaceAnalysis.sprintQuali);
                this.setCharts();
                this.checkIfSprintMode();
                return data;
            }
        });
        this.utilityService.reloadPosts(11);
    }
    sortMainResultData(sort, eventNumber) {
        switch (eventNumber) {
            case 1:
                this.upcomingRaceAnalysis.fp1 = this.sortResults(this.upcomingRaceAnalysis.fp1, sort.active, sort.direction);
                break;
            case 2:
                this.upcomingRaceAnalysis.fp2 = this.sortResults(this.upcomingRaceAnalysis.fp2, sort.active, sort.direction);
                break;
            case 3:
                this.upcomingRaceAnalysis.fp3 = this.sortResults(this.upcomingRaceAnalysis.fp3, sort.active, sort.direction);
                break;
            case 4:
                this.upcomingRaceAnalysis.quali = this.sortResults(this.upcomingRaceAnalysis.quali, sort.active, sort.direction);
                break;
            case 5:
                this.upcomingRaceAnalysis.sprintQuali = this.sortResults(this.upcomingRaceAnalysis.sprintQuali, sort.active, sort.direction);
                break;
        }
    }
    sortResultData(sort, eventNumber) {
        switch (eventNumber) {
            case 1:
                this.upcomingRaceAnalysis.fp1Laps = this.sortLaps(this.upcomingRaceAnalysis.fp1Laps, sort.active, sort.direction);
                break;
            case 2:
                this.upcomingRaceAnalysis.fp2Laps = this.sortLaps(this.upcomingRaceAnalysis.fp2Laps, sort.active, sort.direction);
                break;
            case 3:
                this.upcomingRaceAnalysis.fp3Laps = this.sortLaps(this.upcomingRaceAnalysis.fp3Laps, sort.active, sort.direction);
                break;
            case 4:
                this.upcomingRaceAnalysis.qualiLaps = this.sortLaps(this.upcomingRaceAnalysis.qualiLaps, sort.active, sort.direction);
                break;
            case 5:
                this.upcomingRaceAnalysis.sprintQualiLaps = this.sortLaps(this.upcomingRaceAnalysis.sprintQualiLaps, sort.active, sort.direction);
                break;
        }
    }
    sortLaps(laps, sortingColumn, direction) {
        let sorted = [];
        if (direction == 'asc') {
            switch (sortingColumn) {
                case 'name':
                    sorted = laps.sort((a, b) => (a.driverName >= b.driverName) ? 1 : -1);
                    return sorted;
                case 'position':
                    sorted = laps.sort((a, b) => (a.lapTimeMs > b.lapTimeMs) ? 1 : -1);
                    return sorted;
                case 'lapNumber':
                    sorted = laps.sort((a, b) => (a.lapNumber > b.lapNumber) ? 1 : -1);
                    return sorted;
            }
        }
        if (direction == 'desc') {
            switch (sortingColumn) {
                case 'name':
                    sorted = laps.sort((a, b) => (a.driverName <= b.driverName) ? 1 : -1);
                    return sorted;
                case 'position':
                    sorted = laps.sort((a, b) => (a.lapTimeMs < b.lapTimeMs) ? 1 : -1);
                    return sorted;
                case 'lapNumber':
                    sorted = laps.sort((a, b) => (a.lapNumber < b.lapNumber) ? 1 : -1);
                    return sorted;
            }
        }
        if (direction == '') {
            sorted = laps.sort((a, b) => (a.lapTimeMs > b.lapTimeMs) ? 1 : -1);
            return sorted;
        }
        return sorted;
    }
    sortResults(drivers, sortingColumn, direction) {
        let sorted = [];
        if (direction == 'asc') {
            switch (sortingColumn) {
                case 'position':
                    sorted = drivers.sort((a, b) => (a.position > b.position) ? 1 : -1);
                    return sorted;
                case 'sector1':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector1 > b.fastestLapSector1) ? 1 : -1);
                    return sorted;
                case 'sector2':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector2 > b.fastestLapSector2) ? 1 : -1);
                    return sorted;
                case 'sector3':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector3 > b.fastestLapSector3) ? 1 : -1);
                    return sorted;
                case 'lapTime':
                    sorted = drivers.sort((a, b) => (a.fastestLapPosition > b.fastestLapPosition) ? 1 : -1);
                    return sorted;
            }
        }
        if (direction == 'desc') {
            switch (sortingColumn) {
                case 'position':
                    sorted = drivers.sort((a, b) => (a.position < b.position) ? 1 : -1);
                    return sorted;
                case 'sector1':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector1 < b.fastestLapSector1) ? 1 : -1);
                    return sorted;
                case 'sector2':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector2 < b.fastestLapSector2) ? 1 : -1);
                    return sorted;
                case 'sector3':
                    sorted = drivers.sort((a, b) => (a.fastestLapSector3 < b.fastestLapSector3) ? 1 : -1);
                    return sorted;
                case 'lapTime':
                    sorted = drivers.sort((a, b) => (a.fastestLapPosition < b.fastestLapPosition) ? 1 : -1);
                    return sorted;
            }
        }
        if (direction == '') {
            sorted = drivers.sort((a, b) => (a.position < b.position) ? 1 : -1);
            return sorted;
        }
        return sorted;
    }
    checkIfSprintMode() {
        if (this.upcomingRaceAnalysis.sprintQuali != null) {
            this.sprintMode = true;
        }
        else if ((this.upcomingRaceAnalysis.fp2 == null || this.upcomingRaceAnalysis.fp3 == null) && this.upcomingRaceAnalysis.quali != null) {
            this.sprintMode = true;
        }
        else {
            this.sprintMode = false;
        }
        // this.ref.detectChanges();
    }
    setCharts() {
        if (this.upcomingRaceAnalysis.sprintQuali == null) {
            return;
        }
        let positionsByLapchartSeries = [];
        for (let driver of this.upcomingRaceAnalysis.sprintQuali) {
            let color = driver.Color;
            if (color == 'FFFFFF') {
                color = '888888';
            }
            positionsByLapchartSeries.push({
                name: driver.Initials,
                data: driver.lapByLapData.positions,
                color: '#' + color
            });
        }
        this.lapPosChart = {
            series: positionsByLapchartSeries,
            title: {
                text: 'Position by lap',
                align: 'center'
            },
        };
    }
    getTyresString(tyres) {
        let response = "";
        tyres.forEach((tyre) => {
            response = response + tyre.type;
        });
        return response;
    }
    getTyresClass(tyres) {
        return RaceAnalysisComponent.getColor(this.getTyresString(tyres));
    }
}
UpcomingRaceAnalysisComponent.ɵfac = function UpcomingRaceAnalysisComponent_Factory(t) { return new (t || UpcomingRaceAnalysisComponent)(core/* ɵɵdirectiveInject */.Y36(rest_service/* RestService */.v), core/* ɵɵdirectiveInject */.Y36(utility_service/* UtilityService */.t), core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO)); };
UpcomingRaceAnalysisComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: UpcomingRaceAnalysisComponent, selectors: [["upcoming-race-analysis-cmp"]], viewQuery: function UpcomingRaceAnalysisComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(upcoming_race_analysis_component_c0, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.chart = _t.first);
    } }, decls: 33, vars: 10, consts: [["autosize", ""], ["autosize", "", "mode", "side", "position", "end", 1, "example-sidenav", 3, "opened", "openedChange"], ["drawer", ""], [1, "row"], [1, "col-md-12"], ["matInput", "", "placeholder", "Leave a comment", 1, "chat-text-area", 3, "ngModel", "ngModelChange"], [1, "post-comment"], ["mat-flat-button", "", 1, "post-comment-button", "bg-success", "text-light", 3, "click"], [4, "ngFor", "ngForOf"], [1, "fixed-button"], ["type", "fixed-plugin", "mat-button", "", 3, "click"], [1, "fa", "fa-commenting-o", "fa-2x"], ["label", "Circuit"], ["matTabContent", ""], ["label", "Circuit winners", 3, "disabled"], ["label", "Practice 1", 3, "disabled"], ["label", "Practice 2", 3, "disabled", 4, "ngIf"], ["label", "Practice 3", 3, "disabled", 4, "ngIf"], ["label", "Qualifying", 3, "disabled"], ["label", "Sprint Qualifying", 3, "disabled", 4, "ngIf"], [1, "basic-comment"], [1, "one"], [1, "two"], [1, "basic-comment-body"], ["class", "div-padded-always-left-right-1 row", 4, "ngIf"], [1, "div-padded-always-left-right-1", "row"], [1, "card-body", "text-center"], [1, "image"], ["target", "_blank", 3, "href"], [3, "src"], ["class", "div-padded-1rem row", 4, "ngIf"], ["class", "div-padded-1rem div-padded-left-2 row", 4, "ngIf"], [1, "div-padded-1rem", "row"], ["class", "col-lg-6 col-12", 4, "ngFor", "ngForOf"], [1, "col-lg-6", "col-12"], [1, "card"], [1, "card-body-padded"], [1, "table", "table-striped"], ["colspan", "2", "scope", "col"], [1, "float-right"], [2, "width", "35px"], ["scope", "row"], [1, "div-padded-1rem", "div-padded-left-2", "row"], [3, "href"], [4, "ngIf"], [1, "div-padded-1rem", "div-padded-left-2", "div-padded-right-2", "row"], [1, "card", "col-lg-12", "col-12"], ["matSort", "", 1, "table", "table-striped", 3, "matSortChange"], ["colspan", "4", 1, "text-center"], ["mat-sort-header", "position", "scope", "col"], ["mat-sort-header", "name", "scope", "col"], ["mat-sort-header", "lapNumber", "scope", "col"], ["colspan", "8", 1, "text-center"], ["scope", "col"], ["scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["mat-sort-header", "sector1", "scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["mat-sort-header", "sector2", "scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["mat-sort-header", "sector3", "scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], [1, "d-sm-none", "d-none", "d-lg-table-cell"], ["label", "Practice 2", 3, "disabled"], ["label", "Practice 3", 3, "disabled"], ["label", "Sprint Qualifying", 3, "disabled"], [3, "series", "chart", "dataLabels", "grid", "stroke", "title", "yaxis", "xaxis"], ["mat-sort-header", "lapTime", "scope", "col", 1, "d-sm-none", "d-none", "d-lg-table-cell"], ["scope", "col", 1, "text-center"], [1, "font-weight-bold", "text-center"]], template: function UpcomingRaceAnalysisComponent_Template(rf, ctx) { if (rf & 1) {
        const _r84 = core/* ɵɵgetCurrentView */.EpF();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵelementStart */.TgZ(1, "mat-drawer-container", 0);
        core/* ɵɵelementStart */.TgZ(2, "mat-drawer", 1, 2);
        core/* ɵɵlistener */.NdJ("openedChange", function UpcomingRaceAnalysisComponent_Template_mat_drawer_openedChange_2_listener($event) { return ctx.utilityService.setSidebarStatus($event); });
        core/* ɵɵelementStart */.TgZ(4, "div", 3);
        core/* ɵɵelementStart */.TgZ(5, "div", 4);
        core/* ɵɵelementStart */.TgZ(6, "div");
        core/* ɵɵelementStart */.TgZ(7, "textarea", 5);
        core/* ɵɵlistener */.NdJ("ngModelChange", function UpcomingRaceAnalysisComponent_Template_textarea_ngModelChange_7_listener($event) { return ctx.utilityService.postText = $event; });
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(8, "div", 6);
        core/* ɵɵelementStart */.TgZ(9, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function UpcomingRaceAnalysisComponent_Template_button_click_9_listener() { return ctx.utilityService.postComment(11); });
        core/* ɵɵtext */._uU(10, "Post ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(11, "button", 7);
        core/* ɵɵlistener */.NdJ("click", function UpcomingRaceAnalysisComponent_Template_button_click_11_listener() { return ctx.utilityService.reloadPosts(11); });
        core/* ɵɵtext */._uU(12, "Reload ");
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(13, UpcomingRaceAnalysisComponent_div_13_Template, 8, 3, "div", 8);
        core/* ɵɵelementStart */.TgZ(14, "div", 9);
        core/* ɵɵelementStart */.TgZ(15, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function UpcomingRaceAnalysisComponent_Template_button_click_15_listener() { core/* ɵɵrestoreView */.CHM(_r84); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(16, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(17, "div", 9);
        core/* ɵɵelementStart */.TgZ(18, "button", 10);
        core/* ɵɵlistener */.NdJ("click", function UpcomingRaceAnalysisComponent_Template_button_click_18_listener() { core/* ɵɵrestoreView */.CHM(_r84); const _r0 = core/* ɵɵreference */.MAs(3); return _r0.toggle(); });
        core/* ɵɵelement */._UZ(19, "i", 11);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(20, "mat-tab-group");
        core/* ɵɵelementStart */.TgZ(21, "mat-tab", 12);
        core/* ɵɵtemplate */.YNc(22, UpcomingRaceAnalysisComponent_ng_template_22_Template, 1, 1, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(23, "mat-tab", 14);
        core/* ɵɵtemplate */.YNc(24, UpcomingRaceAnalysisComponent_ng_template_24_Template, 2, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementStart */.TgZ(25, "mat-tab", 15);
        core/* ɵɵtemplate */.YNc(26, UpcomingRaceAnalysisComponent_ng_template_26_Template, 20, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(27, UpcomingRaceAnalysisComponent_mat_tab_27_Template, 2, 1, "mat-tab", 16);
        core/* ɵɵtemplate */.YNc(28, UpcomingRaceAnalysisComponent_mat_tab_28_Template, 2, 1, "mat-tab", 17);
        core/* ɵɵelementStart */.TgZ(29, "mat-tab", 18);
        core/* ɵɵtemplate */.YNc(30, UpcomingRaceAnalysisComponent_ng_template_30_Template, 20, 2, "ng-template", 13);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(31, UpcomingRaceAnalysisComponent_mat_tab_31_Template, 2, 1, "mat-tab", 16);
        core/* ɵɵtemplate */.YNc(32, UpcomingRaceAnalysisComponent_mat_tab_32_Template, 2, 1, "mat-tab", 19);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵpropertyInterpolate */.s9C("opened", ctx.utilityService.getSidebarStatus());
        core/* ɵɵadvance */.xp6(5);
        core/* ɵɵproperty */.Q6J("ngModel", ctx.utilityService.postText);
        core/* ɵɵadvance */.xp6(6);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.utilityService.comments);
        core/* ɵɵadvance */.xp6(10);
        core/* ɵɵproperty */.Q6J("disabled", ctx.upcomingRaceAnalysis.basicRaces.length == 0);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵproperty */.Q6J("disabled", ctx.upcomingRaceAnalysis.fp1 == null);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵproperty */.Q6J("ngIf", !ctx.sprintMode);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", !ctx.sprintMode);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("disabled", ctx.upcomingRaceAnalysis.quali == null);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.sprintMode);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", ctx.sprintMode);
    } }, directives: [sidenav/* MatDrawerContainer */.kh, sidenav/* MatDrawer */.jA, MatInput, fesm2015_forms/* DefaultValueAccessor */.Fj, fesm2015_forms/* NgControlStatus */.JJ, fesm2015_forms/* NgModel */.On, fesm2015_button/* MatButton */.lW, common/* NgForOf */.sg, tabs/* MatTabGroup */.SP, tabs/* MatTab */.uX, tabs/* MatTabContent */.Vc, common/* NgIf */.O5, MatSort, MatSortHeader, ng_apexcharts/* ChartComponent */.x], pipes: [common/* DatePipe */.uU], encapsulation: 2 });

;// CONCATENATED MODULE: ./src/app/layouts/admin-layout/admin-layout.routing.ts
















const AdminLayoutRoutes = [
    { path: 'countdown', component: F1CountdownComponent },
    { path: 'exposure', component: ExposureComponent },
    { path: 'exposed', component: ExposedComponent },
    { path: 'championship', component: StandingsComponent },
    { path: 'contact-info', component: ContactInfoComponent },
    { path: 'videostreams', component: VideostreamsComponent },
    { path: 'poweredByAws', component: PoweredByAwsComponent },
    { path: 'aws', component: AwsComponent },
    { path: 'carousel', component: NgbdCarouselConfig },
    { path: 'upcomingRaceAnalysis', component: UpcomingRaceAnalysisComponent },
    { path: 'raceAnalysis', component: RaceAnalysisComponent },
    { path: 'instagramFeed', component: InstagramFeedComponent },
    { path: 'moderator', component: ModeratorComponent },
    { path: 'typography', component: TypographyComponent },
    { path: 'icons', component: IconsComponent },
    { path: 'notifications', component: NotificationsComponent }
];

;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/grid-list.js





/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Class for determining, from a list of tiles, the (row, col) position of each of those tiles
 * in the grid. This is necessary (rather than just rendering the tiles in normal document flow)
 * because the tiles can have a rowspan.
 *
 * The positioning algorithm greedily places each tile as soon as it encounters a gap in the grid
 * large enough to accommodate it so that the tiles still render in the same order in which they
 * are given.
 *
 * The basis of the algorithm is the use of an array to track the already placed tiles. Each
 * element of the array corresponds to a column, and the value indicates how many cells in that
 * column are already occupied; zero indicates an empty cell. Moving "down" to the next row
 * decrements each value in the tracking array (indicating that the column is one cell closer to
 * being free).
 *
 * @docs-private
 */



const grid_list_c0 = ["*"];
const grid_list_c1 = [[["", "mat-grid-avatar", ""], ["", "matGridAvatar", ""]], [["", "mat-line", ""], ["", "matLine", ""]], "*"];
const grid_list_c2 = ["[mat-grid-avatar], [matGridAvatar]", "[mat-line], [matLine]", "*"];
const grid_list_c3 = ".mat-grid-list{display:block;position:relative}.mat-grid-tile{display:block;position:absolute;overflow:hidden}.mat-grid-tile .mat-grid-tile-header,.mat-grid-tile .mat-grid-tile-footer{display:flex;align-items:center;height:48px;color:#fff;background:rgba(0,0,0,.38);overflow:hidden;padding:0 16px;position:absolute;left:0;right:0}.mat-grid-tile .mat-grid-tile-header>*,.mat-grid-tile .mat-grid-tile-footer>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-tile-header.mat-2-line,.mat-grid-tile .mat-grid-tile-footer.mat-2-line{height:68px}.mat-grid-tile .mat-grid-list-text{display:flex;flex-direction:column;flex:auto;box-sizing:border-box;overflow:hidden}.mat-grid-tile .mat-grid-list-text>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-list-text:empty{display:none}.mat-grid-tile .mat-grid-tile-header{top:0}.mat-grid-tile .mat-grid-tile-footer{bottom:0}.mat-grid-tile .mat-grid-avatar{padding-right:16px}[dir=rtl] .mat-grid-tile .mat-grid-avatar{padding-right:0;padding-left:16px}.mat-grid-tile .mat-grid-avatar:empty{display:none}.mat-grid-tile-content{top:0;left:0;right:0;bottom:0;position:absolute;display:flex;align-items:center;justify-content:center;height:100%;padding:0;margin:0}\n";
class TileCoordinator {
    constructor() {
        /** Index at which the search for the next gap will start. */
        this.columnIndex = 0;
        /** The current row index. */
        this.rowIndex = 0;
    }
    /** Gets the total number of rows occupied by tiles */
    get rowCount() { return this.rowIndex + 1; }
    /**
     * Gets the total span of rows occupied by tiles.
     * Ex: A list with 1 row that contains a tile with rowspan 2 will have a total rowspan of 2.
     */
    get rowspan() {
        const lastRowMax = Math.max(...this.tracker);
        // if any of the tiles has a rowspan that pushes it beyond the total row count,
        // add the difference to the rowcount
        return lastRowMax > 1 ? this.rowCount + lastRowMax - 1 : this.rowCount;
    }
    /**
     * Updates the tile positions.
     * @param numColumns Amount of columns in the grid.
     * @param tiles Tiles to be positioned.
     */
    update(numColumns, tiles) {
        this.columnIndex = 0;
        this.rowIndex = 0;
        this.tracker = new Array(numColumns);
        this.tracker.fill(0, 0, this.tracker.length);
        this.positions = tiles.map(tile => this._trackTile(tile));
    }
    /** Calculates the row and col position of a tile. */
    _trackTile(tile) {
        // Find a gap large enough for this tile.
        const gapStartIndex = this._findMatchingGap(tile.colspan);
        // Place tile in the resulting gap.
        this._markTilePosition(gapStartIndex, tile);
        // The next time we look for a gap, the search will start at columnIndex, which should be
        // immediately after the tile that has just been placed.
        this.columnIndex = gapStartIndex + tile.colspan;
        return new TilePosition(this.rowIndex, gapStartIndex);
    }
    /** Finds the next available space large enough to fit the tile. */
    _findMatchingGap(tileCols) {
        if (tileCols > this.tracker.length && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw Error(`mat-grid-list: tile with colspan ${tileCols} is wider than ` +
                `grid with cols="${this.tracker.length}".`);
        }
        // Start index is inclusive, end index is exclusive.
        let gapStartIndex = -1;
        let gapEndIndex = -1;
        // Look for a gap large enough to fit the given tile. Empty spaces are marked with a zero.
        do {
            // If we've reached the end of the row, go to the next row.
            if (this.columnIndex + tileCols > this.tracker.length) {
                this._nextRow();
                gapStartIndex = this.tracker.indexOf(0, this.columnIndex);
                gapEndIndex = this._findGapEndIndex(gapStartIndex);
                continue;
            }
            gapStartIndex = this.tracker.indexOf(0, this.columnIndex);
            // If there are no more empty spaces in this row at all, move on to the next row.
            if (gapStartIndex == -1) {
                this._nextRow();
                gapStartIndex = this.tracker.indexOf(0, this.columnIndex);
                gapEndIndex = this._findGapEndIndex(gapStartIndex);
                continue;
            }
            gapEndIndex = this._findGapEndIndex(gapStartIndex);
            // If a gap large enough isn't found, we want to start looking immediately after the current
            // gap on the next iteration.
            this.columnIndex = gapStartIndex + 1;
            // Continue iterating until we find a gap wide enough for this tile. Since gapEndIndex is
            // exclusive, gapEndIndex is 0 means we didn't find a gap and should continue.
        } while ((gapEndIndex - gapStartIndex < tileCols) || (gapEndIndex == 0));
        // If we still didn't manage to find a gap, ensure that the index is
        // at least zero so the tile doesn't get pulled out of the grid.
        return Math.max(gapStartIndex, 0);
    }
    /** Move "down" to the next row. */
    _nextRow() {
        this.columnIndex = 0;
        this.rowIndex++;
        // Decrement all spaces by one to reflect moving down one row.
        for (let i = 0; i < this.tracker.length; i++) {
            this.tracker[i] = Math.max(0, this.tracker[i] - 1);
        }
    }
    /**
     * Finds the end index (exclusive) of a gap given the index from which to start looking.
     * The gap ends when a non-zero value is found.
     */
    _findGapEndIndex(gapStartIndex) {
        for (let i = gapStartIndex + 1; i < this.tracker.length; i++) {
            if (this.tracker[i] != 0) {
                return i;
            }
        }
        // The gap ends with the end of the row.
        return this.tracker.length;
    }
    /** Update the tile tracker to account for the given tile in the given space. */
    _markTilePosition(start, tile) {
        for (let i = 0; i < tile.colspan; i++) {
            this.tracker[start + i] = tile.rowspan;
        }
    }
}
/**
 * Simple data structure for tile position (row, col).
 * @docs-private
 */
class TilePosition {
    constructor(row, col) {
        this.row = row;
        this.col = col;
    }
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Injection token used to provide a grid list to a tile and to avoid circular imports.
 * @docs-private
 */
const MAT_GRID_LIST = new core/* InjectionToken */.OlP('MAT_GRID_LIST');

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatGridTile {
    constructor(_element, _gridList) {
        this._element = _element;
        this._gridList = _gridList;
        this._rowspan = 1;
        this._colspan = 1;
    }
    /** Amount of rows that the grid tile takes up. */
    get rowspan() { return this._rowspan; }
    set rowspan(value) { this._rowspan = Math.round((0,coercion/* coerceNumberProperty */.su)(value)); }
    /** Amount of columns that the grid tile takes up. */
    get colspan() { return this._colspan; }
    set colspan(value) { this._colspan = Math.round((0,coercion/* coerceNumberProperty */.su)(value)); }
    /**
     * Sets the style of the grid-tile element.  Needs to be set manually to avoid
     * "Changed after checked" errors that would occur with HostBinding.
     */
    _setStyle(property, value) {
        this._element.nativeElement.style[property] = value;
    }
}
MatGridTile.ɵfac = function MatGridTile_Factory(t) { return new (t || MatGridTile)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(MAT_GRID_LIST, 8)); };
MatGridTile.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatGridTile, selectors: [["mat-grid-tile"]], hostAttrs: [1, "mat-grid-tile"], hostVars: 2, hostBindings: function MatGridTile_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵattribute */.uIk("rowspan", ctx.rowspan)("colspan", ctx.colspan);
    } }, inputs: { rowspan: "rowspan", colspan: "colspan" }, exportAs: ["matGridTile"], ngContentSelectors: grid_list_c0, decls: 2, vars: 0, consts: [[1, "mat-grid-tile-content"]], template: function MatGridTile_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t();
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵprojection */.Hsn(1);
        core/* ɵɵelementEnd */.qZA();
    } }, styles: [grid_list_c3], encapsulation: 2, changeDetection: 0 });
MatGridTile.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_GRID_LIST,] }] }
];
MatGridTile.propDecorators = {
    rowspan: [{ type: core/* Input */.IIB }],
    colspan: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridTile, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-grid-tile',
                exportAs: 'matGridTile',
                host: {
                    'class': 'mat-grid-tile',
                    // Ensures that the "rowspan" and "colspan" input value is reflected in
                    // the DOM. This is needed for the grid-tile harness.
                    '[attr.rowspan]': 'rowspan',
                    '[attr.colspan]': 'colspan'
                },
                template: "<div class=\"mat-grid-tile-content\">\n  <ng-content></ng-content>\n</div>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                styles: [".mat-grid-list{display:block;position:relative}.mat-grid-tile{display:block;position:absolute;overflow:hidden}.mat-grid-tile .mat-grid-tile-header,.mat-grid-tile .mat-grid-tile-footer{display:flex;align-items:center;height:48px;color:#fff;background:rgba(0,0,0,.38);overflow:hidden;padding:0 16px;position:absolute;left:0;right:0}.mat-grid-tile .mat-grid-tile-header>*,.mat-grid-tile .mat-grid-tile-footer>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-tile-header.mat-2-line,.mat-grid-tile .mat-grid-tile-footer.mat-2-line{height:68px}.mat-grid-tile .mat-grid-list-text{display:flex;flex-direction:column;flex:auto;box-sizing:border-box;overflow:hidden}.mat-grid-tile .mat-grid-list-text>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-list-text:empty{display:none}.mat-grid-tile .mat-grid-tile-header{top:0}.mat-grid-tile .mat-grid-tile-footer{bottom:0}.mat-grid-tile .mat-grid-avatar{padding-right:16px}[dir=rtl] .mat-grid-tile .mat-grid-avatar{padding-right:0;padding-left:16px}.mat-grid-tile .mat-grid-avatar:empty{display:none}.mat-grid-tile-content{top:0;left:0;right:0;bottom:0;position:absolute;display:flex;align-items:center;justify-content:center;height:100%;padding:0;margin:0}\n"]
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_GRID_LIST]
            }] }]; }, { rowspan: [{
            type: core/* Input */.IIB
        }], colspan: [{
            type: core/* Input */.IIB
        }] }); })();
class MatGridTileText {
    constructor(_element) {
        this._element = _element;
    }
    ngAfterContentInit() {
        (0,fesm2015_core/* setLines */.E0)(this._lines, this._element);
    }
}
MatGridTileText.ɵfac = function MatGridTileText_Factory(t) { return new (t || MatGridTileText)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq)); };
MatGridTileText.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatGridTileText, selectors: [["mat-grid-tile-header"], ["mat-grid-tile-footer"]], contentQueries: function MatGridTileText_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, fesm2015_core/* MatLine */.X2, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._lines = _t);
    } }, ngContentSelectors: grid_list_c2, decls: 4, vars: 0, consts: [[1, "mat-grid-list-text"]], template: function MatGridTileText_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t(grid_list_c1);
        core/* ɵɵprojection */.Hsn(0);
        core/* ɵɵelementStart */.TgZ(1, "div", 0);
        core/* ɵɵprojection */.Hsn(2, 1);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵprojection */.Hsn(3, 2);
    } }, encapsulation: 2, changeDetection: 0 });
MatGridTileText.ctorParameters = () => [
    { type: core/* ElementRef */.SBq }
];
MatGridTileText.propDecorators = {
    _lines: [{ type: core/* ContentChildren */.AcB, args: [fesm2015_core/* MatLine */.X2, { descendants: true },] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridTileText, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-grid-tile-header, mat-grid-tile-footer',
                template: "<ng-content select=\"[mat-grid-avatar], [matGridAvatar]\"></ng-content>\n<div class=\"mat-grid-list-text\"><ng-content select=\"[mat-line], [matLine]\"></ng-content></div>\n<ng-content></ng-content>\n",
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                encapsulation: core/* ViewEncapsulation.None */.ifc.None
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }]; }, { _lines: [{
            type: core/* ContentChildren */.AcB,
            args: [fesm2015_core/* MatLine */.X2, { descendants: true }]
        }] }); })();
/**
 * Directive whose purpose is to add the mat- CSS styling to this selector.
 * @docs-private
 */
class MatGridAvatarCssMatStyler {
}
MatGridAvatarCssMatStyler.ɵfac = function MatGridAvatarCssMatStyler_Factory(t) { return new (t || MatGridAvatarCssMatStyler)(); };
MatGridAvatarCssMatStyler.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatGridAvatarCssMatStyler, selectors: [["", "mat-grid-avatar", ""], ["", "matGridAvatar", ""]], hostAttrs: [1, "mat-grid-avatar"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridAvatarCssMatStyler, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[mat-grid-avatar], [matGridAvatar]',
                host: { 'class': 'mat-grid-avatar' }
            }]
    }], null, null); })();
/**
 * Directive whose purpose is to add the mat- CSS styling to this selector.
 * @docs-private
 */
class MatGridTileHeaderCssMatStyler {
}
MatGridTileHeaderCssMatStyler.ɵfac = function MatGridTileHeaderCssMatStyler_Factory(t) { return new (t || MatGridTileHeaderCssMatStyler)(); };
MatGridTileHeaderCssMatStyler.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatGridTileHeaderCssMatStyler, selectors: [["mat-grid-tile-header"]], hostAttrs: [1, "mat-grid-tile-header"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridTileHeaderCssMatStyler, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-grid-tile-header',
                host: { 'class': 'mat-grid-tile-header' }
            }]
    }], null, null); })();
/**
 * Directive whose purpose is to add the mat- CSS styling to this selector.
 * @docs-private
 */
class MatGridTileFooterCssMatStyler {
}
MatGridTileFooterCssMatStyler.ɵfac = function MatGridTileFooterCssMatStyler_Factory(t) { return new (t || MatGridTileFooterCssMatStyler)(); };
MatGridTileFooterCssMatStyler.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatGridTileFooterCssMatStyler, selectors: [["mat-grid-tile-footer"]], hostAttrs: [1, "mat-grid-tile-footer"] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridTileFooterCssMatStyler, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: 'mat-grid-tile-footer',
                host: { 'class': 'mat-grid-tile-footer' }
            }]
    }], null, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * RegExp that can be used to check whether a value will
 * be allowed inside a CSS `calc()` expression.
 */
const cssCalcAllowedValue = /^-?\d+((\.\d+)?[A-Za-z%$]?)+$/;
/**
 * Sets the style properties for an individual tile, given the position calculated by the
 * Tile Coordinator.
 * @docs-private
 */
class TileStyler {
    constructor() {
        this._rows = 0;
        this._rowspan = 0;
    }
    /**
     * Adds grid-list layout info once it is available. Cannot be processed in the constructor
     * because these properties haven't been calculated by that point.
     *
     * @param gutterSize Size of the grid's gutter.
     * @param tracker Instance of the TileCoordinator.
     * @param cols Amount of columns in the grid.
     * @param direction Layout direction of the grid.
     */
    init(gutterSize, tracker, cols, direction) {
        this._gutterSize = normalizeUnits(gutterSize);
        this._rows = tracker.rowCount;
        this._rowspan = tracker.rowspan;
        this._cols = cols;
        this._direction = direction;
    }
    /**
     * Computes the amount of space a single 1x1 tile would take up (width or height).
     * Used as a basis for other calculations.
     * @param sizePercent Percent of the total grid-list space that one 1x1 tile would take up.
     * @param gutterFraction Fraction of the gutter size taken up by one 1x1 tile.
     * @return The size of a 1x1 tile as an expression that can be evaluated via CSS calc().
     */
    getBaseTileSize(sizePercent, gutterFraction) {
        // Take the base size percent (as would be if evenly dividing the size between cells),
        // and then subtracting the size of one gutter. However, since there are no gutters on the
        // edges, each tile only uses a fraction (gutterShare = numGutters / numCells) of the gutter
        // size. (Imagine having one gutter per tile, and then breaking up the extra gutter on the
        // edge evenly among the cells).
        return `(${sizePercent}% - (${this._gutterSize} * ${gutterFraction}))`;
    }
    /**
     * Gets The horizontal or vertical position of a tile, e.g., the 'top' or 'left' property value.
     * @param offset Number of tiles that have already been rendered in the row/column.
     * @param baseSize Base size of a 1x1 tile (as computed in getBaseTileSize).
     * @return Position of the tile as a CSS calc() expression.
     */
    getTilePosition(baseSize, offset) {
        // The position comes the size of a 1x1 tile plus gutter for each previous tile in the
        // row/column (offset).
        return offset === 0 ? '0' : calc(`(${baseSize} + ${this._gutterSize}) * ${offset}`);
    }
    /**
     * Gets the actual size of a tile, e.g., width or height, taking rowspan or colspan into account.
     * @param baseSize Base size of a 1x1 tile (as computed in getBaseTileSize).
     * @param span The tile's rowspan or colspan.
     * @return Size of the tile as a CSS calc() expression.
     */
    getTileSize(baseSize, span) {
        return `(${baseSize} * ${span}) + (${span - 1} * ${this._gutterSize})`;
    }
    /**
     * Sets the style properties to be applied to a tile for the given row and column index.
     * @param tile Tile to which to apply the styling.
     * @param rowIndex Index of the tile's row.
     * @param colIndex Index of the tile's column.
     */
    setStyle(tile, rowIndex, colIndex) {
        // Percent of the available horizontal space that one column takes up.
        let percentWidthPerTile = 100 / this._cols;
        // Fraction of the vertical gutter size that each column takes up.
        // For example, if there are 5 columns, each column uses 4/5 = 0.8 times the gutter width.
        let gutterWidthFractionPerTile = (this._cols - 1) / this._cols;
        this.setColStyles(tile, colIndex, percentWidthPerTile, gutterWidthFractionPerTile);
        this.setRowStyles(tile, rowIndex, percentWidthPerTile, gutterWidthFractionPerTile);
    }
    /** Sets the horizontal placement of the tile in the list. */
    setColStyles(tile, colIndex, percentWidth, gutterWidth) {
        // Base horizontal size of a column.
        let baseTileWidth = this.getBaseTileSize(percentWidth, gutterWidth);
        // The width and horizontal position of each tile is always calculated the same way, but the
        // height and vertical position depends on the rowMode.
        let side = this._direction === 'rtl' ? 'right' : 'left';
        tile._setStyle(side, this.getTilePosition(baseTileWidth, colIndex));
        tile._setStyle('width', calc(this.getTileSize(baseTileWidth, tile.colspan)));
    }
    /**
     * Calculates the total size taken up by gutters across one axis of a list.
     */
    getGutterSpan() {
        return `${this._gutterSize} * (${this._rowspan} - 1)`;
    }
    /**
     * Calculates the total size taken up by tiles across one axis of a list.
     * @param tileHeight Height of the tile.
     */
    getTileSpan(tileHeight) {
        return `${this._rowspan} * ${this.getTileSize(tileHeight, 1)}`;
    }
    /**
     * Calculates the computed height and returns the correct style property to set.
     * This method can be implemented by each type of TileStyler.
     * @docs-private
     */
    getComputedHeight() { return null; }
}
/**
 * This type of styler is instantiated when the user passes in a fixed row height.
 * Example `<mat-grid-list cols="3" rowHeight="100px">`
 * @docs-private
 */
class FixedTileStyler extends TileStyler {
    constructor(fixedRowHeight) {
        super();
        this.fixedRowHeight = fixedRowHeight;
    }
    init(gutterSize, tracker, cols, direction) {
        super.init(gutterSize, tracker, cols, direction);
        this.fixedRowHeight = normalizeUnits(this.fixedRowHeight);
        if (!cssCalcAllowedValue.test(this.fixedRowHeight) &&
            (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw Error(`Invalid value "${this.fixedRowHeight}" set as rowHeight.`);
        }
    }
    setRowStyles(tile, rowIndex) {
        tile._setStyle('top', this.getTilePosition(this.fixedRowHeight, rowIndex));
        tile._setStyle('height', calc(this.getTileSize(this.fixedRowHeight, tile.rowspan)));
    }
    getComputedHeight() {
        return [
            'height', calc(`${this.getTileSpan(this.fixedRowHeight)} + ${this.getGutterSpan()}`)
        ];
    }
    reset(list) {
        list._setListStyle(['height', null]);
        if (list._tiles) {
            list._tiles.forEach(tile => {
                tile._setStyle('top', null);
                tile._setStyle('height', null);
            });
        }
    }
}
/**
 * This type of styler is instantiated when the user passes in a width:height ratio
 * for the row height.  Example `<mat-grid-list cols="3" rowHeight="3:1">`
 * @docs-private
 */
class RatioTileStyler extends TileStyler {
    constructor(value) {
        super();
        this._parseRatio(value);
    }
    setRowStyles(tile, rowIndex, percentWidth, gutterWidth) {
        let percentHeightPerTile = percentWidth / this.rowHeightRatio;
        this.baseTileHeight = this.getBaseTileSize(percentHeightPerTile, gutterWidth);
        // Use padding-top and margin-top to maintain the given aspect ratio, as
        // a percentage-based value for these properties is applied versus the *width* of the
        // containing block. See http://www.w3.org/TR/CSS2/box.html#margin-properties
        tile._setStyle('marginTop', this.getTilePosition(this.baseTileHeight, rowIndex));
        tile._setStyle('paddingTop', calc(this.getTileSize(this.baseTileHeight, tile.rowspan)));
    }
    getComputedHeight() {
        return [
            'paddingBottom', calc(`${this.getTileSpan(this.baseTileHeight)} + ${this.getGutterSpan()}`)
        ];
    }
    reset(list) {
        list._setListStyle(['paddingBottom', null]);
        list._tiles.forEach(tile => {
            tile._setStyle('marginTop', null);
            tile._setStyle('paddingTop', null);
        });
    }
    _parseRatio(value) {
        const ratioParts = value.split(':');
        if (ratioParts.length !== 2 && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw Error(`mat-grid-list: invalid ratio given for row-height: "${value}"`);
        }
        this.rowHeightRatio = parseFloat(ratioParts[0]) / parseFloat(ratioParts[1]);
    }
}
/**
 * This type of styler is instantiated when the user selects a "fit" row height mode.
 * In other words, the row height will reflect the total height of the container divided
 * by the number of rows.  Example `<mat-grid-list cols="3" rowHeight="fit">`
 *
 * @docs-private
 */
class FitTileStyler extends TileStyler {
    setRowStyles(tile, rowIndex) {
        // Percent of the available vertical space that one row takes up.
        let percentHeightPerTile = 100 / this._rowspan;
        // Fraction of the horizontal gutter size that each column takes up.
        let gutterHeightPerTile = (this._rows - 1) / this._rows;
        // Base vertical size of a column.
        let baseTileHeight = this.getBaseTileSize(percentHeightPerTile, gutterHeightPerTile);
        tile._setStyle('top', this.getTilePosition(baseTileHeight, rowIndex));
        tile._setStyle('height', calc(this.getTileSize(baseTileHeight, tile.rowspan)));
    }
    reset(list) {
        if (list._tiles) {
            list._tiles.forEach(tile => {
                tile._setStyle('top', null);
                tile._setStyle('height', null);
            });
        }
    }
}
/** Wraps a CSS string in a calc function */
function calc(exp) {
    return `calc(${exp})`;
}
/** Appends pixels to a CSS string if no units are given. */
function normalizeUnits(value) {
    return value.match(/([A-Za-z%]+)$/) ? value : `${value}px`;
}

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
// TODO(kara): Conditional (responsive) column count / row size.
// TODO(kara): Re-layout on window resize / media change (debounced).
// TODO(kara): gridTileHeader and gridTileFooter.
const MAT_FIT_MODE = 'fit';
class MatGridList {
    constructor(_element, _dir) {
        this._element = _element;
        this._dir = _dir;
        /** The amount of space between tiles. This will be something like '5px' or '2em'. */
        this._gutter = '1px';
    }
    /** Amount of columns in the grid list. */
    get cols() { return this._cols; }
    set cols(value) {
        this._cols = Math.max(1, Math.round((0,coercion/* coerceNumberProperty */.su)(value)));
    }
    /** Size of the grid list's gutter in pixels. */
    get gutterSize() { return this._gutter; }
    set gutterSize(value) { this._gutter = `${value == null ? '' : value}`; }
    /** Set internal representation of row height from the user-provided value. */
    get rowHeight() { return this._rowHeight; }
    set rowHeight(value) {
        const newValue = `${value == null ? '' : value}`;
        if (newValue !== this._rowHeight) {
            this._rowHeight = newValue;
            this._setTileStyler(this._rowHeight);
        }
    }
    ngOnInit() {
        this._checkCols();
        this._checkRowHeight();
    }
    /**
     * The layout calculation is fairly cheap if nothing changes, so there's little cost
     * to run it frequently.
     */
    ngAfterContentChecked() {
        this._layoutTiles();
    }
    /** Throw a friendly error if cols property is missing */
    _checkCols() {
        if (!this.cols && (typeof ngDevMode === 'undefined' || ngDevMode)) {
            throw Error(`mat-grid-list: must pass in number of columns. ` +
                `Example: <mat-grid-list cols="3">`);
        }
    }
    /** Default to equal width:height if rowHeight property is missing */
    _checkRowHeight() {
        if (!this._rowHeight) {
            this._setTileStyler('1:1');
        }
    }
    /** Creates correct Tile Styler subtype based on rowHeight passed in by user */
    _setTileStyler(rowHeight) {
        if (this._tileStyler) {
            this._tileStyler.reset(this);
        }
        if (rowHeight === MAT_FIT_MODE) {
            this._tileStyler = new FitTileStyler();
        }
        else if (rowHeight && rowHeight.indexOf(':') > -1) {
            this._tileStyler = new RatioTileStyler(rowHeight);
        }
        else {
            this._tileStyler = new FixedTileStyler(rowHeight);
        }
    }
    /** Computes and applies the size and position for all children grid tiles. */
    _layoutTiles() {
        if (!this._tileCoordinator) {
            this._tileCoordinator = new TileCoordinator();
        }
        const tracker = this._tileCoordinator;
        const tiles = this._tiles.filter(tile => !tile._gridList || tile._gridList === this);
        const direction = this._dir ? this._dir.value : 'ltr';
        this._tileCoordinator.update(this.cols, tiles);
        this._tileStyler.init(this.gutterSize, tracker, this.cols, direction);
        tiles.forEach((tile, index) => {
            const pos = tracker.positions[index];
            this._tileStyler.setStyle(tile, pos.row, pos.col);
        });
        this._setListStyle(this._tileStyler.getComputedHeight());
    }
    /** Sets style on the main grid-list element, given the style name and value. */
    _setListStyle(style) {
        if (style) {
            this._element.nativeElement.style[style[0]] = style[1];
        }
    }
}
MatGridList.ɵfac = function MatGridList_Factory(t) { return new (t || MatGridList)(core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(bidi/* Directionality */.Is, 8)); };
MatGridList.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatGridList, selectors: [["mat-grid-list"]], contentQueries: function MatGridList_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatGridTile, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx._tiles = _t);
    } }, hostAttrs: [1, "mat-grid-list"], hostVars: 1, hostBindings: function MatGridList_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵattribute */.uIk("cols", ctx.cols);
    } }, inputs: { cols: "cols", gutterSize: "gutterSize", rowHeight: "rowHeight" }, exportAs: ["matGridList"], features: [core/* ɵɵProvidersFeature */._Bn([{
                provide: MAT_GRID_LIST,
                useExisting: MatGridList
            }])], ngContentSelectors: grid_list_c0, decls: 2, vars: 0, template: function MatGridList_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t();
        core/* ɵɵelementStart */.TgZ(0, "div");
        core/* ɵɵprojection */.Hsn(1);
        core/* ɵɵelementEnd */.qZA();
    } }, styles: [grid_list_c3], encapsulation: 2, changeDetection: 0 });
MatGridList.ctorParameters = () => [
    { type: core/* ElementRef */.SBq },
    { type: bidi/* Directionality */.Is, decorators: [{ type: core/* Optional */.FiY }] }
];
MatGridList.propDecorators = {
    _tiles: [{ type: core/* ContentChildren */.AcB, args: [MatGridTile, { descendants: true },] }],
    cols: [{ type: core/* Input */.IIB }],
    gutterSize: [{ type: core/* Input */.IIB }],
    rowHeight: [{ type: core/* Input */.IIB }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridList, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-grid-list',
                exportAs: 'matGridList',
                template: "<div>\n  <ng-content></ng-content>\n</div>",
                host: {
                    'class': 'mat-grid-list',
                    // Ensures that the "cols" input value is reflected in the DOM. This is
                    // needed for the grid-list harness.
                    '[attr.cols]': 'cols'
                },
                providers: [{
                        provide: MAT_GRID_LIST,
                        useExisting: MatGridList
                    }],
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                styles: [".mat-grid-list{display:block;position:relative}.mat-grid-tile{display:block;position:absolute;overflow:hidden}.mat-grid-tile .mat-grid-tile-header,.mat-grid-tile .mat-grid-tile-footer{display:flex;align-items:center;height:48px;color:#fff;background:rgba(0,0,0,.38);overflow:hidden;padding:0 16px;position:absolute;left:0;right:0}.mat-grid-tile .mat-grid-tile-header>*,.mat-grid-tile .mat-grid-tile-footer>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-tile-header.mat-2-line,.mat-grid-tile .mat-grid-tile-footer.mat-2-line{height:68px}.mat-grid-tile .mat-grid-list-text{display:flex;flex-direction:column;flex:auto;box-sizing:border-box;overflow:hidden}.mat-grid-tile .mat-grid-list-text>*{margin:0;padding:0;font-weight:normal;font-size:inherit}.mat-grid-tile .mat-grid-list-text:empty{display:none}.mat-grid-tile .mat-grid-tile-header{top:0}.mat-grid-tile .mat-grid-tile-footer{bottom:0}.mat-grid-tile .mat-grid-avatar{padding-right:16px}[dir=rtl] .mat-grid-tile .mat-grid-avatar{padding-right:0;padding-left:16px}.mat-grid-tile .mat-grid-avatar:empty{display:none}.mat-grid-tile-content{top:0;left:0;right:0;bottom:0;position:absolute;display:flex;align-items:center;justify-content:center;height:100%;padding:0;margin:0}\n"]
            }]
    }], function () { return [{ type: core/* ElementRef */.SBq }, { type: bidi/* Directionality */.Is, decorators: [{
                type: core/* Optional */.FiY
            }] }]; }, { cols: [{
            type: core/* Input */.IIB
        }], gutterSize: [{
            type: core/* Input */.IIB
        }], rowHeight: [{
            type: core/* Input */.IIB
        }], _tiles: [{
            type: core/* ContentChildren */.AcB,
            args: [MatGridTile, { descendants: true }]
        }] }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatGridListModule {
}
MatGridListModule.ɵfac = function MatGridListModule_Factory(t) { return new (t || MatGridListModule)(); };
MatGridListModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatGridListModule });
MatGridListModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[fesm2015_core/* MatLineModule */.uc, fesm2015_core/* MatCommonModule */.BQ], fesm2015_core/* MatLineModule */.uc,
        fesm2015_core/* MatCommonModule */.BQ] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatGridListModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [fesm2015_core/* MatLineModule */.uc, fesm2015_core/* MatCommonModule */.BQ],
                exports: [
                    MatGridList,
                    MatGridTile,
                    MatGridTileText,
                    fesm2015_core/* MatLineModule */.uc,
                    fesm2015_core/* MatCommonModule */.BQ,
                    MatGridTileHeaderCssMatStyler,
                    MatGridTileFooterCssMatStyler,
                    MatGridAvatarCssMatStyler
                ],
                declarations: [
                    MatGridList,
                    MatGridTile,
                    MatGridTileText,
                    MatGridTileHeaderCssMatStyler,
                    MatGridTileFooterCssMatStyler,
                    MatGridAvatarCssMatStyler
                ]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatGridListModule, { declarations: function () { return [MatGridList, MatGridTile, MatGridTileText, MatGridTileHeaderCssMatStyler, MatGridTileFooterCssMatStyler, MatGridAvatarCssMatStyler]; }, imports: function () { return [fesm2015_core/* MatLineModule */.uc, fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [MatGridList, MatGridTile, MatGridTileText, fesm2015_core/* MatLineModule */.uc,
        fesm2015_core/* MatCommonModule */.BQ, MatGridTileHeaderCssMatStyler, MatGridTileFooterCssMatStyler, MatGridAvatarCssMatStyler]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
// Privately exported for the grid-list harness.
const ɵTileCoordinator = (/* unused pure expression or super */ null && (TileCoordinator));

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=grid-list.js.map
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/divider.js
var divider = __webpack_require__(1769);
// EXTERNAL MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/icon.js
var icon = __webpack_require__(6627);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/BehaviorSubject.js
var BehaviorSubject = __webpack_require__(6215);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/Observable.js + 3 modules
var Observable = __webpack_require__(7574);
// EXTERNAL MODULE: ./node_modules/rxjs/_esm2015/internal/util/isNumeric.js
var isNumeric = __webpack_require__(6561);
;// CONCATENATED MODULE: ./node_modules/rxjs/_esm2015/internal/observable/interval.js



function interval(period = 0, scheduler = scheduler_async/* async */.P) {
    if (!(0,isNumeric/* isNumeric */.k)(period) || period < 0) {
        period = 0;
    }
    if (!scheduler || typeof scheduler.schedule !== 'function') {
        scheduler = scheduler_async/* async */.P;
    }
    return new Observable/* Observable */.y(subscriber => {
        subscriber.add(scheduler.schedule(dispatch, period, { subscriber, counter: 0, period }));
        return subscriber;
    });
}
function dispatch(state) {
    const { subscriber, counter, period } = state;
    subscriber.next(counter);
    this.schedule({ subscriber, counter: counter + 1, period }, period);
}
//# sourceMappingURL=interval.js.map
;// CONCATENATED MODULE: ./node_modules/@ngbmodule/material-carousel/__ivy_ngcc__/fesm2015/ngbmodule-material-carousel.js










/**
 * @fileoverview added by tsickle
 * Generated from: lib/carousel-slide/carousel-slide.component.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */







function MatCarouselSlideComponent_ng_template_0_div_3_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "div", 3);
} if (rf & 2) {
    const ctx_r1 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵstyleProp */.Udp("background-color", ctx_r1.overlayColor);
} }
function MatCarouselSlideComponent_ng_template_0_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 0);
    core/* ɵɵelementStart */.TgZ(1, "div", 1);
    core/* ɵɵprojection */.Hsn(2);
    core/* ɵɵelementEnd */.qZA();
    core/* ɵɵtemplate */.YNc(3, MatCarouselSlideComponent_ng_template_0_div_3_Template, 1, 2, "div", 2);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r0 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵstyleProp */.Udp("background-image", ctx_r0.image);
    core/* ɵɵadvance */.xp6(3);
    core/* ɵɵproperty */.Q6J("ngIf", !ctx_r0.hideOverlay);
} }
const ngbmodule_material_carousel_c0 = ["*"];
const ngbmodule_material_carousel_c1 = ["carouselContainer"];
const ngbmodule_material_carousel_c2 = ["carouselList"];
function MatCarouselComponent_li_4_Template(rf, ctx) { if (rf & 1) {
    const _r9 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "li", 7, 8);
    core/* ɵɵlistener */.NdJ("panleft", function MatCarouselComponent_li_4_Template_li_panleft_0_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const _r7 = core/* ɵɵreference */.MAs(1); const ctx_r8 = core/* ɵɵnextContext */.oxw(); return ctx_r8.onPan($event, _r7); })("panright", function MatCarouselComponent_li_4_Template_li_panright_0_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const _r7 = core/* ɵɵreference */.MAs(1); const ctx_r10 = core/* ɵɵnextContext */.oxw(); return ctx_r10.onPan($event, _r7); })("panend", function MatCarouselComponent_li_4_Template_li_panend_0_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const _r7 = core/* ɵɵreference */.MAs(1); const ctx_r11 = core/* ɵɵnextContext */.oxw(); return ctx_r11.onPanEnd($event, _r7); })("pancancel", function MatCarouselComponent_li_4_Template_li_pancancel_0_listener($event) { core/* ɵɵrestoreView */.CHM(_r9); const _r7 = core/* ɵɵreference */.MAs(1); const ctx_r12 = core/* ɵɵnextContext */.oxw(); return ctx_r12.onPanEnd($event, _r7); });
    core/* ɵɵelementContainer */.GkF(2, 9);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const slide_r6 = ctx.$implicit;
    const ctx_r2 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵstyleProp */.Udp("padding-bottom", ctx_r2.maintainAspectRatio && ctx_r2.proportion ? ctx_r2.proportion + "%" : "0px")("height", !ctx_r2.maintainAspectRatio && ctx_r2.slideHeight ? ctx_r2.slideHeight : "0px");
    core/* ɵɵadvance */.xp6(2);
    core/* ɵɵproperty */.Q6J("ngTemplateOutlet", slide_r6.templateRef);
} }
function MatCarouselComponent_button_5_mat_icon_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "mat-icon", 13);
} if (rf & 2) {
    const ctx_r13 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("svgIcon", ctx_r13.svgIconOverrides.arrowBack);
} }
function MatCarouselComponent_button_5_ng_template_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-icon");
    core/* ɵɵtext */._uU(1, "arrow_back");
    core/* ɵɵelementEnd */.qZA();
} }
function MatCarouselComponent_button_5_Template(rf, ctx) { if (rf & 1) {
    const _r17 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "button", 10);
    core/* ɵɵlistener */.NdJ("click", function MatCarouselComponent_button_5_Template_button_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r17); const ctx_r16 = core/* ɵɵnextContext */.oxw(); return ctx_r16.previous(); });
    core/* ɵɵtemplate */.YNc(1, MatCarouselComponent_button_5_mat_icon_1_Template, 1, 1, "mat-icon", 11);
    core/* ɵɵtemplate */.YNc(2, MatCarouselComponent_button_5_ng_template_2_Template, 2, 0, "ng-template", null, 12, core/* ɵɵtemplateRefExtractor */.W1O);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const _r14 = core/* ɵɵreference */.MAs(3);
    const ctx_r3 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("color", ctx_r3.color)("disabled", !ctx_r3.loop && ctx_r3.currentIndex == 0);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r3.svgIconOverrides == null ? null : ctx_r3.svgIconOverrides.arrowBack)("ngIfElse", _r14);
} }
function MatCarouselComponent_button_6_mat_icon_1_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelement */._UZ(0, "mat-icon", 13);
} if (rf & 2) {
    const ctx_r18 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("svgIcon", ctx_r18.svgIconOverrides.arrowForward);
} }
function MatCarouselComponent_button_6_ng_template_2_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "mat-icon");
    core/* ɵɵtext */._uU(1, "arrow_forward");
    core/* ɵɵelementEnd */.qZA();
} }
function MatCarouselComponent_button_6_Template(rf, ctx) { if (rf & 1) {
    const _r22 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "button", 10);
    core/* ɵɵlistener */.NdJ("click", function MatCarouselComponent_button_6_Template_button_click_0_listener() { core/* ɵɵrestoreView */.CHM(_r22); const ctx_r21 = core/* ɵɵnextContext */.oxw(); return ctx_r21.next(); });
    core/* ɵɵtemplate */.YNc(1, MatCarouselComponent_button_6_mat_icon_1_Template, 1, 1, "mat-icon", 11);
    core/* ɵɵtemplate */.YNc(2, MatCarouselComponent_button_6_ng_template_2_Template, 2, 0, "ng-template", null, 14, core/* ɵɵtemplateRefExtractor */.W1O);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const _r19 = core/* ɵɵreference */.MAs(3);
    const ctx_r4 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵproperty */.Q6J("color", ctx_r4.color)("disabled", !ctx_r4.loop && ctx_r4.currentIndex == ctx_r4.slidesList.length - 1);
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngIf", ctx_r4.svgIconOverrides == null ? null : ctx_r4.svgIconOverrides.arrowForward)("ngIfElse", _r19);
} }
function MatCarouselComponent_div_7_button_1_Template(rf, ctx) { if (rf & 1) {
    const _r27 = core/* ɵɵgetCurrentView */.EpF();
    core/* ɵɵelementStart */.TgZ(0, "button", 17);
    core/* ɵɵlistener */.NdJ("click", function MatCarouselComponent_div_7_button_1_Template_button_click_0_listener() { const restoredCtx = core/* ɵɵrestoreView */.CHM(_r27); const i_r25 = restoredCtx.index; const ctx_r26 = core/* ɵɵnextContext */.oxw(2); return ctx_r26.slideTo(i_r25); })("focus", function MatCarouselComponent_div_7_button_1_Template_button_focus_0_listener() { core/* ɵɵrestoreView */.CHM(_r27); core/* ɵɵnextContext */.oxw(2); const _r0 = core/* ɵɵreference */.MAs(1); return _r0.focus(); });
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const i_r25 = ctx.index;
    const ctx_r23 = core/* ɵɵnextContext */.oxw(2);
    core/* ɵɵproperty */.Q6J("color", ctx_r23.color)("disabled", i_r25 == ctx_r23.currentIndex);
} }
function MatCarouselComponent_div_7_Template(rf, ctx) { if (rf & 1) {
    core/* ɵɵelementStart */.TgZ(0, "div", 15);
    core/* ɵɵtemplate */.YNc(1, MatCarouselComponent_div_7_button_1_Template, 1, 2, "button", 16);
    core/* ɵɵelementEnd */.qZA();
} if (rf & 2) {
    const ctx_r5 = core/* ɵɵnextContext */.oxw();
    core/* ɵɵstyleProp */.Udp("flex-direction", ctx_r5.orientation === "rtl" ? "row-reverse" : "row");
    core/* ɵɵadvance */.xp6(1);
    core/* ɵɵproperty */.Q6J("ngForOf", ctx_r5.slidesList);
} }
class MatCarouselSlideComponent {
    /**
     * @param {?} sanitizer
     */
    constructor(sanitizer) {
        this.sanitizer = sanitizer;
        this.overlayColor = '#00000040';
        this.hideOverlay = false;
        this.disabled = false; // implements ListKeyManagerOption
    }
    /**
     * @return {?}
     */
    ngOnInit() {
        if (this.image) {
            this.image = this.sanitizer.bypassSecurityTrustStyle(`url("${this.image}")`);
        }
    }
}
MatCarouselSlideComponent.ɵfac = function MatCarouselSlideComponent_Factory(t) { return new (t || MatCarouselSlideComponent)(core/* ɵɵdirectiveInject */.Y36(platform_browser/* DomSanitizer */.H7)); };
MatCarouselSlideComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatCarouselSlideComponent, selectors: [["mat-carousel-slide"]], viewQuery: function MatCarouselSlideComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(core/* TemplateRef */.Rgc, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.templateRef = _t.first);
    } }, inputs: { overlayColor: "overlayColor", hideOverlay: "hideOverlay", disabled: "disabled", image: "image" }, ngContentSelectors: ngbmodule_material_carousel_c0, decls: 1, vars: 0, consts: [[1, "carousel-slide"], [1, "carousel-slide-content"], ["class", "carousel-slide-overlay", 3, "background-color", 4, "ngIf"], [1, "carousel-slide-overlay"]], template: function MatCarouselSlideComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵprojectionDef */.F$t();
        core/* ɵɵtemplate */.YNc(0, MatCarouselSlideComponent_ng_template_0_Template, 4, 3, "ng-template");
    } }, directives: [common/* NgIf */.O5], styles: [".carousel-slide[_ngcontent-%COMP%]{background-position:50%;background-repeat:no-repeat;background-size:cover}.carousel-slide[_ngcontent-%COMP%], .carousel-slide-overlay[_ngcontent-%COMP%]{height:100%;position:absolute;width:100%;z-index:auto}.carousel-slide-content[_ngcontent-%COMP%]{height:100%;position:absolute;width:100%;z-index:1}"] });
/** @nocollapse */
MatCarouselSlideComponent.ctorParameters = () => [
    { type: platform_browser/* DomSanitizer */.H7 }
];
MatCarouselSlideComponent.propDecorators = {
    image: [{ type: core/* Input */.IIB }],
    overlayColor: [{ type: core/* Input */.IIB }],
    hideOverlay: [{ type: core/* Input */.IIB }],
    disabled: [{ type: core/* Input */.IIB }],
    templateRef: [{ type: core/* ViewChild */.i9L, args: [core/* TemplateRef */.Rgc,] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCarouselSlideComponent, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-carousel-slide',
                template: "<ng-template>\r\n  <div class=\"carousel-slide\" [style.background-image]=\"image\">\r\n    <div class=\"carousel-slide-content\"><ng-content></ng-content></div>\r\n    <div\r\n      *ngIf=\"!hideOverlay\"\r\n      class=\"carousel-slide-overlay\"\r\n      [style.background-color]=\"overlayColor\"\r\n    ></div>\r\n  </div>\r\n</ng-template>\r\n",
                styles: [".carousel-slide{background-position:50%;background-repeat:no-repeat;background-size:cover}.carousel-slide,.carousel-slide-overlay{height:100%;position:absolute;width:100%;z-index:auto}.carousel-slide-content{height:100%;position:absolute;width:100%;z-index:1}"]
            }]
    }], function () { return [{ type: platform_browser/* DomSanitizer */.H7 }]; }, { overlayColor: [{
            type: core/* Input */.IIB
        }], hideOverlay: [{
            type: core/* Input */.IIB
        }], disabled: [{
            type: core/* Input */.IIB
        }], image: [{
            type: core/* Input */.IIB
        }], templateRef: [{
            type: core/* ViewChild */.i9L,
            args: [core/* TemplateRef */.Rgc]
        }] }); })();
if (false) {}

/**
 * @fileoverview added by tsickle
 * Generated from: lib/carousel.component.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/** @enum {number} */
const Direction = {
    Left: 0,
    Right: 1,
    Index: 2,
};
Direction[Direction.Left] = 'Left';
Direction[Direction.Right] = 'Right';
Direction[Direction.Index] = 'Index';
class MatCarouselComponent {
    /**
     * @param {?} animationBuilder
     * @param {?} renderer
     * @param {?} platformId
     */
    constructor(animationBuilder, renderer, platformId) {
        this.animationBuilder = animationBuilder;
        this.renderer = renderer;
        this.platformId = platformId;
        this.timings = '250ms ease-in';
        this.hideArrows = true;
        this.hideIndicators = true;
        this.color = 'accent';
        this.maintainAspectRatio = true;
        this.proportion = 25;
        this.slideHeight = '100%';
        this.useKeyboard = false;
        this.useMouseWheel = false;
        this.change = new core/* EventEmitter */.vpe();
        this._autoplay = true;
        this.autoplay$ = new Subject/* Subject */.xQ();
        this.interval$ = new BehaviorSubject/* BehaviorSubject */.X(5000);
        this.slides$ = new BehaviorSubject/* BehaviorSubject */.X(null);
        this._maxWidth = 'auto';
        this.maxWidth$ = new Subject/* Subject */.xQ();
        this._loop = true;
        this.loop$ = new Subject/* Subject */.xQ();
        this._orientation = 'ltr';
        this.orientation$ = new Subject/* Subject */.xQ();
        this.timerStop$ = new Subject/* Subject */.xQ();
        this.destroy$ = new Subject/* Subject */.xQ();
        this.playing = false;
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set autoplay(value) {
        this.autoplay$.next(value);
        this._autoplay = value;
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set interval(value) {
        this.interval$.next(value);
    }
    /**
     * @return {?}
     */
    get loop() {
        return this._loop;
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set loop(value) {
        this.loop$.next(value);
        this._loop = value;
    }
    /**
     * @return {?}
     */
    get maxWidth() {
        return this._maxWidth;
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set maxWidth(value) {
        this._maxWidth = value;
        this.maxWidth$.next();
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set slides(value) {
        this.slides$.next(value);
    }
    /**
     * @return {?}
     */
    get orientation() {
        return this._orientation;
    }
    /**
     * @param {?} value
     * @return {?}
     */
    set orientation(value) {
        this.orientation$.next(value);
        this._orientation = value;
    }
    /**
     * @return {?}
     */
    get currentIndex() {
        if (this.listKeyManager) {
            return this.listKeyManager.activeItemIndex;
        }
        return 0;
    }
    /**
     * @return {?}
     */
    get currentSlide() {
        if (this.listKeyManager) {
            return this.listKeyManager.activeItem;
        }
        return null;
    }
    /**
     * @return {?}
     */
    ngAfterContentInit() {
        this.listKeyManager = new a11y/* ListKeyManager */.cS(this.slidesList)
            .withVerticalOrientation(false)
            .withHorizontalOrientation(this._orientation)
            .withWrap(this._loop);
        this.listKeyManager.updateActiveItem(0);
        this.listKeyManager.change
            .pipe((0,takeUntil/* takeUntil */.R)(this.destroy$))
            .subscribe((/**
         * @return {?}
         */
        () => this.playAnimation()));
    }
    /**
     * @return {?}
     */
    ngAfterViewInit() {
        this.autoplay$.pipe((0,takeUntil/* takeUntil */.R)(this.destroy$)).subscribe((/**
         * @param {?} value
         * @return {?}
         */
        value => {
            this.stopTimer();
            this.startTimer(value);
        }));
        this.interval$.pipe((0,takeUntil/* takeUntil */.R)(this.destroy$)).subscribe((/**
         * @param {?} value
         * @return {?}
         */
        value => {
            this.stopTimer();
            this.resetTimer(value);
            this.startTimer(this._autoplay);
        }));
        this.maxWidth$
            .pipe((0,takeUntil/* takeUntil */.R)(this.destroy$))
            .subscribe((/**
         * @return {?}
         */
        () => this.slideTo(0)));
        this.loop$
            .pipe((0,takeUntil/* takeUntil */.R)(this.destroy$))
            .subscribe((/**
         * @param {?} value
         * @return {?}
         */
        value => this.listKeyManager.withWrap(value)));
        this.orientation$
            .pipe((0,takeUntil/* takeUntil */.R)(this.destroy$))
            .subscribe((/**
         * @param {?} value
         * @return {?}
         */
        value => this.listKeyManager.withHorizontalOrientation(value)));
        this.slides$
            .pipe((0,takeUntil/* takeUntil */.R)(this.destroy$), (0,filter/* filter */.h)((/**
         * @param {?} value
         * @return {?}
         */
        value => value && value < this.slidesList.length)))
            .subscribe((/**
         * @param {?} value
         * @return {?}
         */
        value => this.resetSlides(value)));
    }
    /**
     * @return {?}
     */
    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
    }
    /**
     * @return {?}
     */
    next() {
        this.goto(Direction.Right);
    }
    /**
     * @return {?}
     */
    previous() {
        this.goto(Direction.Left);
    }
    /**
     * @param {?} index
     * @return {?}
     */
    slideTo(index) {
        this.goto(Direction.Index, index);
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onKeyUp(event) {
        if (this.useKeyboard && !this.playing) {
            this.listKeyManager.onKeydown(event);
        }
    }
    /**
     * @return {?}
     */
    onMouseEnter() {
        this.stopTimer();
    }
    /**
     * @return {?}
     */
    onMouseLeave() {
        this.startTimer(this._autoplay);
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onMouseWheel(event) {
        if (this.useMouseWheel) {
            event.preventDefault(); // prevent window to scroll
            // prevent window to scroll
            /** @type {?} */
            const Δ = Math.sign(event.deltaY);
            if (Δ > 0) {
                this.next();
            }
            else if (Δ < 0) {
                this.previous();
            }
        }
    }
    /**
     * @param {?} event
     * @return {?}
     */
    onResize(event) {
        // Reset carousel when window is resized
        // in order to avoid major glitches.
        this.slideTo(0);
    }
    /**
     * @param {?} event
     * @param {?} slideElem
     * @return {?}
     */
    onPan(event, slideElem) {
        // https://github.com/angular/angular/issues/10541#issuecomment-346539242
        // if y velocity is greater, it's a panup/pandown, so ignore.
        if (Math.abs(event.velocityY) > Math.abs(event.velocityX)) {
            return;
        }
        /** @type {?} */
        let Δx = event.deltaX;
        if (this.isOutOfBounds()) {
            Δx *= 0.2; // decelerate movement;
        }
        this.renderer.setStyle(slideElem, 'cursor', 'grabbing');
        this.renderer.setStyle(this.carouselList.nativeElement, 'transform', this.getTranslation(this.getOffset() + Δx));
    }
    /**
     * @param {?} event
     * @param {?} slideElem
     * @return {?}
     */
    onPanEnd(event, slideElem) {
        this.renderer.removeStyle(slideElem, 'cursor');
        if (!this.isOutOfBounds() &&
            Math.abs(event.deltaX) > this.getWidth() * 0.25) {
            if (event.deltaX <= 0) {
                this.next();
                return;
            }
            this.previous();
            return;
        }
        this.playAnimation(); // slide back, don't change current index
    }
    /**
     * @private
     * @return {?}
     */
    isOutOfBounds() {
        /** @type {?} */
        const sign = this.orientation === 'rtl' ? -1 : 1;
        /** @type {?} */
        const left = sign *
            (this.carouselList.nativeElement.getBoundingClientRect().left -
                this.carouselList.nativeElement.offsetParent.getBoundingClientRect()
                    .left);
        /** @type {?} */
        const lastIndex = this.slidesList.length - 1;
        /** @type {?} */
        const width = -this.getWidth() * lastIndex;
        return ((this.listKeyManager.activeItemIndex === 0 && left >= 0) ||
            (this.listKeyManager.activeItemIndex === lastIndex && left <= width));
    }
    /**
     * @private
     * @return {?}
     */
    isVisible() {
        if (!(0,common/* isPlatformBrowser */.NF)(this.platformId)) {
            return false;
        }
        /** @type {?} */
        const elem = this.carouselContainer.nativeElement;
        /** @type {?} */
        const docViewTop = window.pageYOffset;
        /** @type {?} */
        const docViewBottom = docViewTop + window.innerHeight;
        /** @type {?} */
        const elemOffset = elem.getBoundingClientRect();
        /** @type {?} */
        const elemTop = docViewTop + elemOffset.top;
        /** @type {?} */
        const elemBottom = elemTop + elemOffset.height;
        return elemBottom <= docViewBottom || elemTop >= docViewTop;
    }
    /**
     * @private
     * @return {?}
     */
    getOffset() {
        /** @type {?} */
        const offset = this.listKeyManager.activeItemIndex * this.getWidth();
        /** @type {?} */
        const sign = this.orientation === 'rtl' ? 1 : -1;
        return sign * offset;
    }
    /**
     * @private
     * @param {?} offset
     * @return {?}
     */
    getTranslation(offset) {
        return `translateX(${offset}px)`;
    }
    /**
     * @private
     * @return {?}
     */
    getWidth() {
        return this.carouselContainer.nativeElement.clientWidth;
    }
    /**
     * @private
     * @param {?} direction
     * @param {?=} index
     * @return {?}
     */
    goto(direction, index) {
        if (!this.playing) {
            /** @type {?} */
            const rtl = this.orientation === 'rtl';
            switch (direction) {
                case Direction.Left:
                    return rtl
                        ? this.listKeyManager.setNextItemActive()
                        : this.listKeyManager.setPreviousItemActive();
                case Direction.Right:
                    return rtl
                        ? this.listKeyManager.setPreviousItemActive()
                        : this.listKeyManager.setNextItemActive();
                case Direction.Index:
                    return this.listKeyManager.setActiveItem(index);
            }
        }
    }
    /**
     * @private
     * @return {?}
     */
    playAnimation() {
        /** @type {?} */
        const translation = this.getTranslation(this.getOffset());
        /** @type {?} */
        const factory = this.animationBuilder.build((0,animations/* animate */.jt)(this.timings, (0,animations/* style */.oB)({ transform: translation })));
        /** @type {?} */
        const animation = factory.create(this.carouselList.nativeElement);
        animation.onStart((/**
         * @return {?}
         */
        () => (this.playing = true)));
        animation.onDone((/**
         * @return {?}
         */
        () => {
            this.change.emit(this.currentIndex);
            this.playing = false;
            this.renderer.setStyle(this.carouselList.nativeElement, 'transform', translation);
            animation.destroy();
        }));
        animation.play();
    }
    /**
     * @private
     * @param {?} slides
     * @return {?}
     */
    resetSlides(slides) {
        this.slidesList.reset(this.slidesList.toArray().slice(0, slides));
    }
    /**
     * @private
     * @param {?} value
     * @return {?}
     */
    resetTimer(value) {
        this.timer$ = interval(value);
    }
    /**
     * @private
     * @param {?} autoplay
     * @return {?}
     */
    startTimer(autoplay) {
        if (!autoplay) {
            return;
        }
        this.timer$
            .pipe((0,takeUntil/* takeUntil */.R)(this.timerStop$), (0,takeUntil/* takeUntil */.R)(this.destroy$), (0,filter/* filter */.h)((/**
         * @return {?}
         */
        () => this.isVisible())))
            .subscribe((/**
         * @return {?}
         */
        () => {
            this.listKeyManager.withWrap(true).setNextItemActive();
            this.listKeyManager.withWrap(this.loop);
        }));
    }
    /**
     * @private
     * @return {?}
     */
    stopTimer() {
        this.timerStop$.next();
    }
}
MatCarouselComponent.ɵfac = function MatCarouselComponent_Factory(t) { return new (t || MatCarouselComponent)(core/* ɵɵdirectiveInject */.Y36(animations/* AnimationBuilder */._j), core/* ɵɵdirectiveInject */.Y36(core/* Renderer2 */.Qsj), core/* ɵɵdirectiveInject */.Y36(core/* PLATFORM_ID */.Lbi)); };
MatCarouselComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: MatCarouselComponent, selectors: [["mat-carousel"]], contentQueries: function MatCarouselComponent_ContentQueries(rf, ctx, dirIndex) { if (rf & 1) {
        core/* ɵɵcontentQuery */.Suo(dirIndex, MatCarouselSlideComponent, 4);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.slidesList = _t);
    } }, viewQuery: function MatCarouselComponent_Query(rf, ctx) { if (rf & 1) {
        core/* ɵɵviewQuery */.Gf(ngbmodule_material_carousel_c1, 5);
        core/* ɵɵviewQuery */.Gf(ngbmodule_material_carousel_c2, 5);
    } if (rf & 2) {
        let _t;
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.carouselContainer = _t.first);
        core/* ɵɵqueryRefresh */.iGM(_t = core/* ɵɵloadQuery */.CRH()) && (ctx.carouselList = _t.first);
    } }, hostBindings: function MatCarouselComponent_HostBindings(rf, ctx) { if (rf & 1) {
        core/* ɵɵlistener */.NdJ("keyup", function MatCarouselComponent_keyup_HostBindingHandler($event) { return ctx.onKeyUp($event); })("mouseenter", function MatCarouselComponent_mouseenter_HostBindingHandler() { return ctx.onMouseEnter(); })("mouseleave", function MatCarouselComponent_mouseleave_HostBindingHandler() { return ctx.onMouseLeave(); })("mousewheel", function MatCarouselComponent_mousewheel_HostBindingHandler($event) { return ctx.onMouseWheel($event); })("resize", function MatCarouselComponent_resize_HostBindingHandler($event) { return ctx.onResize($event); }, false, core/* ɵɵresolveWindow */.Jf7);
    } }, inputs: { timings: "timings", hideArrows: "hideArrows", hideIndicators: "hideIndicators", color: "color", maintainAspectRatio: "maintainAspectRatio", proportion: "proportion", slideHeight: "slideHeight", useKeyboard: "useKeyboard", useMouseWheel: "useMouseWheel", autoplay: "autoplay", interval: "interval", loop: "loop", maxWidth: "maxWidth", slides: "slides", orientation: "orientation", svgIconOverrides: "svgIconOverrides" }, outputs: { change: "change" }, decls: 8, vars: 12, consts: [["tabindex", "0", 1, "carousel"], ["carouselContainer", ""], ["role", "listbox", 1, "carousel-list"], ["carouselList", ""], ["class", "carousel-slide", "role", "option", 3, "padding-bottom", "height", "panleft", "panright", "panend", "pancancel", 4, "ngFor", "ngForOf"], ["mat-icon-button", "", "type", "button", "tabindex", "-1", 3, "color", "disabled", "click", 4, "ngIf"], ["class", "carousel-indicators", "tabindex", "-1", 3, "flex-direction", 4, "ngIf"], ["role", "option", 1, "carousel-slide", 3, "panleft", "panright", "panend", "pancancel"], ["carouselSlide", ""], [3, "ngTemplateOutlet"], ["mat-icon-button", "", "type", "button", "tabindex", "-1", 3, "color", "disabled", "click"], [3, "svgIcon", 4, "ngIf", "ngIfElse"], ["defaultArrowBack", ""], [3, "svgIcon"], ["defaultArrowForward", ""], ["tabindex", "-1", 1, "carousel-indicators"], ["type", "button", "tabindex", "-1", "mat-mini-fab", "", 3, "color", "disabled", "click", "focus", 4, "ngFor", "ngForOf"], ["type", "button", "tabindex", "-1", "mat-mini-fab", "", 3, "color", "disabled", "click", "focus"]], template: function MatCarouselComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0, 1);
        core/* ɵɵelementStart */.TgZ(2, "ul", 2, 3);
        core/* ɵɵtemplate */.YNc(4, MatCarouselComponent_li_4_Template, 3, 5, "li", 4);
        core/* ɵɵelementEnd */.qZA();
        core/* ɵɵtemplate */.YNc(5, MatCarouselComponent_button_5_Template, 4, 4, "button", 5);
        core/* ɵɵtemplate */.YNc(6, MatCarouselComponent_button_6_Template, 4, 4, "button", 5);
        core/* ɵɵtemplate */.YNc(7, MatCarouselComponent_div_7_Template, 2, 3, "div", 6);
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        core/* ɵɵstyleProp */.Udp("max-width", ctx.maxWidth)("height", !ctx.maintainAspectRatio ? "100%" : "auto");
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵstyleProp */.Udp("flex-direction", ctx.orientation === "rtl" ? "row-reverse" : "row")("height", !ctx.maintainAspectRatio ? "100%" : "auto");
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵproperty */.Q6J("ngForOf", ctx.slidesList);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", !ctx.hideArrows);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", !ctx.hideArrows);
        core/* ɵɵadvance */.xp6(1);
        core/* ɵɵproperty */.Q6J("ngIf", !ctx.hideIndicators);
    } }, directives: [common/* NgForOf */.sg, common/* NgIf */.O5, common/* NgTemplateOutlet */.tP, fesm2015_button/* MatButton */.lW, icon/* MatIcon */.Hw], styles: [".carousel[_ngcontent-%COMP%]{outline:none;overflow:hidden;position:relative;width:100%}.carousel[_ngcontent-%COMP%] > button[_ngcontent-%COMP%]{position:absolute;top:50%;transform:translateY(-50%);z-index:1}.carousel[_ngcontent-%COMP%] > button[_ngcontent-%COMP%]:first-of-type{left:30px}.carousel[_ngcontent-%COMP%] > button[_ngcontent-%COMP%]:last-of-type{right:30px}.carousel-list[_ngcontent-%COMP%]{list-style:none;margin:0;padding:0}.carousel-list[_ngcontent-%COMP%], .carousel-slide[_ngcontent-%COMP%]{display:flex;position:relative;width:100%}.carousel-slide[_ngcontent-%COMP%]{flex-shrink:0;height:0}.carousel-slide[_ngcontent-%COMP%]:hover{cursor:-webkit-grab;cursor:grab}.carousel-indicators[_ngcontent-%COMP%]{bottom:15px;display:flex;left:50%;outline:none;position:absolute;transform:translateX(-50%);z-index:1}.carousel-indicators[_ngcontent-%COMP%] > button[_ngcontent-%COMP%]{height:10px;margin:7.5px;width:10px}"] });
/** @nocollapse */
MatCarouselComponent.ctorParameters = () => [
    { type: animations/* AnimationBuilder */._j },
    { type: core/* Renderer2 */.Qsj },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [core/* PLATFORM_ID */.Lbi,] }] }
];
MatCarouselComponent.propDecorators = {
    timings: [{ type: core/* Input */.IIB }],
    svgIconOverrides: [{ type: core/* Input */.IIB }],
    autoplay: [{ type: core/* Input */.IIB }],
    interval: [{ type: core/* Input */.IIB }],
    loop: [{ type: core/* Input */.IIB }],
    hideArrows: [{ type: core/* Input */.IIB }],
    hideIndicators: [{ type: core/* Input */.IIB }],
    color: [{ type: core/* Input */.IIB }],
    maxWidth: [{ type: core/* Input */.IIB }],
    maintainAspectRatio: [{ type: core/* Input */.IIB }],
    proportion: [{ type: core/* Input */.IIB }],
    slideHeight: [{ type: core/* Input */.IIB }],
    slides: [{ type: core/* Input */.IIB }],
    useKeyboard: [{ type: core/* Input */.IIB }],
    useMouseWheel: [{ type: core/* Input */.IIB }],
    orientation: [{ type: core/* Input */.IIB }],
    change: [{ type: core/* Output */.r_U }],
    slidesList: [{ type: core/* ContentChildren */.AcB, args: [MatCarouselSlideComponent,] }],
    carouselContainer: [{ type: core/* ViewChild */.i9L, args: ['carouselContainer',] }],
    carouselList: [{ type: core/* ViewChild */.i9L, args: ['carouselList',] }],
    onKeyUp: [{ type: core/* HostListener */.L6J, args: ['keyup', ['$event'],] }],
    onMouseEnter: [{ type: core/* HostListener */.L6J, args: ['mouseenter',] }],
    onMouseLeave: [{ type: core/* HostListener */.L6J, args: ['mouseleave',] }],
    onMouseWheel: [{ type: core/* HostListener */.L6J, args: ['mousewheel', ['$event'],] }],
    onResize: [{ type: core/* HostListener */.L6J, args: ['window:resize', ['$event'],] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCarouselComponent, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-carousel',
                template: "<div\r\n  #carouselContainer\r\n  class=\"carousel\"\r\n  tabindex=\"0\"\r\n  [style.max-width]=\"maxWidth\"\r\n  [style.height]=\"!maintainAspectRatio ? '100%' : 'auto'\"\r\n>\r\n  <ul\r\n    #carouselList\r\n    class=\"carousel-list\"\r\n    role=\"listbox\"\r\n    [style.flex-direction]=\"orientation === 'rtl' ? 'row-reverse' : 'row'\"\r\n    [style.height]=\"!maintainAspectRatio ? '100%' : 'auto'\"\r\n  >\r\n    <li\r\n      #carouselSlide\r\n      *ngFor=\"let slide of slidesList\"\r\n      class=\"carousel-slide\"\r\n      role=\"option\"\r\n      [style.padding-bottom]=\"maintainAspectRatio && proportion ? proportion + '%': '0px'\"\r\n      [style.height]=\"!maintainAspectRatio && slideHeight ? slideHeight : '0px'\"\r\n      (panleft)=\"onPan($event, carouselSlide)\"\r\n      (panright)=\"onPan($event, carouselSlide)\"\r\n      (panend)=\"onPanEnd($event, carouselSlide)\"\r\n      (pancancel)=\"onPanEnd($event, carouselSlide)\"\r\n    >\r\n      <ng-container [ngTemplateOutlet]=\"slide.templateRef\"></ng-container>\r\n    </li>\r\n  </ul>\r\n\r\n  <button\r\n    *ngIf=\"!hideArrows\"\r\n    mat-icon-button\r\n    type=\"button\"\r\n    tabindex=\"-1\"\r\n    [color]=\"color\"\r\n    [disabled]=\"!loop && currentIndex == 0\"\r\n    (click)=\"previous()\"\r\n  >\r\n    <mat-icon\r\n      *ngIf=\"svgIconOverrides?.arrowBack; else: defaultArrowBack\"\r\n      [svgIcon]=\"svgIconOverrides.arrowBack\"\r\n    ></mat-icon>\r\n    <ng-template #defaultArrowBack>\r\n      <mat-icon>arrow_back</mat-icon>\r\n    </ng-template>\r\n  </button>\r\n  <button\r\n    *ngIf=\"!hideArrows\"\r\n    mat-icon-button\r\n    type=\"button\"\r\n    tabindex=\"-1\"\r\n    [color]=\"color\"\r\n    [disabled]=\"!loop && currentIndex == slidesList.length - 1\"\r\n    (click)=\"next()\"\r\n  >\r\n    <mat-icon\r\n      *ngIf=\"svgIconOverrides?.arrowForward; else: defaultArrowForward\"\r\n      [svgIcon]=\"svgIconOverrides.arrowForward\"\r\n    ></mat-icon>\r\n    <ng-template #defaultArrowForward>\r\n      <mat-icon>arrow_forward</mat-icon>\r\n    </ng-template>\r\n  </button>\r\n\r\n  <div\r\n    *ngIf=\"!hideIndicators\"\r\n    class=\"carousel-indicators\"\r\n    tabindex=\"-1\"\r\n    [style.flex-direction]=\"orientation === 'rtl' ? 'row-reverse' : 'row'\"\r\n  >\r\n    <button\r\n      *ngFor=\"let slide of slidesList; let i = index\"\r\n      type=\"button\"\r\n      tabindex=\"-1\"\r\n      mat-mini-fab\r\n      [color]=\"color\"\r\n      [disabled]=\"i == currentIndex\"\r\n      (click)=\"slideTo(i)\"\r\n      (focus)=\"carouselContainer.focus()\"\r\n    ></button>\r\n  </div>\r\n</div>\r\n",
                styles: [".carousel{outline:none;overflow:hidden;position:relative;width:100%}.carousel>button{position:absolute;top:50%;transform:translateY(-50%);z-index:1}.carousel>button:first-of-type{left:30px}.carousel>button:last-of-type{right:30px}.carousel-list{list-style:none;margin:0;padding:0}.carousel-list,.carousel-slide{display:flex;position:relative;width:100%}.carousel-slide{flex-shrink:0;height:0}.carousel-slide:hover{cursor:-webkit-grab;cursor:grab}.carousel-indicators{bottom:15px;display:flex;left:50%;outline:none;position:absolute;transform:translateX(-50%);z-index:1}.carousel-indicators>button{height:10px;margin:7.5px;width:10px}"]
            }]
    }], function () { return [{ type: animations/* AnimationBuilder */._j }, { type: core/* Renderer2 */.Qsj }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [core/* PLATFORM_ID */.Lbi]
            }] }]; }, { timings: [{
            type: core/* Input */.IIB
        }], hideArrows: [{
            type: core/* Input */.IIB
        }], hideIndicators: [{
            type: core/* Input */.IIB
        }], color: [{
            type: core/* Input */.IIB
        }], maintainAspectRatio: [{
            type: core/* Input */.IIB
        }], proportion: [{
            type: core/* Input */.IIB
        }], slideHeight: [{
            type: core/* Input */.IIB
        }], useKeyboard: [{
            type: core/* Input */.IIB
        }], useMouseWheel: [{
            type: core/* Input */.IIB
        }], change: [{
            type: core/* Output */.r_U
        }], autoplay: [{
            type: core/* Input */.IIB
        }], interval: [{
            type: core/* Input */.IIB
        }], loop: [{
            type: core/* Input */.IIB
        }], maxWidth: [{
            type: core/* Input */.IIB
        }], slides: [{
            type: core/* Input */.IIB
        }], orientation: [{
            type: core/* Input */.IIB
        }], 
    /**
     * @param {?} event
     * @return {?}
     */
    onKeyUp: [{
            type: core/* HostListener */.L6J,
            args: ['keyup', ['$event']]
        }], 
    /**
     * @return {?}
     */
    onMouseEnter: [{
            type: core/* HostListener */.L6J,
            args: ['mouseenter']
        }], 
    /**
     * @return {?}
     */
    onMouseLeave: [{
            type: core/* HostListener */.L6J,
            args: ['mouseleave']
        }], 
    /**
     * @param {?} event
     * @return {?}
     */
    onMouseWheel: [{
            type: core/* HostListener */.L6J,
            args: ['mousewheel', ['$event']]
        }], 
    /**
     * @param {?} event
     * @return {?}
     */
    onResize: [{
            type: core/* HostListener */.L6J,
            args: ['window:resize', ['$event']]
        }], svgIconOverrides: [{
            type: core/* Input */.IIB
        }], slidesList: [{
            type: core/* ContentChildren */.AcB,
            args: [MatCarouselSlideComponent]
        }], carouselContainer: [{
            type: core/* ViewChild */.i9L,
            args: ['carouselContainer']
        }], carouselList: [{
            type: core/* ViewChild */.i9L,
            args: ['carouselList']
        }] }); })();
if (false) {}

/**
 * @fileoverview added by tsickle
 * Generated from: lib/carousel.module.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
// https://github.com/angular/angular/issues/10541#issuecomment-300761387
class MatCarouselHammerConfig extends platform_browser/* HammerGestureConfig */.hm {
    constructor() {
        super(...arguments);
        this.overrides = {
            pinch: { enable: false },
            rotate: { enable: false }
        };
    }
}
MatCarouselHammerConfig.ɵfac = /*@__PURE__*/ function () { let ɵMatCarouselHammerConfig_BaseFactory; return function MatCarouselHammerConfig_Factory(t) { return (ɵMatCarouselHammerConfig_BaseFactory || (ɵMatCarouselHammerConfig_BaseFactory = core/* ɵɵgetInheritedFactory */.n5z(MatCarouselHammerConfig)))(t || MatCarouselHammerConfig); }; }();
MatCarouselHammerConfig.ɵprov = /*@__PURE__*/ core/* ɵɵdefineInjectable */.Yz7({ token: MatCarouselHammerConfig, factory: MatCarouselHammerConfig.ɵfac });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCarouselHammerConfig, [{
        type: core/* Injectable */.GSi
    }], null, null); })();
if (false) {}
class MatCarouselModule {
    /**
     * @return {?}
     */
    static forRoot() {
        return {
            ngModule: MatCarouselModule,
            providers: [
                { provide: platform_browser/* HAMMER_GESTURE_CONFIG */.ok, useClass: MatCarouselHammerConfig }
            ]
        };
    }
}
MatCarouselModule.ɵfac = function MatCarouselModule_Factory(t) { return new (t || MatCarouselModule)(); };
MatCarouselModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatCarouselModule });
MatCarouselModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[common/* CommonModule */.ez, fesm2015_button/* MatButtonModule */.ot, icon/* MatIconModule */.Ps, platform_browser/* HammerModule */.t6]] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatCarouselModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                declarations: [MatCarouselComponent, MatCarouselSlideComponent],
                imports: [common/* CommonModule */.ez, fesm2015_button/* MatButtonModule */.ot, icon/* MatIconModule */.Ps, platform_browser/* HammerModule */.t6],
                exports: [MatCarouselComponent, MatCarouselSlideComponent]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatCarouselModule, { declarations: function () { return [MatCarouselComponent, MatCarouselSlideComponent]; }, imports: function () { return [common/* CommonModule */.ez, fesm2015_button/* MatButtonModule */.ot, icon/* MatIconModule */.Ps, platform_browser/* HammerModule */.t6]; }, exports: function () { return [MatCarouselComponent, MatCarouselSlideComponent]; } }); })();

/**
 * @fileoverview added by tsickle
 * Generated from: lib/carousel.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @record
 */
function MatCarousel() { }
if (false) {}
/**
 * @record
 */
function SvgIconOverrides() { }
if (false) {}

/**
 * @fileoverview added by tsickle
 * Generated from: lib/carousel-slide/carousel-slide.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @record
 */
function MatCarouselSlide() { }
if (false) {}

/**
 * @fileoverview added by tsickle
 * Generated from: public_api.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */

/**
 * @fileoverview added by tsickle
 * Generated from: ngbmodule-material-carousel.ts
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingRequire,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */



//# sourceMappingURL=ngbmodule-material-carousel.js.map
// EXTERNAL MODULE: ./node_modules/@angular/cdk/__ivy_ngcc__/fesm2015/layout.js
var layout = __webpack_require__(5072);
;// CONCATENATED MODULE: ./node_modules/@angular/material/__ivy_ngcc__/fesm2015/tooltip.js
















/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/**
 * Animations used by MatTooltip.
 * @docs-private
 */








const matTooltipAnimations = {
    /** Animation that transitions a tooltip in and out. */
    tooltipState: (0,animations/* trigger */.X$)('state', [
        (0,animations/* state */.SB)('initial, void, hidden', (0,animations/* style */.oB)({ opacity: 0, transform: 'scale(0)' })),
        (0,animations/* state */.SB)('visible', (0,animations/* style */.oB)({ transform: 'scale(1)' })),
        (0,animations/* transition */.eR)('* => visible', (0,animations/* animate */.jt)('200ms cubic-bezier(0, 0, 0.2, 1)', (0,animations/* keyframes */.F4)([
            (0,animations/* style */.oB)({ opacity: 0, transform: 'scale(0)', offset: 0 }),
            (0,animations/* style */.oB)({ opacity: 0.5, transform: 'scale(0.99)', offset: 0.5 }),
            (0,animations/* style */.oB)({ opacity: 1, transform: 'scale(1)', offset: 1 })
        ]))),
        (0,animations/* transition */.eR)('* => hidden', (0,animations/* animate */.jt)('100ms cubic-bezier(0, 0, 0.2, 1)', (0,animations/* style */.oB)({ opacity: 0 }))),
    ])
};

/** Time in ms to throttle repositioning after scroll events. */
const SCROLL_THROTTLE_MS = 20;
/**
 * CSS class that will be attached to the overlay panel.
 * @deprecated
 * @breaking-change 13.0.0 remove this variable
 */
const TOOLTIP_PANEL_CLASS = 'mat-tooltip-panel';
const PANEL_CLASS = 'tooltip-panel';
/** Options used to bind passive event listeners. */
const passiveListenerOptions = (0,platform/* normalizePassiveListenerOptions */.i$)({ passive: true });
/**
 * Time between the user putting the pointer on a tooltip
 * trigger and the long press event being fired.
 */
const LONGPRESS_DELAY = 500;
/**
 * Creates an error to be thrown if the user supplied an invalid tooltip position.
 * @docs-private
 */
function getMatTooltipInvalidPositionError(position) {
    return Error(`Tooltip position "${position}" is invalid.`);
}
/** Injection token that determines the scroll handling while a tooltip is visible. */
const MAT_TOOLTIP_SCROLL_STRATEGY = new core/* InjectionToken */.OlP('mat-tooltip-scroll-strategy');
/** @docs-private */
function MAT_TOOLTIP_SCROLL_STRATEGY_FACTORY(overlay) {
    return () => overlay.scrollStrategies.reposition({ scrollThrottle: SCROLL_THROTTLE_MS });
}
/** @docs-private */
const MAT_TOOLTIP_SCROLL_STRATEGY_FACTORY_PROVIDER = {
    provide: MAT_TOOLTIP_SCROLL_STRATEGY,
    deps: [overlay/* Overlay */.aV],
    useFactory: MAT_TOOLTIP_SCROLL_STRATEGY_FACTORY,
};
/** Injection token to be used to override the default options for `matTooltip`. */
const MAT_TOOLTIP_DEFAULT_OPTIONS = new core/* InjectionToken */.OlP('mat-tooltip-default-options', {
    providedIn: 'root',
    factory: MAT_TOOLTIP_DEFAULT_OPTIONS_FACTORY
});
/** @docs-private */
function MAT_TOOLTIP_DEFAULT_OPTIONS_FACTORY() {
    return {
        showDelay: 0,
        hideDelay: 0,
        touchendHideDelay: 1500,
    };
}
class _MatTooltipBase {
    constructor(_overlay, _elementRef, _scrollDispatcher, _viewContainerRef, _ngZone, _platform, _ariaDescriber, _focusMonitor, scrollStrategy, _dir, _defaultOptions, _document) {
        this._overlay = _overlay;
        this._elementRef = _elementRef;
        this._scrollDispatcher = _scrollDispatcher;
        this._viewContainerRef = _viewContainerRef;
        this._ngZone = _ngZone;
        this._platform = _platform;
        this._ariaDescriber = _ariaDescriber;
        this._focusMonitor = _focusMonitor;
        this._dir = _dir;
        this._defaultOptions = _defaultOptions;
        this._position = 'below';
        this._disabled = false;
        this._viewInitialized = false;
        this._pointerExitEventsInitialized = false;
        this._viewportMargin = 8;
        this._cssClassPrefix = 'mat';
        /** The default delay in ms before showing the tooltip after show is called */
        this.showDelay = this._defaultOptions.showDelay;
        /** The default delay in ms before hiding the tooltip after hide is called */
        this.hideDelay = this._defaultOptions.hideDelay;
        /**
         * How touch gestures should be handled by the tooltip. On touch devices the tooltip directive
         * uses a long press gesture to show and hide, however it can conflict with the native browser
         * gestures. To work around the conflict, Angular Material disables native gestures on the
         * trigger, but that might not be desirable on particular elements (e.g. inputs and draggable
         * elements). The different values for this option configure the touch event handling as follows:
         * - `auto` - Enables touch gestures for all elements, but tries to avoid conflicts with native
         *   browser gestures on particular elements. In particular, it allows text selection on inputs
         *   and textareas, and preserves the native browser dragging on elements marked as `draggable`.
         * - `on` - Enables touch gestures for all elements and disables native
         *   browser gestures with no exceptions.
         * - `off` - Disables touch gestures. Note that this will prevent the tooltip from
         *   showing on touch devices.
         */
        this.touchGestures = 'auto';
        this._message = '';
        /** Manually-bound passive event listeners. */
        this._passiveListeners = [];
        /** Emits when the component is destroyed. */
        this._destroyed = new Subject/* Subject */.xQ();
        /**
         * Handles the keydown events on the host element.
         * Needs to be an arrow function so that we can use it in addEventListener.
         */
        this._handleKeydown = (event) => {
            if (this._isTooltipVisible() && event.keyCode === keycodes/* ESCAPE */.hY && !(0,keycodes/* hasModifierKey */.Vb)(event)) {
                event.preventDefault();
                event.stopPropagation();
                this._ngZone.run(() => this.hide(0));
            }
        };
        this._scrollStrategy = scrollStrategy;
        this._document = _document;
        if (_defaultOptions) {
            if (_defaultOptions.position) {
                this.position = _defaultOptions.position;
            }
            if (_defaultOptions.touchGestures) {
                this.touchGestures = _defaultOptions.touchGestures;
            }
        }
        _dir.change.pipe((0,takeUntil/* takeUntil */.R)(this._destroyed)).subscribe(() => {
            if (this._overlayRef) {
                this._updatePosition(this._overlayRef);
            }
        });
        _ngZone.runOutsideAngular(() => {
            _elementRef.nativeElement.addEventListener('keydown', this._handleKeydown);
        });
    }
    /** Allows the user to define the position of the tooltip relative to the parent element */
    get position() { return this._position; }
    set position(value) {
        var _a;
        if (value !== this._position) {
            this._position = value;
            if (this._overlayRef) {
                this._updatePosition(this._overlayRef);
                (_a = this._tooltipInstance) === null || _a === void 0 ? void 0 : _a.show(0);
                this._overlayRef.updatePosition();
            }
        }
    }
    /** Disables the display of the tooltip. */
    get disabled() { return this._disabled; }
    set disabled(value) {
        this._disabled = (0,coercion/* coerceBooleanProperty */.Ig)(value);
        // If tooltip is disabled, hide immediately.
        if (this._disabled) {
            this.hide(0);
        }
        else {
            this._setupPointerEnterEventsIfNeeded();
        }
    }
    /** The message to be displayed in the tooltip */
    get message() { return this._message; }
    set message(value) {
        this._ariaDescriber.removeDescription(this._elementRef.nativeElement, this._message, 'tooltip');
        // If the message is not a string (e.g. number), convert it to a string and trim it.
        // Must convert with `String(value)`, not `${value}`, otherwise Closure Compiler optimises
        // away the string-conversion: https://github.com/angular/components/issues/20684
        this._message = value != null ? String(value).trim() : '';
        if (!this._message && this._isTooltipVisible()) {
            this.hide(0);
        }
        else {
            this._setupPointerEnterEventsIfNeeded();
            this._updateTooltipMessage();
            this._ngZone.runOutsideAngular(() => {
                // The `AriaDescriber` has some functionality that avoids adding a description if it's the
                // same as the `aria-label` of an element, however we can't know whether the tooltip trigger
                // has a data-bound `aria-label` or when it'll be set for the first time. We can avoid the
                // issue by deferring the description by a tick so Angular has time to set the `aria-label`.
                Promise.resolve().then(() => {
                    this._ariaDescriber.describe(this._elementRef.nativeElement, this.message, 'tooltip');
                });
            });
        }
    }
    /** Classes to be passed to the tooltip. Supports the same syntax as `ngClass`. */
    get tooltipClass() { return this._tooltipClass; }
    set tooltipClass(value) {
        this._tooltipClass = value;
        if (this._tooltipInstance) {
            this._setTooltipClass(this._tooltipClass);
        }
    }
    ngAfterViewInit() {
        // This needs to happen after view init so the initial values for all inputs have been set.
        this._viewInitialized = true;
        this._setupPointerEnterEventsIfNeeded();
        this._focusMonitor.monitor(this._elementRef)
            .pipe((0,takeUntil/* takeUntil */.R)(this._destroyed))
            .subscribe(origin => {
            // Note that the focus monitor runs outside the Angular zone.
            if (!origin) {
                this._ngZone.run(() => this.hide(0));
            }
            else if (origin === 'keyboard') {
                this._ngZone.run(() => this.show());
            }
        });
    }
    /**
     * Dispose the tooltip when destroyed.
     */
    ngOnDestroy() {
        const nativeElement = this._elementRef.nativeElement;
        clearTimeout(this._touchstartTimeout);
        if (this._overlayRef) {
            this._overlayRef.dispose();
            this._tooltipInstance = null;
        }
        // Clean up the event listeners set in the constructor
        nativeElement.removeEventListener('keydown', this._handleKeydown);
        this._passiveListeners.forEach(([event, listener]) => {
            nativeElement.removeEventListener(event, listener, passiveListenerOptions);
        });
        this._passiveListeners.length = 0;
        this._destroyed.next();
        this._destroyed.complete();
        this._ariaDescriber.removeDescription(nativeElement, this.message, 'tooltip');
        this._focusMonitor.stopMonitoring(nativeElement);
    }
    /** Shows the tooltip after the delay in ms, defaults to tooltip-delay-show or 0ms if no input */
    show(delay = this.showDelay) {
        if (this.disabled || !this.message || (this._isTooltipVisible() &&
            !this._tooltipInstance._showTimeoutId && !this._tooltipInstance._hideTimeoutId)) {
            return;
        }
        const overlayRef = this._createOverlay();
        this._detach();
        this._portal = this._portal ||
            new portal/* ComponentPortal */.C5(this._tooltipComponent, this._viewContainerRef);
        this._tooltipInstance = overlayRef.attach(this._portal).instance;
        this._tooltipInstance.afterHidden()
            .pipe((0,takeUntil/* takeUntil */.R)(this._destroyed))
            .subscribe(() => this._detach());
        this._setTooltipClass(this._tooltipClass);
        this._updateTooltipMessage();
        this._tooltipInstance.show(delay);
    }
    /** Hides the tooltip after the delay in ms, defaults to tooltip-delay-hide or 0ms if no input */
    hide(delay = this.hideDelay) {
        if (this._tooltipInstance) {
            this._tooltipInstance.hide(delay);
        }
    }
    /** Shows/hides the tooltip */
    toggle() {
        this._isTooltipVisible() ? this.hide() : this.show();
    }
    /** Returns true if the tooltip is currently visible to the user */
    _isTooltipVisible() {
        return !!this._tooltipInstance && this._tooltipInstance.isVisible();
    }
    /** Create the overlay config and position strategy */
    _createOverlay() {
        if (this._overlayRef) {
            return this._overlayRef;
        }
        const scrollableAncestors = this._scrollDispatcher.getAncestorScrollContainers(this._elementRef);
        // Create connected position strategy that listens for scroll events to reposition.
        const strategy = this._overlay.position()
            .flexibleConnectedTo(this._elementRef)
            .withTransformOriginOn(`.${this._cssClassPrefix}-tooltip`)
            .withFlexibleDimensions(false)
            .withViewportMargin(this._viewportMargin)
            .withScrollableContainers(scrollableAncestors);
        strategy.positionChanges.pipe((0,takeUntil/* takeUntil */.R)(this._destroyed)).subscribe(change => {
            this._updateCurrentPositionClass(change.connectionPair);
            if (this._tooltipInstance) {
                if (change.scrollableViewProperties.isOverlayClipped && this._tooltipInstance.isVisible()) {
                    // After position changes occur and the overlay is clipped by
                    // a parent scrollable then close the tooltip.
                    this._ngZone.run(() => this.hide(0));
                }
            }
        });
        this._overlayRef = this._overlay.create({
            direction: this._dir,
            positionStrategy: strategy,
            panelClass: `${this._cssClassPrefix}-${PANEL_CLASS}`,
            scrollStrategy: this._scrollStrategy()
        });
        this._updatePosition(this._overlayRef);
        this._overlayRef.detachments()
            .pipe((0,takeUntil/* takeUntil */.R)(this._destroyed))
            .subscribe(() => this._detach());
        this._overlayRef.outsidePointerEvents()
            .pipe((0,takeUntil/* takeUntil */.R)(this._destroyed))
            .subscribe(() => { var _a; return (_a = this._tooltipInstance) === null || _a === void 0 ? void 0 : _a._handleBodyInteraction(); });
        return this._overlayRef;
    }
    /** Detaches the currently-attached tooltip. */
    _detach() {
        if (this._overlayRef && this._overlayRef.hasAttached()) {
            this._overlayRef.detach();
        }
        this._tooltipInstance = null;
    }
    /** Updates the position of the current tooltip. */
    _updatePosition(overlayRef) {
        const position = overlayRef.getConfig().positionStrategy;
        const origin = this._getOrigin();
        const overlay = this._getOverlayPosition();
        position.withPositions([
            this._addOffset(Object.assign(Object.assign({}, origin.main), overlay.main)),
            this._addOffset(Object.assign(Object.assign({}, origin.fallback), overlay.fallback))
        ]);
    }
    /** Adds the configured offset to a position. Used as a hook for child classes. */
    _addOffset(position) {
        return position;
    }
    /**
     * Returns the origin position and a fallback position based on the user's position preference.
     * The fallback position is the inverse of the origin (e.g. `'below' -> 'above'`).
     */
    _getOrigin() {
        const isLtr = !this._dir || this._dir.value == 'ltr';
        const position = this.position;
        let originPosition;
        if (position == 'above' || position == 'below') {
            originPosition = { originX: 'center', originY: position == 'above' ? 'top' : 'bottom' };
        }
        else if (position == 'before' ||
            (position == 'left' && isLtr) ||
            (position == 'right' && !isLtr)) {
            originPosition = { originX: 'start', originY: 'center' };
        }
        else if (position == 'after' ||
            (position == 'right' && isLtr) ||
            (position == 'left' && !isLtr)) {
            originPosition = { originX: 'end', originY: 'center' };
        }
        else if (typeof ngDevMode === 'undefined' || ngDevMode) {
            throw getMatTooltipInvalidPositionError(position);
        }
        const { x, y } = this._invertPosition(originPosition.originX, originPosition.originY);
        return {
            main: originPosition,
            fallback: { originX: x, originY: y }
        };
    }
    /** Returns the overlay position and a fallback position based on the user's preference */
    _getOverlayPosition() {
        const isLtr = !this._dir || this._dir.value == 'ltr';
        const position = this.position;
        let overlayPosition;
        if (position == 'above') {
            overlayPosition = { overlayX: 'center', overlayY: 'bottom' };
        }
        else if (position == 'below') {
            overlayPosition = { overlayX: 'center', overlayY: 'top' };
        }
        else if (position == 'before' ||
            (position == 'left' && isLtr) ||
            (position == 'right' && !isLtr)) {
            overlayPosition = { overlayX: 'end', overlayY: 'center' };
        }
        else if (position == 'after' ||
            (position == 'right' && isLtr) ||
            (position == 'left' && !isLtr)) {
            overlayPosition = { overlayX: 'start', overlayY: 'center' };
        }
        else if (typeof ngDevMode === 'undefined' || ngDevMode) {
            throw getMatTooltipInvalidPositionError(position);
        }
        const { x, y } = this._invertPosition(overlayPosition.overlayX, overlayPosition.overlayY);
        return {
            main: overlayPosition,
            fallback: { overlayX: x, overlayY: y }
        };
    }
    /** Updates the tooltip message and repositions the overlay according to the new message length */
    _updateTooltipMessage() {
        // Must wait for the message to be painted to the tooltip so that the overlay can properly
        // calculate the correct positioning based on the size of the text.
        if (this._tooltipInstance) {
            this._tooltipInstance.message = this.message;
            this._tooltipInstance._markForCheck();
            this._ngZone.onMicrotaskEmpty.pipe((0,take/* take */.q)(1), (0,takeUntil/* takeUntil */.R)(this._destroyed)).subscribe(() => {
                if (this._tooltipInstance) {
                    this._overlayRef.updatePosition();
                }
            });
        }
    }
    /** Updates the tooltip class */
    _setTooltipClass(tooltipClass) {
        if (this._tooltipInstance) {
            this._tooltipInstance.tooltipClass = tooltipClass;
            this._tooltipInstance._markForCheck();
        }
    }
    /** Inverts an overlay position. */
    _invertPosition(x, y) {
        if (this.position === 'above' || this.position === 'below') {
            if (y === 'top') {
                y = 'bottom';
            }
            else if (y === 'bottom') {
                y = 'top';
            }
        }
        else {
            if (x === 'end') {
                x = 'start';
            }
            else if (x === 'start') {
                x = 'end';
            }
        }
        return { x, y };
    }
    /** Updates the class on the overlay panel based on the current position of the tooltip. */
    _updateCurrentPositionClass(connectionPair) {
        const { overlayY, originX, originY } = connectionPair;
        let newPosition;
        // If the overlay is in the middle along the Y axis,
        // it means that it's either before or after.
        if (overlayY === 'center') {
            // Note that since this information is used for styling, we want to
            // resolve `start` and `end` to their real values, otherwise consumers
            // would have to remember to do it themselves on each consumption.
            if (this._dir && this._dir.value === 'rtl') {
                newPosition = originX === 'end' ? 'left' : 'right';
            }
            else {
                newPosition = originX === 'start' ? 'left' : 'right';
            }
        }
        else {
            newPosition = overlayY === 'bottom' && originY === 'top' ? 'above' : 'below';
        }
        if (newPosition !== this._currentPosition) {
            const overlayRef = this._overlayRef;
            if (overlayRef) {
                const classPrefix = `${this._cssClassPrefix}-${PANEL_CLASS}-`;
                overlayRef.removePanelClass(classPrefix + this._currentPosition);
                overlayRef.addPanelClass(classPrefix + newPosition);
            }
            this._currentPosition = newPosition;
        }
    }
    /** Binds the pointer events to the tooltip trigger. */
    _setupPointerEnterEventsIfNeeded() {
        // Optimization: Defer hooking up events if there's no message or the tooltip is disabled.
        if (this._disabled || !this.message || !this._viewInitialized ||
            this._passiveListeners.length) {
            return;
        }
        // The mouse events shouldn't be bound on mobile devices, because they can prevent the
        // first tap from firing its click event or can cause the tooltip to open for clicks.
        if (this._platformSupportsMouseEvents()) {
            this._passiveListeners
                .push(['mouseenter', () => {
                    this._setupPointerExitEventsIfNeeded();
                    this.show();
                }]);
        }
        else if (this.touchGestures !== 'off') {
            this._disableNativeGesturesIfNecessary();
            this._passiveListeners
                .push(['touchstart', () => {
                    // Note that it's important that we don't `preventDefault` here,
                    // because it can prevent click events from firing on the element.
                    this._setupPointerExitEventsIfNeeded();
                    clearTimeout(this._touchstartTimeout);
                    this._touchstartTimeout = setTimeout(() => this.show(), LONGPRESS_DELAY);
                }]);
        }
        this._addListeners(this._passiveListeners);
    }
    _setupPointerExitEventsIfNeeded() {
        if (this._pointerExitEventsInitialized) {
            return;
        }
        this._pointerExitEventsInitialized = true;
        const exitListeners = [];
        if (this._platformSupportsMouseEvents()) {
            exitListeners.push(['mouseleave', () => this.hide()], ['wheel', event => this._wheelListener(event)]);
        }
        else if (this.touchGestures !== 'off') {
            this._disableNativeGesturesIfNecessary();
            const touchendListener = () => {
                clearTimeout(this._touchstartTimeout);
                this.hide(this._defaultOptions.touchendHideDelay);
            };
            exitListeners.push(['touchend', touchendListener], ['touchcancel', touchendListener]);
        }
        this._addListeners(exitListeners);
        this._passiveListeners.push(...exitListeners);
    }
    _addListeners(listeners) {
        listeners.forEach(([event, listener]) => {
            this._elementRef.nativeElement.addEventListener(event, listener, passiveListenerOptions);
        });
    }
    _platformSupportsMouseEvents() {
        return !this._platform.IOS && !this._platform.ANDROID;
    }
    /** Listener for the `wheel` event on the element. */
    _wheelListener(event) {
        if (this._isTooltipVisible()) {
            const elementUnderPointer = this._document.elementFromPoint(event.clientX, event.clientY);
            const element = this._elementRef.nativeElement;
            // On non-touch devices we depend on the `mouseleave` event to close the tooltip, but it
            // won't fire if the user scrolls away using the wheel without moving their cursor. We
            // work around it by finding the element under the user's cursor and closing the tooltip
            // if it's not the trigger.
            if (elementUnderPointer !== element && !element.contains(elementUnderPointer)) {
                this.hide();
            }
        }
    }
    /** Disables the native browser gestures, based on how the tooltip has been configured. */
    _disableNativeGesturesIfNecessary() {
        const gestures = this.touchGestures;
        if (gestures !== 'off') {
            const element = this._elementRef.nativeElement;
            const style = element.style;
            // If gestures are set to `auto`, we don't disable text selection on inputs and
            // textareas, because it prevents the user from typing into them on iOS Safari.
            if (gestures === 'on' || (element.nodeName !== 'INPUT' && element.nodeName !== 'TEXTAREA')) {
                style.userSelect = style.msUserSelect = style.webkitUserSelect =
                    style.MozUserSelect = 'none';
            }
            // If we have `auto` gestures and the element uses native HTML dragging,
            // we don't set `-webkit-user-drag` because it prevents the native behavior.
            if (gestures === 'on' || !element.draggable) {
                style.webkitUserDrag = 'none';
            }
            style.touchAction = 'none';
            style.webkitTapHighlightColor = 'transparent';
        }
    }
}
_MatTooltipBase.ɵfac = function _MatTooltipBase_Factory(t) { return new (t || _MatTooltipBase)(core/* ɵɵdirectiveInject */.Y36(overlay/* Overlay */.aV), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(scrolling/* ScrollDispatcher */.mF), core/* ɵɵdirectiveInject */.Y36(core/* ViewContainerRef */.s_b), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(platform/* Platform */.t4), core/* ɵɵdirectiveInject */.Y36(a11y/* AriaDescriber */.$s), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusMonitor */.tE), core/* ɵɵdirectiveInject */.Y36(undefined), core/* ɵɵdirectiveInject */.Y36(bidi/* Directionality */.Is), core/* ɵɵdirectiveInject */.Y36(undefined), core/* ɵɵdirectiveInject */.Y36(common/* DOCUMENT */.K0)); };
_MatTooltipBase.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: _MatTooltipBase, inputs: { showDelay: ["matTooltipShowDelay", "showDelay"], hideDelay: ["matTooltipHideDelay", "hideDelay"], touchGestures: ["matTooltipTouchGestures", "touchGestures"], position: ["matTooltipPosition", "position"], disabled: ["matTooltipDisabled", "disabled"], message: ["matTooltip", "message"], tooltipClass: ["matTooltipClass", "tooltipClass"] } });
_MatTooltipBase.ctorParameters = () => [
    { type: overlay/* Overlay */.aV },
    { type: core/* ElementRef */.SBq },
    { type: scrolling/* ScrollDispatcher */.mF },
    { type: core/* ViewContainerRef */.s_b },
    { type: core/* NgZone */.R0b },
    { type: platform/* Platform */.t4 },
    { type: a11y/* AriaDescriber */.$s },
    { type: a11y/* FocusMonitor */.tE },
    { type: undefined },
    { type: bidi/* Directionality */.Is },
    { type: undefined },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [common/* DOCUMENT */.K0,] }] }
];
_MatTooltipBase.propDecorators = {
    position: [{ type: core/* Input */.IIB, args: ['matTooltipPosition',] }],
    disabled: [{ type: core/* Input */.IIB, args: ['matTooltipDisabled',] }],
    showDelay: [{ type: core/* Input */.IIB, args: ['matTooltipShowDelay',] }],
    hideDelay: [{ type: core/* Input */.IIB, args: ['matTooltipHideDelay',] }],
    touchGestures: [{ type: core/* Input */.IIB, args: ['matTooltipTouchGestures',] }],
    message: [{ type: core/* Input */.IIB, args: ['matTooltip',] }],
    tooltipClass: [{ type: core/* Input */.IIB, args: ['matTooltipClass',] }]
};
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(_MatTooltipBase, [{
        type: core/* Directive */.Xek
    }], function () { return [{ type: overlay/* Overlay */.aV }, { type: core/* ElementRef */.SBq }, { type: scrolling/* ScrollDispatcher */.mF }, { type: core/* ViewContainerRef */.s_b }, { type: core/* NgZone */.R0b }, { type: platform/* Platform */.t4 }, { type: a11y/* AriaDescriber */.$s }, { type: a11y/* FocusMonitor */.tE }, { type: undefined }, { type: bidi/* Directionality */.Is }, { type: undefined }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [common/* DOCUMENT */.K0]
            }] }]; }, { showDelay: [{
            type: core/* Input */.IIB,
            args: ['matTooltipShowDelay']
        }], hideDelay: [{
            type: core/* Input */.IIB,
            args: ['matTooltipHideDelay']
        }], touchGestures: [{
            type: core/* Input */.IIB,
            args: ['matTooltipTouchGestures']
        }], position: [{
            type: core/* Input */.IIB,
            args: ['matTooltipPosition']
        }], disabled: [{
            type: core/* Input */.IIB,
            args: ['matTooltipDisabled']
        }], message: [{
            type: core/* Input */.IIB,
            args: ['matTooltip']
        }], tooltipClass: [{
            type: core/* Input */.IIB,
            args: ['matTooltipClass']
        }] }); })();
/**
 * Directive that attaches a material design tooltip to the host element. Animates the showing and
 * hiding of a tooltip provided position (defaults to below the element).
 *
 * https://material.io/design/components/tooltips.html
 */
class MatTooltip extends _MatTooltipBase {
    constructor(overlay, elementRef, scrollDispatcher, viewContainerRef, ngZone, platform, ariaDescriber, focusMonitor, scrollStrategy, dir, defaultOptions, _document) {
        super(overlay, elementRef, scrollDispatcher, viewContainerRef, ngZone, platform, ariaDescriber, focusMonitor, scrollStrategy, dir, defaultOptions, _document);
        this._tooltipComponent = TooltipComponent;
    }
}
MatTooltip.ɵfac = function MatTooltip_Factory(t) { return new (t || MatTooltip)(core/* ɵɵdirectiveInject */.Y36(overlay/* Overlay */.aV), core/* ɵɵdirectiveInject */.Y36(core/* ElementRef */.SBq), core/* ɵɵdirectiveInject */.Y36(scrolling/* ScrollDispatcher */.mF), core/* ɵɵdirectiveInject */.Y36(core/* ViewContainerRef */.s_b), core/* ɵɵdirectiveInject */.Y36(core/* NgZone */.R0b), core/* ɵɵdirectiveInject */.Y36(platform/* Platform */.t4), core/* ɵɵdirectiveInject */.Y36(a11y/* AriaDescriber */.$s), core/* ɵɵdirectiveInject */.Y36(a11y/* FocusMonitor */.tE), core/* ɵɵdirectiveInject */.Y36(MAT_TOOLTIP_SCROLL_STRATEGY), core/* ɵɵdirectiveInject */.Y36(bidi/* Directionality */.Is, 8), core/* ɵɵdirectiveInject */.Y36(MAT_TOOLTIP_DEFAULT_OPTIONS, 8), core/* ɵɵdirectiveInject */.Y36(common/* DOCUMENT */.K0)); };
MatTooltip.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: MatTooltip, selectors: [["", "matTooltip", ""]], hostAttrs: [1, "mat-tooltip-trigger"], exportAs: ["matTooltip"], features: [core/* ɵɵInheritDefinitionFeature */.qOj] });
MatTooltip.ctorParameters = () => [
    { type: overlay/* Overlay */.aV },
    { type: core/* ElementRef */.SBq },
    { type: scrolling/* ScrollDispatcher */.mF },
    { type: core/* ViewContainerRef */.s_b },
    { type: core/* NgZone */.R0b },
    { type: platform/* Platform */.t4 },
    { type: a11y/* AriaDescriber */.$s },
    { type: a11y/* FocusMonitor */.tE },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [MAT_TOOLTIP_SCROLL_STRATEGY,] }] },
    { type: bidi/* Directionality */.Is, decorators: [{ type: core/* Optional */.FiY }] },
    { type: undefined, decorators: [{ type: core/* Optional */.FiY }, { type: core/* Inject */.tBr, args: [MAT_TOOLTIP_DEFAULT_OPTIONS,] }] },
    { type: undefined, decorators: [{ type: core/* Inject */.tBr, args: [common/* DOCUMENT */.K0,] }] }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatTooltip, [{
        type: core/* Directive */.Xek,
        args: [{
                selector: '[matTooltip]',
                exportAs: 'matTooltip',
                host: {
                    'class': 'mat-tooltip-trigger'
                }
            }]
    }], function () { return [{ type: overlay/* Overlay */.aV }, { type: core/* ElementRef */.SBq }, { type: scrolling/* ScrollDispatcher */.mF }, { type: core/* ViewContainerRef */.s_b }, { type: core/* NgZone */.R0b }, { type: platform/* Platform */.t4 }, { type: a11y/* AriaDescriber */.$s }, { type: a11y/* FocusMonitor */.tE }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [MAT_TOOLTIP_SCROLL_STRATEGY]
            }] }, { type: bidi/* Directionality */.Is, decorators: [{
                type: core/* Optional */.FiY
            }] }, { type: undefined, decorators: [{
                type: core/* Optional */.FiY
            }, {
                type: core/* Inject */.tBr,
                args: [MAT_TOOLTIP_DEFAULT_OPTIONS]
            }] }, { type: undefined, decorators: [{
                type: core/* Inject */.tBr,
                args: [common/* DOCUMENT */.K0]
            }] }]; }, null); })();
class _TooltipComponentBase {
    constructor(_changeDetectorRef) {
        this._changeDetectorRef = _changeDetectorRef;
        /** Property watched by the animation framework to show or hide the tooltip */
        this._visibility = 'initial';
        /** Whether interactions on the page should close the tooltip */
        this._closeOnInteraction = false;
        /** Subject for notifying that the tooltip has been hidden from the view */
        this._onHide = new Subject/* Subject */.xQ();
    }
    /**
     * Shows the tooltip with an animation originating from the provided origin
     * @param delay Amount of milliseconds to the delay showing the tooltip.
     */
    show(delay) {
        // Cancel the delayed hide if it is scheduled
        clearTimeout(this._hideTimeoutId);
        // Body interactions should cancel the tooltip if there is a delay in showing.
        this._closeOnInteraction = true;
        this._showTimeoutId = setTimeout(() => {
            this._visibility = 'visible';
            this._showTimeoutId = undefined;
            this._onShow();
            // Mark for check so if any parent component has set the
            // ChangeDetectionStrategy to OnPush it will be checked anyways
            this._markForCheck();
        }, delay);
    }
    /**
     * Begins the animation to hide the tooltip after the provided delay in ms.
     * @param delay Amount of milliseconds to delay showing the tooltip.
     */
    hide(delay) {
        // Cancel the delayed show if it is scheduled
        clearTimeout(this._showTimeoutId);
        this._hideTimeoutId = setTimeout(() => {
            this._visibility = 'hidden';
            this._hideTimeoutId = undefined;
            // Mark for check so if any parent component has set the
            // ChangeDetectionStrategy to OnPush it will be checked anyways
            this._markForCheck();
        }, delay);
    }
    /** Returns an observable that notifies when the tooltip has been hidden from view. */
    afterHidden() {
        return this._onHide;
    }
    /** Whether the tooltip is being displayed. */
    isVisible() {
        return this._visibility === 'visible';
    }
    ngOnDestroy() {
        clearTimeout(this._showTimeoutId);
        clearTimeout(this._hideTimeoutId);
        this._onHide.complete();
    }
    _animationStart() {
        this._closeOnInteraction = false;
    }
    _animationDone(event) {
        const toState = event.toState;
        if (toState === 'hidden' && !this.isVisible()) {
            this._onHide.next();
        }
        if (toState === 'visible' || toState === 'hidden') {
            this._closeOnInteraction = true;
        }
    }
    /**
     * Interactions on the HTML body should close the tooltip immediately as defined in the
     * material design spec.
     * https://material.io/design/components/tooltips.html#behavior
     */
    _handleBodyInteraction() {
        if (this._closeOnInteraction) {
            this.hide(0);
        }
    }
    /**
     * Marks that the tooltip needs to be checked in the next change detection run.
     * Mainly used for rendering the initial text before positioning a tooltip, which
     * can be problematic in components with OnPush change detection.
     */
    _markForCheck() {
        this._changeDetectorRef.markForCheck();
    }
    /**
     * Callback for when the timeout in this.show() gets completed.
     * This method is only needed by the mdc-tooltip, and so it is only implemented
     * in the mdc-tooltip, not here.
     */
    _onShow() { }
}
_TooltipComponentBase.ɵfac = function _TooltipComponentBase_Factory(t) { return new (t || _TooltipComponentBase)(core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO)); };
_TooltipComponentBase.ɵdir = /*@__PURE__*/ core/* ɵɵdefineDirective */.lG2({ type: _TooltipComponentBase });
_TooltipComponentBase.ctorParameters = () => [
    { type: core/* ChangeDetectorRef */.sBO }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(_TooltipComponentBase, [{
        type: core/* Directive */.Xek
    }], function () { return [{ type: core/* ChangeDetectorRef */.sBO }]; }, null); })();
/**
 * Internal component that wraps the tooltip's content.
 * @docs-private
 */
class TooltipComponent extends _TooltipComponentBase {
    constructor(changeDetectorRef, _breakpointObserver) {
        super(changeDetectorRef);
        this._breakpointObserver = _breakpointObserver;
        /** Stream that emits whether the user has a handset-sized display.  */
        this._isHandset = this._breakpointObserver.observe(layout/* Breakpoints.Handset */.u3.Handset);
    }
}
TooltipComponent.ɵfac = function TooltipComponent_Factory(t) { return new (t || TooltipComponent)(core/* ɵɵdirectiveInject */.Y36(core/* ChangeDetectorRef */.sBO), core/* ɵɵdirectiveInject */.Y36(layout/* BreakpointObserver */.Yg)); };
TooltipComponent.ɵcmp = /*@__PURE__*/ core/* ɵɵdefineComponent */.Xpm({ type: TooltipComponent, selectors: [["mat-tooltip-component"]], hostAttrs: ["aria-hidden", "true"], hostVars: 2, hostBindings: function TooltipComponent_HostBindings(rf, ctx) { if (rf & 2) {
        core/* ɵɵstyleProp */.Udp("zoom", ctx._visibility === "visible" ? 1 : null);
    } }, features: [core/* ɵɵInheritDefinitionFeature */.qOj], decls: 3, vars: 7, consts: [[1, "mat-tooltip", 3, "ngClass"]], template: function TooltipComponent_Template(rf, ctx) { if (rf & 1) {
        core/* ɵɵelementStart */.TgZ(0, "div", 0);
        core/* ɵɵlistener */.NdJ("@state.start", function TooltipComponent_Template_div_animation_state_start_0_listener() { return ctx._animationStart(); })("@state.done", function TooltipComponent_Template_div_animation_state_done_0_listener($event) { return ctx._animationDone($event); });
        core/* ɵɵpipe */.ALo(1, "async");
        core/* ɵɵtext */._uU(2);
        core/* ɵɵelementEnd */.qZA();
    } if (rf & 2) {
        let tmp_0_0;
        core/* ɵɵclassProp */.ekj("mat-tooltip-handset", (tmp_0_0 = core/* ɵɵpipeBind1 */.lcZ(1, 5, ctx._isHandset)) == null ? null : tmp_0_0.matches);
        core/* ɵɵproperty */.Q6J("ngClass", ctx.tooltipClass)("@state", ctx._visibility);
        core/* ɵɵadvance */.xp6(2);
        core/* ɵɵtextInterpolate */.Oqu(ctx.message);
    } }, directives: [common/* NgClass */.mk], pipes: [common/* AsyncPipe */.Ov], styles: [".mat-tooltip-panel{pointer-events:none !important}.mat-tooltip{color:#fff;border-radius:4px;margin:14px;max-width:250px;padding-left:8px;padding-right:8px;overflow:hidden;text-overflow:ellipsis}.cdk-high-contrast-active .mat-tooltip{outline:solid 1px}.mat-tooltip-handset{margin:24px;padding-left:16px;padding-right:16px}\n"], encapsulation: 2, data: { animation: [matTooltipAnimations.tooltipState] }, changeDetection: 0 });
TooltipComponent.ctorParameters = () => [
    { type: core/* ChangeDetectorRef */.sBO },
    { type: layout/* BreakpointObserver */.Yg }
];
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(TooltipComponent, [{
        type: core/* Component */.wA2,
        args: [{
                selector: 'mat-tooltip-component',
                template: "<div class=\"mat-tooltip\"\n     [ngClass]=\"tooltipClass\"\n     [class.mat-tooltip-handset]=\"(_isHandset | async)?.matches\"\n     [@state]=\"_visibility\"\n     (@state.start)=\"_animationStart()\"\n     (@state.done)=\"_animationDone($event)\">{{message}}</div>\n",
                encapsulation: core/* ViewEncapsulation.None */.ifc.None,
                changeDetection: core/* ChangeDetectionStrategy.OnPush */.n4l.OnPush,
                animations: [matTooltipAnimations.tooltipState],
                host: {
                    // Forces the element to have a layout in IE and Edge. This fixes issues where the element
                    // won't be rendered if the animations are disabled or there is no web animations polyfill.
                    '[style.zoom]': '_visibility === "visible" ? 1 : null',
                    'aria-hidden': 'true'
                },
                styles: [".mat-tooltip-panel{pointer-events:none !important}.mat-tooltip{color:#fff;border-radius:4px;margin:14px;max-width:250px;padding-left:8px;padding-right:8px;overflow:hidden;text-overflow:ellipsis}.cdk-high-contrast-active .mat-tooltip{outline:solid 1px}.mat-tooltip-handset{margin:24px;padding-left:16px;padding-right:16px}\n"]
            }]
    }], function () { return [{ type: core/* ChangeDetectorRef */.sBO }, { type: layout/* BreakpointObserver */.Yg }]; }, null); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
class MatTooltipModule {
}
MatTooltipModule.ɵfac = function MatTooltipModule_Factory(t) { return new (t || MatTooltipModule)(); };
MatTooltipModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: MatTooltipModule });
MatTooltipModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ providers: [MAT_TOOLTIP_SCROLL_STRATEGY_FACTORY_PROVIDER], imports: [[
            a11y/* A11yModule */.rt,
            common/* CommonModule */.ez,
            overlay/* OverlayModule */.U8,
            fesm2015_core/* MatCommonModule */.BQ,
        ], fesm2015_core/* MatCommonModule */.BQ, scrolling/* CdkScrollableModule */.ZD] });
(function () { (typeof ngDevMode === "undefined" || ngDevMode) && core/* ɵsetClassMetadata */.zlt(MatTooltipModule, [{
        type: core/* NgModule */.LVF,
        args: [{
                imports: [
                    a11y/* A11yModule */.rt,
                    common/* CommonModule */.ez,
                    overlay/* OverlayModule */.U8,
                    fesm2015_core/* MatCommonModule */.BQ,
                ],
                exports: [MatTooltip, TooltipComponent, fesm2015_core/* MatCommonModule */.BQ, scrolling/* CdkScrollableModule */.ZD],
                declarations: [MatTooltip, TooltipComponent],
                entryComponents: [TooltipComponent],
                providers: [MAT_TOOLTIP_SCROLL_STRATEGY_FACTORY_PROVIDER]
            }]
    }], null, null); })();
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(MatTooltipModule, { declarations: function () { return [MatTooltip, TooltipComponent]; }, imports: function () { return [a11y/* A11yModule */.rt,
        common/* CommonModule */.ez,
        overlay/* OverlayModule */.U8,
        fesm2015_core/* MatCommonModule */.BQ]; }, exports: function () { return [MatTooltip, TooltipComponent, fesm2015_core/* MatCommonModule */.BQ, scrolling/* CdkScrollableModule */.ZD]; } }); })();

/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

/**
 * Generated bundle index. Do not edit.
 */



//# sourceMappingURL=tooltip.js.map
;// CONCATENATED MODULE: ./src/app/layouts/admin-layout/admin-layout.module.ts















































class AdminLayoutModule {
}
AdminLayoutModule.ɵfac = function AdminLayoutModule_Factory(t) { return new (t || AdminLayoutModule)(); };
AdminLayoutModule.ɵmod = /*@__PURE__*/ core/* ɵɵdefineNgModule */.oAB({ type: AdminLayoutModule, bootstrap: [NgbdCarouselConfig] });
AdminLayoutModule.ɵinj = /*@__PURE__*/ core/* ɵɵdefineInjector */.cJS({ imports: [[
            ng2_adsense/* AdsenseModule.forRoot */.ih.forRoot({
                adClient: 'ca-pub-1302230400221331',
                adSlot: 5686017586,
            }),
            common/* CommonModule */.ez,
            router/* RouterModule.forChild */.Bz.forChild(AdminLayoutRoutes),
            fesm2015_forms/* FormsModule */.u5,
            ng_bootstrap/* NgbModule */.IJ,
            MatCardModule,
            MatButtonToggleModule,
            MatGridListModule, divider/* MatDividerModule */.t, icon/* MatIconModule */.Ps, fesm2015_button/* MatButtonModule */.ot, fesm2015_radio/* MatRadioModule */.Fk,
            ng_apexcharts/* NgApexchartsModule */.X, ngx_countdown/* CountdownModule */.cD, tabs/* MatTabsModule */.Nh, table/* MatTableModule */.p0, sidenav/* MatSidenavModule */.SJ,
            MatFormFieldModule, MatInputModule, InfiniteScrollModule, MatCarouselModule, MatDialogModule, MatTooltipModule,
            MatSortModule, MatSelectModule
        ]] });
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && core/* ɵɵsetNgModuleScope */.kYT(AdminLayoutModule, { declarations: [ContactInfoComponent,
        TypographyComponent,
        IconsComponent,
        ExposureComponent,
        RaceAnalysisComponent,
        UpcomingRaceAnalysisComponent,
        ExposedComponent,
        F1CountdownComponent,
        NotificationsComponent, StandingsComponent, VideostreamsComponent, InstagramFeedComponent,
        NgbdCarouselConfig, InfoDialog, SubmitContentDialog, PoweredByAwsComponent, AwsComponent, ModeratorComponent], imports: [ng2_adsense/* AdsenseModule */.ih, common/* CommonModule */.ez, router/* RouterModule */.Bz, fesm2015_forms/* FormsModule */.u5,
        ng_bootstrap/* NgbModule */.IJ,
        MatCardModule,
        MatButtonToggleModule,
        MatGridListModule, divider/* MatDividerModule */.t, icon/* MatIconModule */.Ps, fesm2015_button/* MatButtonModule */.ot, fesm2015_radio/* MatRadioModule */.Fk,
        ng_apexcharts/* NgApexchartsModule */.X, ngx_countdown/* CountdownModule */.cD, tabs/* MatTabsModule */.Nh, table/* MatTableModule */.p0, sidenav/* MatSidenavModule */.SJ,
        MatFormFieldModule, MatInputModule, InfiniteScrollModule, MatCarouselModule, MatDialogModule, MatTooltipModule,
        MatSortModule, MatSelectModule], exports: [NgbdCarouselConfig] }); })();


/***/ })

}]);