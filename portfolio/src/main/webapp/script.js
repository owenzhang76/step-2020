// Copyright 2019 Google LLC

let elementsToShow;
let scroll;
let quotes =
    ["\"In a world too often governed by corruption and arrogance, it can be difficult to stay true to one's philosophical and literary principles.\"   -Lemony Snicket, A Series of Unfortunate Events", "\"Did you know that the Sparrow flies south for winter?\"   -Skulduggery Pleasant, Skulduggery Pleasant", "\"Someday in your life, you'll realize there is no turning back. There was never any turning back.\"   -u/JayStar1213"];
let entrances = 
    ["animate__bounceInUp", "animate__bounceInLeft", "animate__bounceInRight", "animate__bounceInDown", "animate__fadeInBottomLeft", "animate__fadeInBottomRight"];

$(document).ready(function() {
    // scroll here is a fallback function in case some browswers don't support method requestAnimationFrame. 
    scroll = window.requestAnimationFrame || function(callback){ window.setTimeout(callback, 1000/60)};
    // all the elements that require css animation. 
    elementsToShow = document.querySelectorAll('.animate');  

    loop();

    asyncRemovePlaceHolder();
})

/**
 * Get a random quote from the quotes array and gets a random css animation class from the entrances array for the quote to be displayed
 */
function getRandomQuote() {

    // see function doc on line 57
    revertToUnchecked();

    const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  
    let entrance = entrances[Math.floor(Math.random() * greetings.length)];

    
    let quote = document.getElementById('quote');

    // I first clear the quote div's classList in order to assign a random/new css animation from the entrances array. 
    quote.className="";
    quote.classList.add('italic');
    quote.classList.add('animate__animated');
    quote.classList.add(entrance);
    quote.classList.add('animate__delay-1s');

    quote.innerText = greeting;
   
}
/**
 * Removes an empty div with heigh 700px. This method is used because in testing preview port 8080 browser loads first surfing image slower, therefore elements vertically underneath it are shown into the viewport, therfore triggering the one-time css animations prematurely. 
 * This method asynchronously removes the empty div after 2 seconds, giving the browser enough time to load the image. 
 * QUESTION: Why does $(document).ready() function trigger when images aren't fully loaded?
 */
function asyncRemovePlaceHolder() {
    setTimeout(function(){ 
        $('#placeholder').remove(); 
    }, 2000);
}

/**
 * Removes any pre-existing classes on elemebt with '#quote-label' id
 * Automatically unchecks the hidden checkbox after a user clicks the checkbox. 
 * This is a workaround for animating a css button whenever it is clicked. Orignal workaround found on Stack Overflow. 
 * CSS animation is triggered whenever the checkbox is in "checked" state, therefore in order for the "checked" state to trigger whenever a user presses the label "click me", the checkbox must be reverted to an unchecked state after each click. 
 */
function revertToUnchecked() {
    let label = document.getElementById('quote-label');
    label.className= "";
    label.classList= "";
    label.classList.remove('animate__animated');
    label.classList.remove('animate__fadeInDown');
    
    setTimeout(function(){ document.getElementById('btnControl').click(); }, 500);

}

/**
 * Continously checks if any elements that requires CSS animation is shown in the viewport.
 * If so, the function checks many requirements to sort out which css animation classes to add to the element in question.
 * For example, if an element has classes "animate" and "video-container", the function would animate this element with a fadeInUp animation. 
 * The function reruns continously with scroll, defined earlier in code. 
 * Original method found at https://cssanimation.rocks/scroll-animations/
 */
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

/**
 * This function checks if an element is in the viewport of the screen.
 * @param {DOM Object} the element to be checked if it is in viewport.
 */
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
  