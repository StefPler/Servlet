/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

function postUser() {
    var uName = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var pwd = document.getElementById('password').value;
    var repwd = document.getElementById('repwd').value;
    var fName = document.getElementById('name').value;
    var lName = document.getElementById('surname').value;
    var birthDate = document.getElementById('birthDate').value;
    var gender;
    var el = document.getElementsByName('gender');

    for (var i = 0; i < el.length; i++) {
        if (el[i].checked)
            gender = el[i].value;
    }

    var country = document.getElementById('country').value;
    var town = document.getElementById('town').value;
    var address = document.getElementById('address').value;
    var work = document.getElementById('work').value;
    var interests = document.getElementById('interests').value;
    var generalInfo = document.getElementById('generalInfo').value;

    if (!uName || !email || !pwd || !repwd || !fName || !lName ||
            !birthDate || !country || !town || !work) {
        return;
    }
    console.log('User data: ', uName + ' ', email + ' ', pwd + ' ', repwd + ' ',
            fName + ' ', lName + ' ', birthDate + ' ', gender + ' ', country + ' ',
            town + ' ', address + ' ', work + ' ', interests + ' ', generalInfo + ' ');

    var toSend = 'username=' + uName + '&email=' + email + '&pwd=' + pwd + ' &repwd=' + repwd +
            '&fName=' + fName + '&lName=' + lName + '&birthDate=' + birthDate +
            '&gender=' + gender + '&country=' + country + '&town=' + town +
            '&address=' + address + '&work=' + work + '&interests=' + interests +
            '&generalInfo=' + generalInfo;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('ajaxContent')
                    .innerHTML = xhr.responseText;
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
// set the content type
    xhr.open('POST', 'http://localhost:8080/logbook/UserServlet', true);
    xhr.setRequestHeader('Content-type',
            'application/x-www-form-urlencoded');

    xhr.send(toSend);
}

function sessionExists() {
    var sessionCookie = localStorage.getItem('sessionExists');
    if (sessionCookie) {
        return true;
    }
    return false;
}


function sessionLogIn() {
    if (sessionExists()) {
        console.log('Cookie JSESSIONID exists');
        sessionLogin();
    } else {
        console.log('Cookie JSESSIONID does not exist');
    }
}

var autoLogin = (function () {
    function autoLogIn() {
        if (sessionExists()) {
            console.log('Cookie JSESSIONID exists');
            sessionLogIn();
        } else {
            console.log('Cookie JSESSIONID does not exist');
        }
    }
    return {
        autoLogIn: autoLogIn,
    };
})();

function getUsers() {
//    var fName = document.getElementById('name').value;
//    var lName = document.getElementById('surname').value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
//            should NOT parse directly the result and display it
            document.getElementById('userContent')
                    .innerHTML = xhr.responseText;
            //console.log(xhr.responseText);

        } else if (xhr.status !== 200) {
            document.getElementById('userContent')
                    .innerHTML = xhr.responseText;
            alert('Login request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'http://localhost:8080/logbook/UserServlet', true);
    xhr.send();
}

function getUserProfile(username) {
//    var user = getCookie("username")
//    if (user != "") {
//        username = user;
//    }
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            //
            document.getElementById('loginout1').innerHTML = "<a href=# id=\"loginout1\" class=\"glyphicon glyphicon-user dropdown\" data-toggle=\"dropdown\">"
                    + "Profile</a>"
                    + "<ul class=\"dropdown-menu\">"
                    + "<li><a href=\"#\" data-toggle=\"modal\" data-target=\"#ProfileModal\" >Info</a></li>"
                    + "<li><a href=\"signin.html\">Logout</a></li>"
                    + "</ul>";

            document.getElementById('userProfileModal')
                    .innerHTML = xhr.responseText;

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'http://localhost:8080/logbook/UserServlet?userName=' + username, true);
    xhr.send();
}

function deleteUserRequest() {
    var username = localStorage.getItem("currentUser");
    var toSend = 'username=' + username;
    var xhr = new XMLHttpRequest();
    console.log("DeleteUserRequest");
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'http://localhost:8080/logbook/UserServlet', true);
    xhr.send(toSend);
}

function sessionLogIn() {
    var toSend = 'username=' + "username" + '&password=' + "password";

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('ajaxContent')
                    .innerHTML = xhr.responseText;

            var username = localStorage.getItem("currentUser");
            getUserProfile(username);
            getUsers();
            getLoginMenu();
            getTopUserPosts();
            getUserProfile(localStorage.getItem('currentUser'));
            getUsers();
            localStorage.setItem('sessionExists', true);

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
// set the content type
    xhr.open('POST', 'http://localhost:8080/logbook/LoginServlet', true);
    xhr.setRequestHeader('Content-type',
            'application/x-www-form-urlencoded');

    xhr.send(toSend);
}

