// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
let elementsToShow;
let scroll;

$(document).ready(function() {
    scroll = window.requestAnimationFrame || function(callback){ window.setTimeout(callback, 1000/60)};
    elementsToShow = document.querySelectorAll('.animate');    
    loop();
    removeImagePlaceHolder();
})

function addRandomGreeting() {
    revertToUnchecked();
    const greetings =
        ["In a world too often governed by corruption and arrogance, it can be difficult to stay true to one's philosophical and literary principles.   -Lemony Snicket, A Series of Unfortunate Events", "Did you know that the Sparrow flies south for winter?   -Skulduggery Pleasant, Skulduggery Pleasant", "Someday in your life, you'll realize there is no turning back. There was never any turning back.   -u/JayStar1213"];
    const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  
    let entrances = 
        ["animate__bounceInUp", "animate__bounceInLeft", "animate__bounceInRight", "animate__bounceInDown", "animate__fadeInBottomLeft", "animate__fadeInBottomRight"]
    let entrance = entrances[Math.floor(Math.random() * greetings.length)];
    // Add it to the page.
    const quoteContainer = document.getElementById('quote-container');
    const quote = document.getElementById('quote');
    quote.className="";
    quote.classList.add('animate__animated');
    quote.classList.add(entrance);
    quote.classList.add('animate__delay-1s');
    quote.innerText = greeting;
   
}

function revertToUnchecked() {
    setTimeout(function(){ document.getElementById('btnControl').click(); }, 500);
}

function removeImagePlaceHolder() {
    setTimeout(function(){ $('#img-placeholder').remove(); }, 3000);
}


function loop() {
    // console.log('inside loop')
    // console.log(elementsToShow);
    // console.log('inside loop');
    console.log(elementsToShow);
    elementsToShow.forEach(function (element) {
      if (isElementInViewport(element)) {
        if(element.classList.contains('blog-images-row')) {
            element.classList.add('animate__animated');
            element.classList.add('animate__fadeIn');
            element.classList.add('animate__delay-1s');
        } else if(element.classList.contains('1s-delay')) {
            element.classList.add('animate__animated');
            element.classList.add('animate__fadeInDown');
            element.classList.add('animate__delay-1s');
        } else {
            element.classList.add('animate__animated');
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
  