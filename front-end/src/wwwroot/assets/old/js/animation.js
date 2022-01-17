function generateMovingRect(){
    var rectCount = 40;
    for(let i = 0; i < rectCount; i++){
        var element = document.createElement('div');
        element.className = 'rect';
        var size = Math.random() * 20 + 20;
        element.style.height = `${size}vh`;
        element.style.aspectRatio = '1 / 1';
        var posX = Math.round(Math.random() * 24) * 5 -10;
        var posY = Math.round(Math.random() * 24) * 5 -10;
        element.style.position = 'fixed';
        element.style.top = `${posX}%`;
        element.style.left = `${posY}%`;
        var r = Math.round(128 + Math.random() * 127);
        var g = Math.round(128 + Math.random() * 127);
        var b = Math.round(128 + Math.random() * 127);
        element.style.background = `rgb(${r},${g},${b})`;
        // var radius = Math.random() * 30 + 20;
        // element.style.borderRadius = `${radius}%`;
        var t = 2 + Math.random() * 6;
        element.style.animationDuration = `${t}s`;
        t = 0 + Math.random() * 20;
        element.style.animationDelay = `${t}s`;
        document.body.appendChild(element);
    }
}