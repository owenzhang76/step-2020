// TODO: Complete async arrow notation function here
// Include: Creating a DOM Object
// Include: Adding to DOM Tree

function displayQuote() {
    fetch('/randomQuote')
        .then(response => response.text)
        .then((quote) => {
            let quoteDiv = document.createElement('div');
            quoteDiv.classList.add("quote");
            quoteDiv.innerHTML = quote;
            document.getElementById("comments-container").appendChild(quoteDiv);
        })
}