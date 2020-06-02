// TODO: Complete async arrow notation function here
// Include: Creating a DOM Object
// Include: Adding to DOM Tree

function displayComment() {
    console.log("displayComment ran");
    fetch('/add-comment')
        .then(response => response.text())
        .then((comment) => {
            let commentDiv = document.createElement('div');
            commentDiv.classList.add("comment");
            commentDiv.innerHTML = comment;
            document.getElementById("comments-container").appendChild(commentDiv);
        })
        .catch(err => console.log(err));
}

function displayThoughts() {
    console.log('displayThoughts ran');
    fetch('/add-comment')
        .then(response => response.json())
        .then((thoughts) => {
            console.log(thoughts);
            for (var index in thoughts) {
                let thoughtDiv = document.createElement('div');
                thoughtDiv.classList.add("thought");
                thoughtDiv.innerHTML = thoughts[index];
                document.getElementById("comments-container").appendChild(thoughtDiv);
            }
        })
        .catch(err => console.log(err));
}