function likepost(postid,likeType,likeid){
if((likeType===null || !likeType) && likeid=='null'){
console.log("liking post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('status',true);
axios.post('/postLikes',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Liked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else if(likeType===null || !likeType){
console.log("Editing Disliked post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('likeid',likeid);
params.append('status',true);
params.append('commentid',null);
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Liked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else{
toastr["info"]("Already Liked", "Like");
}
}



function dislikepost(postid,likeType,likeid){
if((likeType===null || likeType) && likeid=='null'){
console.log("Disliking post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('status',false);
axios.post('/postLikes',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Disliked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else if(likeType===null || likeType){
console.log("Editing Liked post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('likeid',likeid);
params.append('status',false);
params.append('commentid',null);
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Disliked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else{
toastr["info"]("Already Disliked", "Like");
}



function likecomment(postid,commentid,likeType,likeid){
console.log("Comment Likes",postid,commentid,likeType,likeid);
if((likeType===null || !likeType) && likeid=='null'){
console.log("liking Comment",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('commentid',commentid);
params.append('status',true);
axios.post('/commentLikes',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Comment Liked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else if(likeType===null || !likeType){
console.log("Editing Disliked post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('likeid',likeid);
params.append('commentid',commentid);
params.append('status',true);
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Comment Liked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else{
toastr["info"]("Comment Already Liked", "Like");
}
}



function dislikecomment(postid,commentid,likeType,likeid){
//console.log("Comment Likes",postid,commentid,likeType,likeid);
if((likeType===null || likeType) && likeid=='null'){
//console.log("disliking Comment",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('commentid',commentid);
params.append('status',false);
axios.post('/commentLikes',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Comment DisLiked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else if(likeType===null || likeType){
console.log("Editing Disliked post",likeType,likeid);
const params = new URLSearchParams();
var owner=getCookie("user");
params.append('username',owner);
params.append('postid',postid);
params.append('likeid',likeid);
params.append('commentid',commentid);
params.append('status',false);
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Comment DisLiked Successfully", "Success");
//getPostDetail();
}else{
toastr["error"](response.data.data.message, "Error");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else{
toastr["info"]("Comment Already Disliked", "Like");
}
}
