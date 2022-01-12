class Switch extends HTMLElement {
	static formAssociated = true;
	static observedAttributes = ["checked"];

	constructor() {
		super();
		let bgElm = document.createElement("input");
		this._internals = this.attachInternals();
		this.addEventListener("click", this._onClick.bind(this));
	}

	_onClick() {
		this.checked = !this.checked;
	}

	_updateRendering() {}
}

class Slider extends HTMLElement {
	static formAssociated = true;
	static observedAttributes = ["checked"];

	constructor() {
		super();
		const shadow = this.attachShadow({ mode: "open" });

        let a = 1;

        this._wrapper = document.createElement("div")
        this._wrapper.className = "wrapper"

        this._track = document.createElement("div")
        this._track.className= "track"

        this._filled = document.createElement("div")
        this._filled.className = "filled"

        this._thumb = document.createElement("div")
        this._thumb.className = "thumb"

		this._inputElm = document.createElement("input");
		this._inputElm.type = "range";
		this._inputElm.setAttribute("min", "0");
		this._inputElm.setAttribute("max", "100");
		this._inputElm.setAttribute("step", ".1");
        this._inputElm.addEventListener("input",() => this._updateValue())

        const style = document.createElement("style")

        style.textContent = `
            
        `
        this._wrapper.appendChild(this._track);
        this._wrapper.appendChild(this._filled);
        this._wrapper.appendChild(this._thumb);
        this._wrapper.appendChild(this._inputElm);

        shadow.appendChild(style)

        const linkElm = document.createElement('link');
        linkElm.setAttribute('rel', 'stylesheet');
        linkElm.setAttribute('href', '/assets/css/components/slider.css');
        
        // Attach the created element to the shadow dom
        shadow.appendChild(linkElm);

        shadow.appendChild(this._wrapper)
		// shadow.appendChild(this._inputElm);
		// this._internals = this.attachInternals();
		// this.addEventListener("click", this._onClick.bind(this));

        console.log(this._inputElm) 
	}

	_onClick() {
		this.checked = !this.checked;
	}

	_updateValue() {
        this._filled.style.width = `${this._inputElm.value}%`;
        this._thumb.style.left = `${this._inputElm.value}%`;
    }   
}

customElements.define("range-slider", Slider);
