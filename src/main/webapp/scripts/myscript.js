
'use strict';

var map;
var markers;
var latestJsonResponse;

var mapDiv;
var markersDiv;

function revealRadio() {
    var div = document.getElementById('enableFaceApp');
    div.style.display = "block";

}

// function revealButtons() {
//   var div = document.getElementById('enableFaceAppButtons');
//   div.style.display = "block";
// }

// function hideButtons() {
//   var div = document.getElementById('enableFaceAppButtons');
//   div.style.display = "none";
// }

function init() {
    map = new OpenLayers.Map("mapdiv");
    map.addLayer(new OpenLayers.Layer.OSM());

    var lonLat = new OpenLayers.LonLat(23.731003, 37.972034)
            .transform(
                    new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                    new OpenLayers.Projection("EPSG:900913") // to Spherical Mercator Projection
                    );

    var zoom = 16;

    markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);

    markers.addMarker(new OpenLayers.Marker(lonLat));

    map.setCenter(lonLat, zoom);

}

function initModalMap() {

    if (localStorage.getItem("firstCord")) {
        localStorage.setItem("firstCord", true);
    } else {
        localStorage.setItem("firstCord", false);
    }


}

function displayOnMap() {


    if (localStorage.getItem("firstCord")) {
        console.log('first time');
        mapDiv = new OpenLayers.Map("mapModalDiv");
        mapDiv.addLayer(new OpenLayers.Layer.OSM());

        var lonLat = new OpenLayers.LonLat(23.731003, 37.972034)
                .transform(
                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                        new OpenLayers.Projection("EPSG:900913") // to Spherical Mercator Projection
                        );

        var zoom = 16;

        markersDiv = new OpenLayers.Layer.Markers("Markers");
        mapDiv.addLayer(markersDiv);

        markersDiv.addMarker(new OpenLayers.Marker(lonLat));

        mapDiv.setCenter(lonLat, zoom);
    } else {
        console.log('2nd+ time');
        var longitude = localStorage.getItem("lon");
        var latitude = localStorage.getItem("lat");
        console.log("Cords are ", longitude, " ", latitude);

        var lonlat = new OpenLayers.LonLat(longitude, latitude)
                .transform(
                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                        new OpenLayers.Projection("EPSG:900913") // to Spherical Mercator Projection
                        );

        markersDiv.addMarker(new OpenLayers.Marker(lonlat));

        mapDiv.setCenter(lonlat, 16);
    }



}

function loadNewCords() {

    if (latestJsonResponse) {

        var lonlat = new OpenLayers.LonLat(latestJsonResponse.lon, latestJsonResponse.lat)
                .transform(
                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                        new OpenLayers.Projection("EPSG:900913") // to Spherical Mercator Projection
                        );

        markers.addMarker(new OpenLayers.Marker(lonlat));

        map.setCenter(lonlat, 16);

    } else {
        alert("Required fields were not provided");
    }
}

function checkForm() {
    var pwd = document.getElementById('password').value;
    // console.log('Very safe, me gonna reveal pass: ', pwd);
    var repwd = document.getElementById('repwd').value;
    // console.log('Very safe me, me gonna reveal retyped pass: ', repwd);

    if ((pwd === repwd) && pwd !== '') {
        // all gud
        console.log('Passwords match');
        document.getElementById('whenWrongPass').innerHTML = '';
        document.getElementById('whenCorrectPass').innerHTML = 'Passwords Match!';
    } else {
        // alert('The passwords do not match');
        document.getElementById('whenCorrectPass').innerHTML = '';
        document.getElementById('whenWrongPass').innerHTML = 'The passwords do not match';
    }
}


function validateLocation() {
    var Town = document.getElementById('town').value;
    var Address = document.getElementById('address').value;
    var Country = document.getElementById('country').value;


    console.log(`Values passed, town: ${Town}, address: ${Address}, country: ${Country}`);
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {

        // if the request was successful then read the response
        if (this.readyState == 4 && this.status == 200) {
            var jsonResponse = JSON.parse(this.responseText);
            console.log(jsonResponse);

            if (jsonResponse.length >= 1) {
                // we have a match
                var location = jsonResponse[0];
                latestJsonResponse = location;
                console.log('Match is: ', location);
                document.getElementById('whenWrongAddress').innerHTML = '';
                document.getElementById('whenCorrectAddress').innerHTML = 'Location valid';
                return true;
            } else {
                // we don't have a location match΄
                document.getElementById('whenCorrectAddress').innerHTML = '';
                document.getElementById('whenWrongAddress').innerHTML = 'Location not valid';
                return false;
            }
        }

        // readyState = 4 means that the request was send and the response is ready
        if (this.readyState == 4 && this.status !== 200) {
            console.error('There has been an error with the request: ', this.statusText);
            return false;
        }
    };

    xhttp.open('GET', `https://nominatim.openstreetmap.org/search?q=${Town},${Country},${Address}&format=json`, true);

    xhttp.send();
}


function getLocation() {

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(reverseSearch);
    } else {
        alert('Geolocation is not supported. Can not locate user');
    }
}

function reverseSearch(position) {
    console.log('lat: ', position.coords.latitude, 'lon: ', position.coords.longitude);

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {

        // if the request was successful then read the response
        if (this.readyState == 4 && this.status == 200) {
            var jsonResponse = JSON.parse(this.responseText);
            latestJsonResponse = jsonResponse;
            console.log(jsonResponse);

            document.getElementById('town').value = jsonResponse.address.city;
            document.getElementById('address').value = jsonResponse.address.neighbourhood;
            document.getElementById('country').setAttribute("selected", String.prototype.toUpperCase(jsonResponse.address.country_code));
        }

        // readyState = 4 means that the request was send and the response is ready
        if (this.readyState == 4 && this.status !== 200) {
            console.error('There has been an error with the request: ', this.statusText);
            return false;
        }
    };

    xhttp.open('GET', `https://nominatim.openstreetmap.org/reverse?lat=${position.coords.latitude}&lon=${position.coords.longitude}&format=json`, true);

    xhttp.send();

}