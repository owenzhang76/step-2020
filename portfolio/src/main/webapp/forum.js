let startCursorLocation = "0";
let loggedIn; 
let blobUploadUrl = "";
let counter = 0;

$(document).ready(function() {
    if (loggedIn == false || loggedIn == null) {
        checkLogin();
    }
    setBlobstoreUploadUrl();
})

/*
 * loads x newest comments. 
 * Comments are sorted in descending order, therefore we reverse iterate through comments to display in an old at top new at bottom model. 
 * Requires ArrayList message to be cleared each fetch is called. 
 **/
function displayComments() {
    console.log("displayComments ran");
    fetch('/add-comment')
        .then(response => response.json())
        .then((comments) => {
            let orderedCommentsOldToNew = comments.reverse();
            for (var i = 0; i < orderedCommentsOldToNew.length; i++) {
                console.log(orderedCommentsOldToNew[i]['body']);
            }
            for (var index in orderedCommentsOldToNew) {
                let commentDiv = document.createElement('div');
                commentDiv.classList.add("comment-div-container");
                let commentText = document.createElement('div');
                if(comments[index]["senderName"] == null) {
                    commentText.innerHTML = "anon: " + comments[index]["body"];
                } else {
                    commentText.innerHTML = comments[index]["senderName"] + ": " + comments[index]["body"];
                }
                commentDiv.appendChild(commentText);
                if (comments[index]["imageUrl"] != null) {
                    console.log("image detected");
                    let commentImage = document.createElement('img');
                    commentImage.src = comments[index]["imageUrl"];
                    commentImage.classList.add("comment-medium");
                    commentDiv.appendChild(commentImage);
                }
                document.getElementById("comments-container").appendChild(commentDiv);
            }
        })
        .catch(err => console.log(err));
}

/*
 * loads x immediately older comments after x newest comments. 
 * Pass in start cursor index.
 **/
function loadPreviousComments() {
    console.log('loadPreviousComments ran');
    let data = {
        startIndex: startCursorLocation,
    };
    console.log(data);
    fetch("/previous-comments", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => response.json())
        .then((comments) => {
            for (var index in comments) {
                if (comments[index]["id"] == 0) {
                    startCursorLocation = comments[index]["body"];
                } else {
                    let commentDiv = document.createElement('div');
                    commentDiv.classList.add("comment-div-container");
                    let commentText = document.createElement('div');
                    if(comments[index]["senderName"] == null) {
                        commentText.innerHTML = "anon: " + comments[index]["body"];
                    } else {
                        commentText.innerHTML = comments[index]["senderName"] + ": " + comments[index]["body"];
                    }
                    commentDiv.appendChild(commentText);
                    if (comments[index]["imageUrl"] != null) {
                        console.log("image detected");
                        let commentImage = document.createElement('img');
                        commentImage.src = comments[index]["imageUrl"];
                        commentImage.classList.add("comment-image");
                        commentDiv.appendChild(commentImage);
                    }
                    document.getElementById("comments-container").prepend(commentDiv);
                }
            }
        })
}

/*
 * Checks if user is logged in via the Users API. 
 **/
function checkLogin() {
    fetch('/check-login')
        .then(response => response.json())
        .then((data) => {
            let successOrFailure = data[0];
            if(successOrFailure === "true") {
                document.getElementById("login-logout-button").href = data[1];
                document.getElementById("login-logout-text").innerText = "LOGOUT";
                document.getElementById("login-to-chat").style.display = "none";
                document.getElementById("chat-info-container").style.visibility = "visible";
                loggedIn = true;
                displayComments();
            } else {
                console.log("inside else");
                document.getElementById("chat-info-container").style.visibility = "hidden";
                document.getElementById("login-logout-button").href = data[1];
                document.getElementById("login-logout-text").innerText = "LOGIN";
                loggedIn = false;
                displayComments();
            }
        })
}

/*
 * Sets action field of upload images url to correct blobstore upload url. 
 **/
function setBlobstoreUploadUrl() {
    console.log("get url ran");
    fetch('/blobstore-upload-url')
        .then(response => response.text()) 
        .then((url) => {
            console.log("blostore upload url is " + url);
            blobUploadUrl = url;
            const messageForm = document.getElementById('comment-form');
            messageForm.action = blobUploadUrl;
        })
}