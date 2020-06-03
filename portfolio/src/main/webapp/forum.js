// TODO: Complete async arrow notation function here
// Include: Creating a DOM Object
// Include: Adding to DOM Tree

// function displayComment() {
//     console.log("displayComment ran");
//     fetch('/add-comment')
//         .then(response => response.text())
//         .then((comment) => {
//             let commentDiv = document.createElement('div');
//             commentDiv.classList.add("comment");
//             commentDiv.innerHTML = comment;
//             document.getElementById("comments-container").appendChild(commentDiv);
//         })
//         .catch(err => console.log(err));
// }

function displayComments() {
    fetch('/add-comment')
        .then(response => response.json())
        .then((comments) => {
            console.log(comments);
            for (var index in comments) {
                let commentDiv = document.createElement('div');
                commentDiv.classList.add("comment");
                commentDiv.innerHTML = "anon: " + comments[index]["body"];
                document.getElementById("comments-container").appendChild(commentDiv);
            }
        })
        .catch(err => console.log(err));
}

function convertTimestampToTime(timestamp) {
    var date = new Date(timestamp);
    var month = date.getMonth();
    var day = date.getDay();
    var hours = date.getHours();
    var minutes = "0" + date.getMinutes();
    var formattedTime = month + "." + day + ": " + hours + ':' + minutes.substr(-2);

    return formattedTime;
}