function getLoginMenu() {
//    var fName = document.getElementById('name').value;
//    var lName = document.getElementById('surname').value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
//            should NOT parse directly the result and display it
            document.getElementById('postMenu1')
                    .innerHTML = "<div class=\"form-group\">"
                    + "<label for=\"socialPost\">Post about your day</label>"
                    + "<textarea class=\"form-control\"id=\"socialPost\" maxlength=\"500\">"
                    + "</textarea>"
                    + "<br>"
                    + "<button type=\"submit\" class=\"btn btn-primary fluid\" onclick=\"createSocialPost();\">Post</button>"
                    + "</div>"
                    + "<div id=\"message\"></div>"
                    + "<div id=\"newElementId\"></div>";
            //console.log(xhr.responseText);


        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'http://localhost:8080/logbook/UserServlet', true);
    xhr.send();
}

function createSocialPost() {
    console.log("Posted");
    var socialPostContent = document.getElementById("socialPost").value;


    if (socialPostContent === "") {
        console.log("Post is empty");
        document.getElementById("message").innerHTML = "<p style=\"color:pink\">Fill out the form in order to Post</p>";
    } else {

        document.getElementById("message").innerHTML = "<p></p>";
        // First create a DIV element.
        var txtNewInputBox = document.createElement('div');

        var toSend = 'username=' + localStorage.getItem("currentUser") + "&description="
                + socialPostContent + "&resourceUrl=none" + "&imageUrl=none" + "&imageBase64=none"
                + "&latitude=none" + "&longtitude=none";

        var xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {

                // Then add the content (the post) of the element.
                txtNewInputBox.innerHTML = xhr.responseText;
                // Finally put it where it is supposed to appear.
                document.getElementById("newElementId").append(txtNewInputBox);

            } else if (xhr.status !== 200) {
                alert('Request failed. Returned status of ' + xhr.status);
            }
        };
// set the content type
        xhr.open('POST', 'http://localhost:8080/logbook/SocialPostServlet', true);
        xhr.setRequestHeader('Content-type',
                'application/x-www-form-urlencoded');

        xhr.send(toSend);

    }

}

function getTopUserPosts() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            console.log(JSON.parse(xhr.responseText));
//            document.getElementById("top10userposts").innerHTML = xhr.responseText;

            var postsArray = JSON.parse(xhr.responseText);
            document.getElementById("top10userposts").innerHTML = "<h3 class=\"textTheme\">Top Posts</h3>";
            postsArray.forEach((el) => {

                localStorage.setItem("lon", el.longitude);
                localStorage.setItem("lat", el.latitude);
                var txtNewInputBox = document.createElement('div');
                txtNewInputBox.innerHTML = `<div class=\"card\" style=\"background-color:white\">
                    
                    <button data-toggle=\"modal\" onclick="initModalMap()" data-target="#myMapModal">Display location</button>
                    <div class=\"card-header\">
                      <h4> ${el.userName} posted</h4>
                    </div>

                    <div class=\"card-body\">
                        <div class=\"card-text\">
                          <p>${el.description}</p>
                        </div>

                        <footer class=\"blockquote-footer\">
                          <h5> ${el.createdAt}</h5>
                        </footer>
                    </div>
                </div>`


                document.getElementById("newPostElementId").append(txtNewInputBox);

            });


        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'http://localhost:8080/logbook/SocialPostServlet?amountId=all', true);
    xhr.send();
}

function loginUser() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    if (!username || !password) {
        alert('Please fill in all fields');
        return;
    }

    localStorage.setItem('currentUser', username);
    console.log('User data: ', username + ' ' + password);

    var toSend = 'username=' + username + '&password=' + password;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('ajaxContent')
                    .innerHTML = xhr.responseText;

            getUserProfile(username);
            getUsers();
            getLoginMenu();
            getTopUserPosts();
            localStorage.setItem('sessionExists', true);

        } else if (xhr.status !== 200) {
            document.getElementById('ajaxContent')
                    .innerHTML = xhr.responseText;
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
// set the content type
    xhr.open('POST', 'http://localhost:8080/logbook/LoginServlet', true);
    xhr.setRequestHeader('Content-type',
            'application/x-www-form-urlencoded');

    xhr.send(toSend);
}

function updateUser() {
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var fName = document.getElementById('name').value;
    var lName = document.getElementById('surname').value;
    var birthDate = document.getElementById('birthDate').value;
    var gender = document.getElementById('gender').value;
    var country = document.getElementById('country').value;
    var town = document.getElementById('town').value;
    var address = document.getElementById('address').value;
    var work = document.getElementById('work').value;
    var interests = document.getElementById('interests').value;
    var generalInfo = document.getElementById('generalInfo').value;

    var toSend = 'userName=' + username + '&email=' + email +
            '&firstName=' + fName + '&lastName=' + lName + '&birthDate=' + birthDate +
            '&gender=' + gender + '&country=' + country + '&town=' + town +
            '&address=' + address + '&work=' + work + '&interests=' + interests +
            '&info=' + generalInfo;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
//            document.getElementById('ajaxContent')
//                    .innerHTML = xhr.responseText;

            console.log(JSON.parse(xhr.responseText));

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
// set the content type
    xhr.open('POST', 'http://localhost:8080/logbook/UpdateUserServlet', true);
    xhr.setRequestHeader('Content-type',
            'application/x-www-form-urlencoded');

    xhr.send(toSend);
}