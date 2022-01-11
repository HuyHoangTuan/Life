function updateSliderValue(slider, trigger = null) {
  //left mouse down
  if (window.event.which == 1) {
    var sliderRect = slider.getBoundingClientRect();
    var c = slider.childNodes;
    var filled = c[5];
    var thumb = c[7];
    resetSlider(slider);
    if (slider.classList.contains("horizontal-slider")) {
      var value =
        ((window.event.clientX - sliderRect.left) / sliderRect.width) * 100;
      if (value < 0) value = 0;
      if (value > 100) value = 100;
      filled.style.width = `${value}%`; // = value + "%"
      thumb.style.left = `${value}%`;
    } else {
      var value =
        ((sliderRect.bottom - window.event.clientY) / sliderRect.height) * 100;
      if (value < 0) value = 0;
      if (value > 100) value = 100;
      filled.style.width = `${value}%`;
      thumb.style.left = `${value}%`;
    }
    // slider.setAttribute("value", value);
    if (trigger != null) {
      trigger(value);
    }
  }
}

function stopSlider(slider) {
  var c = slider.childNodes;
  var filled = c[5];
  var thumb = c[7];
  var value =
    (filled.getBoundingClientRect().width /
      slider.getBoundingClientRect().width) *
    100;
  resetSlider(slider);
  filled.style.width = `${value}%`;
  thumb.style.left = `${value}%`;
}

function resetSlider(slider) {
  var c = slider.childNodes;
  var filled = c[5];
  var thumb = c[7];
  filled.removeAttribute("style");
  thumb.removeAttribute("style");
}

function setSliderTime(slider, second) {
  var c = slider.childNodes;
  var filled = c[5];
  var thumb = c[7];
  filled.style.transition = `width ${second}s linear`;
  filled.style.width = "100%";
  thumb.style.transition = `left ${second}s linear`;
  thumb.style.left = "100%";
}
