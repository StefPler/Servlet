'use strict';

/*
    Author: Panagiotis Papadakos papadako@csd.uoc.gr

    Try to read this file and understand what the code does...
    Then try to complete the missing functionality

    For the needs of the hy359 2020 course
    University of Crete

    At the end of the file there are some comments about our trips

*/

var runningRequest;
/*  face recognition that is based on faceplusplus service */
var faceRec = (function () {

  // Object that holds anything related with the facetPlusPlus REST API Service
  var faceAPI = {
    apiKey: 'l2jNgKbk1HXSR4vMzNygHXx2g8c_xT9c',
    apiSecret: '2T6XdZt4EYw-I7OhmZ6g1wtECl81e_Ip',
    app: 'hy359',
    // Detect
    // https://console.faceplusplus.com/documents/5679127
    detect: 'https://api-us.faceplusplus.com/facepp/v3/detect',  // POST
    // Set User ID
    // https://console.faceplusplus.com/documents/6329500
    setuserId: 'https://api-us.faceplusplus.com/facepp/v3/face/setuserid', // POST
    // Get User ID
    // https://console.faceplusplus.com/documents/6329496
    getDetail: 'https://api-us.faceplusplus.com/facepp/v3/face/getdetail', // POST
    // addFace
    // https://console.faceplusplus.com/documents/6329371
    addFace: 'https://api-us.faceplusplus.com/facepp/v3/faceset/addface', // POST
    // Search
    // https://console.faceplusplus.com/documents/5681455
    search: 'https://api-us.faceplusplus.com/facepp/v3/search', // POST
    // Create set of faces
    // https://console.faceplusplus.com/documents/6329329
    create: 'https://api-us.faceplusplus.com/facepp/v3/faceset/create', // POST
    // update
    // https://console.faceplusplus.com/documents/6329383
    update: 'https://api-us.faceplusplus.com/facepp/v3/faceset/update', // POST
    // removeface
    // https://console.faceplusplus.com/documents/6329376
    removeFace: 'https://api-us.faceplusplus.com/facepp/v3/faceset/removeface', // POST
    analyze: 'https://api-us.faceplusplus.com/facepp/v3/face/analyze'
  };

  // Object that holds anything related with the state of our app
  // Currently it only holds if the snap button has been pressed
  var state = {
    photoSnapped: false,
  };

  // function that returns a binary representation of the canvas
  function getImageAsBlobFromCanvas(canvas) {

    // function that converts the dataURL to a binary blob object
    function dataURLtoBlob(dataUrl) {
      // Decode the dataURL
      var binary = atob(dataUrl.split(',')[1]);

      // Create 8-bit unsigned array
      var array = [];
      for (var i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
      }

      // Return our Blob object
      return new Blob([new Uint8Array(array)], {
        type: 'image/jpg',
      });
    }

    var fullQuality = canvas.toDataURL('image/jpeg', 1.0);
    return dataURLtoBlob(fullQuality);

  }

  // function that returns a base64 representation of the canvas
  function getImageAsBase64FromCanvas(canvas) {
    // return only the base64 image not the header as reported in issue #2
    return canvas.toDataURL('image/jpeg', 1.0);

  }

  // Function called when we upload an image
  function uploadImage() {
    if (state.photoSnapped) {
      var canvas = document.getElementById('canvas');
      // var image = getImageAsBlobFromCanvas(canvas);
      var image = getImageAsBase64FromCanvas(canvas);
      // ============================================

      // TODO!!! Well this is for you ... YES you!!!
      // Good luck!

      // Create Form Data. Here you should put all data
      // requested by the face plus plus services and
      // pass it to ajaxRequest
      var data = new FormData();
      data.append('api_key', faceAPI.apiKey);
      data.append('api_secret', faceAPI.apiSecret);
      data.append('image_base64', image);

      // add also other query parameters based on the request
      // you have to send

      // You have to implement the ajaxRequest. Here you can
      // see an example of how you should call this
      // First argument: the HTTP method
      // Second argument: the URI where we are sending our request
      // Third argument: the data (the parameters of the request)
      // ajaxRequest function should be general and support all your ajax requests...
      // Think also about the handler
      ajaxRequest('POST', faceAPI.detect, data);

      setTimeout(() => {
        console.log('Running req: ', runningRequest);
        var faceToken = runningRequest.faces[0].face_token;

        var userId = document.getElementById('username').value;

        var setUserIdData = new FormData();
        setUserIdData.append('api_key', faceAPI.apiKey);
        setUserIdData.append('api_secret', faceAPI.apiSecret);
        setUserIdData.append('face_token', faceToken);
        setUserIdData.append('user_id', userId);

        ajaxRequest('POST', faceAPI.setuserId, setUserIdData);

        setTimeout(() => {
          console.log('Running req: ', runningRequest);

          var addFaceData = new FormData();
          addFaceData.append('api_key', faceAPI.apiKey);
          addFaceData.append('api_secret', faceAPI.apiSecret);
          addFaceData.append('outer_id', 'hy359');
          addFaceData.append('face_tokens', faceToken);

          ajaxRequest('POST', faceAPI.addFace, addFaceData);

          setTimeout(() => {
            console.log('Running req: ', runningRequest);

            try {
              var userName = runningRequest.results[0].user_id;
              document.getElementById('username').value = userName;
            } catch (e) {
              console.error('There was probably an error with the request: ', e);
            } finally {
              setTimeout(() => {
                var analyzeData = new FormData();
                analyzeData.append('api_key', faceAPI.apiKey);
                analyzeData.append('api_secret', faceAPI.apiSecret);
                analyzeData.append('face_tokens', faceToken);
                analyzeData.append('return_attributes', 'age,smiling,gender');

                ajaxRequest('POST', faceAPI.analyze, analyzeData);

                setTimeout(() => {
                    alert(`Age: ${runningRequest.faces[0].attributes.age.value}, Gender: ${runningRequest.faces[0].attributes.gender.value}, Smile %: ${runningRequest.faces[0].attributes.age.value}`);
                }, 4500);
              }, 4500);
            }


          }, 4500);
        }, 4500);

      }, 4500);



    } else {
      alert('No image has been taken!');
    }
  }

  function searchImage() {
    var canvas = document.getElementById('canvas');
    var image = getImageAsBase64FromCanvas(canvas);

    var searchData = new FormData();
    searchData.append('api_key', faceAPI.apiKey);
    searchData.append('api_secret', faceAPI.apiSecret);
    searchData.append('outer_id', 'hy359');
    searchData.append('image_base64', image);

    ajaxRequest('POST', faceAPI.search, searchData);

    setTimeout(() => {
      console.log('Running req: ', runningRequest);
      var userName = runningRequest.results[0].user_id;
      document.getElementById('username').value = userName;
    }, 4500);

  }


  // Function for initializing things (event handlers, etc...)
  function init() {
    // Put event listeners into place
    // Notice that in this specific case handlers are loaded on the onload event
    window.addEventListener('DOMContentLoaded', function () {
      // Grab elements, create settings, etc.
      var canvas = document.getElementById('canvas');
      var context = canvas.getContext('2d');
      var video = document.getElementById('video');
      var mediaConfig = {
        video: true,
      };
      var errBack = function (e) {
        console.log('An error has occurred!', e);
      };

      // Put video listeners into place
      if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        navigator.mediaDevices.getUserMedia(mediaConfig).then(function (stream) {
          video.srcObject = stream;
          video.onloadedmetadata = function (e) {
            video.play();
          };
        });
      }

      // Trigger photo take
      document.getElementById('snap').addEventListener('click', function () {
        context.drawImage(video, 0, 0, 352, 240);
        state.photoSnapped = true; // photo has been taken
      });

      // Trigger when upload button is pressed
      document.getElementById('upload').addEventListener('click', uploadImage);

      document.getElementById('search').addEventListener('click', searchImage);

    }, false);

  }

  // ============================================

  // !!!!!!!!!!! ================ TODO  ADD YOUR CODE HERE  ====================
  // From here on there is code that should not be given....

  // You have to implement the ajaxRequest function!!!!
  function ajaxRequest(request, uri, data) {

    var xhttp = new XMLHttpRequest();
    var response;

    xhttp.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        response = JSON.parse(this.responseText);
        runningRequest = response;
        console.log(`Request ${request} with uri: ${uri}`, response);
      }

      if ((this.readyState == 4 && this.status != 200)) {
        console.error(`Error with ajax request ${request} with uri: ${uri} `);
        alert(`There was an error with ajax request ${request} with uri: ${uri} `);
      }

    };

    xhttp.open(request, uri, true);
    xhttp.send(data);

  }

  // !!!!!!!!!!! =========== END OF TODO  ===============================

  // Public API of function for facet recognition
  // You might need to add here other methods based on your implementation
  return {
    init: init,
  };

})();


