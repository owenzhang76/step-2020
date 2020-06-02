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