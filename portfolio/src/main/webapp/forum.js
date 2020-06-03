/*
 * loads x newest comments. 
 * Comments are sorted in descending order, therefore we reverse iterate through comments to display in an old at top new at bottom model. 
 * Requires ArrayList message to be cleared each fetch is called. 
 */

let startCursorLocation = 0;

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
                commentDiv.innerHTML = "anon: " + comments[index]["body"];
                document.getElementById("comments-container").appendChild(commentDiv);
            }
            startCursorLocation += comments.length;
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
            let orderedCommentsOldToNew = comments.reverse();
            console.log(orderedCommentsOldToNew);
            for (var index in orderedCommentsOldToNew) {
                console.log("inside for in loop");
                let commentDiv = document.createElement('div');
                commentDiv.classList.add("comment");
                commentDiv.innerHTML = "anon: " + comments[index]["body"];
                document.getElementById("previous-comments-container").appendChild(commentDiv);
            }
        })
}