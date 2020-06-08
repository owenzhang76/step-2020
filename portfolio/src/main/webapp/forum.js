let startCursorLocation = "0";

$(document).ready(function() {
    checkLogin();
})

/*
 * loads x newest comments. 
 * Comments are sorted in descending order, therefore we reverse iterate through comments to display in an old at top new at bottom model. 
 * Requires ArrayList message to be cleared each fetch is called. 
 */
function displayComments() {
    console.log("displayComments ran");
    fetch('/add-comment')
        .then(response => response.json())
        .then((comments) => {
            console.log(comments);
            console.log(comments.length);
            let orderedCommentsOldToNew = comments.reverse();
            console.log("hi, " + orderedCommentsOldToNew);
            for (var i = 0; i < orderedCommentsOldToNew.length; i++) {
                console.log(orderedCommentsOldToNew[i]['body']);
            }
            for (var index in orderedCommentsOldToNew) {
                console.log("inside for in loop");
                let commentDiv = document.createElement('div');
                commentDiv.classList.add("comment");
                if(comments[index]["senderName"] == null) {
                    commentDiv.innerHTML = "anon: " + comments[index]["body"];
                } else {
                    commentDiv.innerHTML = comments[index]["senderName"] + ": " + comments[index]["body"];
                }
                document.getElementById("comments-container").appendChild(commentDiv);
            }
            console.log(startCursorLocation);
        })
        .catch(err => console.log(err));
}

/*
 * loads x immediately older comments after x newest comments. 
 * Pass in start cursor index and end cursor index. 
 */
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
            console.log(comments);
            console.log(comments.length);
            let orderedCommentsOldToNew = comments;
            orderedCommentsOldToNew.reverse();
            console.log(orderedCommentsOldToNew);
            for (var index in orderedCommentsOldToNew.reverse()) {
                console.log("inside for in loop");
                if (comments[index]["id"] == 0) {
                    console.log("found the encodedCursorString");
                    console.log("encoded string is " + comments[index]["body"]);
                    startCursorLocation = comments[index]["body"];
                } else {
                    let commentDiv = document.createElement('div');
                    commentDiv.classList.add("comment");
                    if(comments[index]["senderName"] == null) {
                        commentDiv.innerHTML = "anon: " + comments[index]["body"];
                    } else {
                        commentDiv.innerHTML = comments[index]["senderName"] + ": " + comments[index]["body"];
                    }
                    document.getElementById("comments-container").prepend(commentDiv);
                }
            }
        })
}

function checkLogin() {
    fetch('/check-login')
        .then(response => response.json())
        .then((data) => {
            console.log(data);
            let successOrFailure = data[0];
            if(successOrFailure === "true") {
                console.log("inside if");
                document.getElementById("login-logout-button").href = data[1];
                document.getElementById("login-logout-text").innerText = "LOGOUT";
                document.getElementById("login-to-chat").style.display = "none";
                 document.getElementById("chat-info-container").style.display = "flex";
                displayComments();
            } else {
                console.log("inside else");
                document.getElementById("chat-info-container").style.display = "none";
                document.getElementById("login-logout-button").href = data[1];
                document.getElementById("login-logout-text").innerText = "LOGIN";
                displayComments();
            }
        })
}