class Switch extends HTMLElement {
	static formAssociated = true;
	static observedAttributes = ["checked", "onchange"];

	constructor() {
		super();
		const shadow = this.attachShadow({ mode: "open" });

		this._internals = this.attachInternals();
		this.addEventListener("click", this._onClick.bind(this));

		this._wrapper = document.createElement("div");
		this._wrapper.className = "wrapper";

		this._bgr = document.createElement("div");
		this._bgr.className = "bgr"

		this._thumb = document.createElement("div");
		this._thumb.className = "thumb"

		this._wrapper.appendChild(this._bgr);
		this._wrapper.appendChild(this._thumb);

		const linkElm = document.createElement("link");
		linkElm.setAttribute("rel", "stylesheet");
		linkElm.setAttribute("href", "/assets/css/components/switch.css");

		// Attach the created element to the shadow dom
		shadow.appendChild(linkElm);
		shadow.appendChild(this._wrapper);
	}

	get form() {
		return this._internals.form;
	}
	get name() {
		return this.getAttribute("name");
	}
	get type() {
		return this.localName;
	}

	get checked() {
		return this.hasAttribute("checked");
	}
	set checked(flag) {
		this.toggleAttribute("checked", Boolean(flag));
		this._wrapper.classList.toggle("checked");
	}

	attributeChangedCallback(name, oldValue, newValue) {
		switch (name) {
			case "checked":
				this._internals.setFormValue(this.checked ? 'on' : null);
				break;
			case "onchange":
				this._inputElm.setAttribute(name, newValue);
				break;
			case "oninput":
				this._inputElm.setAttribute(name, newValue);
				break;
		}
	}

	_onClick() {
		this.checked = !this.checked;
	}

	_updateRendering() {}
}

class Slider extends HTMLElement {
	static formAssociated = true;

	constructor() {
		super();
		this._value = 0;
		const shadow = this.attachShadow({ mode: "open" });

		this._wrapper = document.createElement("div");
		this._wrapper.className = "wrapper";

		this._track = document.createElement("div");
		this._track.className = "track";

		this._filled = document.createElement("div");
		this._filled.className = "filled";

		this._thumb = document.createElement("div");
		this._thumb.className = "thumb";

		this._inputElm = document.createElement("input");
		this._inputElm.type = "range";
		this._inputElm.value = 0;
		this._inputElm.setAttribute("min", "0");
		this._inputElm.setAttribute("max", "100");
		this._inputElm.setAttribute("step", ".1");
		this._inputElm.addEventListener("input", () => this._updateValue());

		this._wrapper.appendChild(this._track);
		this._wrapper.appendChild(this._filled);
		this._wrapper.appendChild(this._thumb);
		this._wrapper.appendChild(this._inputElm);

		const linkElm = document.createElement("link");
		linkElm.setAttribute("rel", "stylesheet");
		linkElm.setAttribute("href", "/assets/css/components/slider.css");

		// Attach the created element to the shadow dom
		shadow.appendChild(linkElm);

		shadow.appendChild(this._wrapper);
		// shadow.appendChild(this._inputElm);
		// this._internals = this.attachInternals();
		// this.addEventListener("click", this._onClick.bind(this));

		// console.log(this._inputElm);
	}

	static observedAttributes = ["value", "onchange"];

	attributeChangedCallback(name, oldValue, newValue) {
		switch (name) {
			case "value":
				this.value = newValue;
				break;
			case "onchange":
				this._inputElm.setAttribute(name, newValue);
				break;
			case "oninput":
				this._inputElm.setAttribute(name, newValue);
				break;
		}
	}

	get value() {
		return this._value;
	}

	set value(value) {
		this._value = value;
		this._inputElm.value = value;
		this._filled.style.width = `${value}%`;
		this._thumb.style.left = `${value}%`;
	}

	_onClick() {
		this.checked = !this.checked;
	}

	_updateValue(value) {
		this.value = this._inputElm.value;
	}

	// setAutoSlideTime(t) {
	// 	console.log("Auto-time: " + t);
	// 	this._auto = true;
	// 	this._filled.style.transition = `width ${t}s linear`;
	// 	this._thumb.style.transition = `left ${t}s linear`;
	// }
	// startAutoSlide() {
	// 	this.value = 100;
	// }
	// stopAutoSlide() {}
}

customElements.define("range-slider", Slider);
customElements.define("input-switch", Switch);

class AudioPreview extends HTMLElement {
	static formAssociated = true;
	static observedAttributes = ["src", "origin-src","onchange"];

	constructor() {
		super();
		const shadow = this.attachShadow({ mode: "open" });

		this._internals = this.attachInternals();
		this.addEventListener("click", this._onClick.bind(this));

		this._wrapper = document.createElement("div");
		this._wrapper.className = "wrapper";

		let button = document.createElement("button");
		let label = document.createElement("label");

		let audio = document.createElement("audio");
		audio.onpause = () => {

		}
		audio.onplay = () => {

		}
		audio.ontimeupdate = () => {

		}

		this._audio = audio;

		this._wrapper.appendChild(this._bgr);
		this._wrapper.appendChild(this._label);

		const linkElm = document.createElement("link");
		linkElm.setAttribute("rel", "stylesheet");
		linkElm.setAttribute("href", "/assets/css/components/switch.css");

		// Attach the created element to the shadow dom
		shadow.appendChild(linkElm);
		shadow.appendChild(this._wrapper);
	}

	get form() {
		return this._internals.form;
	}
	get name() {
		return this.getAttribute("name");
	}
	get type() {
		return this.localName;
	}

	get checked() {
		return this.hasAttribute("checked");
	}
	set checked(flag) {
		this.toggleAttribute("checked", Boolean(flag));
		this._wrapper.classList.toggle("checked");
	}

	attributeChangedCallback(name, oldValue, newValue) {
		switch (name) {
			case "checked":
				this._internals.setFormValue(this.checked ? 'on' : null);
				break;
			case "onchange":
				this._inputElm.setAttribute(name, newValue);
				break;
			case "oninput":
				this._inputElm.setAttribute(name, newValue);
				break;
		}
	}

	_onClick() {
		this.checked = !this.checked;
	}

	_updateRendering() {}
}