// Copyright 2019 Google LLC

let elementsToShow;
let scroll;

$(document).ready(function() {
    scroll = window.requestAnimationFrame || function(callback){ window.setTimeout(callback, 1000/60)};
    elementsToShow = document.querySelectorAll('.animate');    
    loop();
    asyncRemovePlaceHolder();
})

function addRandomGreeting() {

    revertToUnchecked();

    const greetings =
        ["\"In a world too often governed by corruption and arrogance, it can be difficult to stay true to one's philosophical and literary principles.\"   -Lemony Snicket, A Series of Unfortunate Events", "\"Did you know that the Sparrow flies south for winter?\"   -Skulduggery Pleasant, Skulduggery Pleasant", "\"Someday in your life, you'll realize there is no turning back. There was never any turning back.\"   -u/JayStar1213"];
    const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  
    let entrances = 
        ["animate__bounceInUp", "animate__bounceInLeft", "animate__bounceInRight", "animate__bounceInDown", "animate__fadeInBottomLeft", "animate__fadeInBottomRight"]
    let entrance = entrances[Math.floor(Math.random() * greetings.length)];

    const quote = document.getElementById('quote');
    quote.className="";
    quote.classList.add('italic');
    quote.classList.add('animate__animated');
    quote.classList.add(entrance);
    quote.classList.add('animate__delay-1s');
    quote.innerText = greeting;
   
}

function asyncRemovePlaceHolder() {
    setTimeout(function(){ 
        $('#placeholder').remove(); 
    }, 2000);
}

function revertToUnchecked() {
    let label = document.getElementById('quote-label');
    label.className= "";
    label.classList= "";
    label.classList.remove('animate__animated');
    label.classList.remove('animate__fadeInDown');
    
    setTimeout(function(){ document.getElementById('btnControl').click(); }, 500);

}

function loop() {
    elementsToShow.forEach(function (element) {
      if (isElementInViewport(element)) {
        element.classList.add('animate__animated');
        if (element.classList.contains('blog-images-row')) {
            element.classList.add('animate__delay-1s');
            if (element.classList.contains('image-row-left')) {
                element.classList.add('animate__fadeInLeft');
            } else if (element.classList.contains('image-row-right')) {
                element.classList.add('animate__fadeInRight');
            } else {
                element.classList.add('animate__fadeInUp');
            }
        } else if (element.classList.contains('delay')) {
            element.classList.add('animate__fadeInDown');
            if (element.classList.contains('1s-delay')) {
                element.classList.add('animate__delay-1s');
            } else if (element.classList.contains('2s-delay')) {
                element.classList.add('animate__delay-2s');
            } else {
                element.classList.add('animate__delay-3s');
            }
        } else if (element.classList.contains('video-container')) {
            element.classList.add('animate__fadeInUp');
        }
        else {
            element.classList.add('animate__fadeInDown');
        }
      }
    });
    scroll(loop);
}

function isElementInViewport(el) {
    // special bonus for those using jQuery
    if (typeof jQuery === "function" && el instanceof jQuery) {
      el = el[0];
    }
    var rect = el.getBoundingClientRect();
    return (
      (rect.top <= 0
        && rect.bottom >= 0)
      ||
      (rect.bottom >= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.top <= (window.innerHeight || document.documentElement.clientHeight))
      ||
      (rect.top >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight))
    );
}
  