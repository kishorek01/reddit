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




function publishComment(){
let comment=document.getElementById("createPostInput").value;
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let postId=querySearch;
comment=comment.replace(/(\r\n|\n|\r)/gm, "");
if(postId!="" && comment!=""){
const params = new URLSearchParams();
params.append('postid', postId);
params.append('comment',comment);
axios.post('/postComments',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
      var noc=document.getElementById("nocomments");
      if(noc){
      noc.remove();
      document.getElementById("sortType").style.display="block";
      }
      document.getElementById("modalArea").style.display="none";
      document.getElementById("createPostInput").value="";
//getPostDetail();
let val;
if(document.getElementById("sortType")){
val=document.getElementById("sortType").value;
}else{
val="new";
}
if(response.data && response.data.data && response.data.data.data){
createCommentForNew(response.data.data.data,val,null);
}
  }})
  .catch(function (error) {
    console.log(error);
  });
}
}


function publishEditComment(){
let comment=document.getElementById("EditCommentInput").value;
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
comment=comment.replace(/(\r\n|\n|\r)/gm, "");
let postId=querySearch;
let commentid=document.getElementById("editCommentId").value;
if(postId!="" && comment!="" && commentid!=""){
const params = new URLSearchParams();
params.append('postid', postId);
params.append('comment',comment);
params.append('commentid',commentid);
axios.post('/editComment',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
      document.getElementById("modalEditArea").style.display="none";
      document.getElementById("EditCommentInput").value="";
      document.getElementById("editCommentId").value="";
//getPostDetail();
if(response.data && response.data.data && response.data.data.data){
var dat=response.data.data.data;
let id=dat.commentid+"value";
//console.log(id);
document.getElementById(dat.commentid+"value").innerHTML=dat.comment;
}
  }})
  .catch(function (error) {
    console.log(error);
  });
}
}




function publishChildComment(){
let comment=document.getElementById("createChildPostInput").value;
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let postId=querySearch;
comment=comment.replace(/(\r\n|\n|\r)/gm, "");
let parentcomment=document.getElementById("childCommentId").value;
if(postId!="" && comment!="" && parentcomment!=""){
const params = new URLSearchParams();
params.append('postid', postId);
params.append('comment',comment);
params.append('parentcomment',parentcomment);
axios.post('/childComments',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
      document.getElementById("modalChildArea").style.display="none";
      document.getElementById("createChildPostInput").value="";
      document.getElementById("childCommentId").value="";
//getPostDetail();
let val;
if(document.getElementById("sortType")){
val=document.getElementById("sortType").value;
}else{
val="new";
}
if(response.data && response.data.data && response.data.data.data){
createCommentForNew(response.data.data.data,val,parentcomment);
}
  }})
  .catch(function (error) {
    console.log(error);
  });
}
}



