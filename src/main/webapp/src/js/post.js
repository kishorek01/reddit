function logout(){
axios.get('/logout')
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data;
  if(data.code==200){
//  console.log(response.data);
    toastr["success"]("Logout Successful","Success");
  setTimeout(function(){
  document.location="login.html";
  },500)
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}
function showparentcomment(data,comments,likesObj){
document.getElementById("commentAreaBox").innerHTML="";
//let parentComments=data;
   var commentsArray=Object.values(comments);
//   commentsArray.sort(custom_sort);
   var obj={}
   var keys=data;
     var owner=getCookie("user");
   for(var i=0;i<keys.length;i++){
   if(comments[keys[i]]){

   var likeArr=[];
   for(var j=0;j<comments[keys[i]].likes.length;j++){
   likeArr.push(likesObj[comments[keys[i]].likes[j]]);
   }
     createCommentFor("commentAreaBox",comments[keys[i]],comments,likeArr,likesObj)
   }
   obj[commentsArray[i].commentid]=commentsArray[i];
   }
      return null;
}
function showComments(data,likesObj){
document.getElementById("commentAreaBox").innerHTML="";
let parentComments=Object.keys(data)
                       .filter((key) => key.includes("Post"))
                       .reduce((obj, key) => {
                           return Object.assign(obj, {
                             [key]: data[key]
                           });
                     }, {});
   var commentsArray=Object.values(data);
//   commentsArray.sort(custom_sort);
   var obj={}
   var keys=Object.keys(parentComments);
     var owner=getCookie("user");
   for(var i=0;i<commentsArray.length;i++){
   if(commentsArray[i].commentid.includes("Post")){
   var likeArr=[];
   for(var j=0;j<commentsArray[i].likes.length;j++){
   likeArr.push(likesObj[commentsArray[i].likes[j]]);
   }
     createCommentFor("commentAreaBox",commentsArray[i],comments,likeArr,likesObj)
   }
   obj[comments[keys[i]].commentid]=comments[keys[i]];
   }

//  for(var i=0;i<keys.length;i++){
//  var comData=parentComments[keys[i]];
//  createCommentFor("commentAreaBox",comData,data,0)
//  }
return null;
 }

 function createCommentForNew(data,sortType,parentcomment){
  if(data!=undefined){
  var owner=getCookie("user");
  var message=data.comment;
  if(message==null || message==""){
  message="This Comment was Deleted";
  }
  var totlikes=data.like;
  var totdislikes=data.dislike;
  var like=null;
  var likeid=null;
  let agoTime=moment(data.created_at).fromNow();
  let code;
  agoTime+=" "+new Date(data.created_at).toLocaleTimeString('en-US');
  if(owner==data.username && message!="This Comment was Deleted" ){
  code="<div class=\"ccomment\"  id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"<span style=\"font-size: 13px;font-weight: 700;opacity: 0.5;padding-left: 7px;font-style: italic;\">"+agoTime+"</span></p><p id=\""+data.commentid+"value\" style=\"font-size: 17px;white-space: nowrap;width: 50px;text-overflow: ellipsis;font-style: italic\">"+message+"</p><div style=\"display: flex;gap: 20px;\"> <div id=\""+data.commentid+"votebox\" style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p> <p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p></div> <p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p><p id=\""+data.commentid+"edit\" onclick=\"openEditComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Edit</p><p id=\""+data.commentid+"delete\" onclick=\"openDeleteComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: red;text-decoration: underline;\">Delete</p></div></div><div id=\""+data.commentid+"childs\"></div></div>";
  }else{
 code="<div class=\"ccomment\"  id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"<span style=\"font-size: 13px;font-weight: 700;opacity: 0.5;padding-left: 7px;font-style: italic;\">"+agoTime+"</span></p><p id=\""+data.commentid+"value\" style=\"font-size: 17px;white-space: nowrap;width: 50px;text-overflow: ellipsis;font-style: italic\">"+message+"</p> <div style=\"display: flex;gap: 20px;\"><div id=\""+data.commentid+"votebox\" style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p><p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p> </div><p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p></div></div><div id=\""+data.commentid+"childs\"></div></div>";
 }
  if(parentcomment!=null){
  parentcomment+="childs";
  }
if(sortType=="top" || sortType=="default"){
 if(data.parentcomment!=undefined && data.parentcomment!=null){
 document.getElementById(parentcomment).innerHTML+=code;

 }else{
 document.getElementById("commentAreaBox").innerHTML+=code;
 }
 }else{
 if(data.parentcomment!=undefined && data.parentcomment!=null){
  document.getElementById(parentcomment).innerHTML=code+document.getElementById(parentcomment).innerHTML;
  }else{
  document.getElementById("commentAreaBox").innerHTML=code+document.getElementById("commentAreaBox").innerHTML;
  }
 }

 }
 }
 function createCommentFor(parent,data,fullData,likeArr,likesObj){
 if(data!=undefined){
 var mle=20;
 var owner=getCookie("user");
 var message=data.comment;
 if(message==null || message==""){
 message="This Comment was Deleted";
 }
 var totlikes=0;
 var totdislikes=0;
 for(var i=0;i<likeArr.length;i++){
 if(likeArr!=undefined && likeArr[i].status){
 totlikes=totlikes+1;
 }else{
 totdislikes=totdislikes+1;
 }
 }
 var like=null;
 var likeid=null;
// console.log(likeArr);
//console.log(likeArr);
 for(var i=0;i<likeArr.length;i++){
 if(likeArr[i].username==owner){
 like=likeArr[i].status;
 likeid=likeArr[i].likeid;
 }
 }
 let agoTime=moment(data.created_at).fromNow();
 agoTime+=" "+new Date(data.created_at).toLocaleTimeString('en-US');

 if(parent!="commentAreaBox"){
 parent+="childs";
 }
 if(owner==data.username && message!="This Comment was Deleted" ){
 document.getElementById(parent).innerHTML+="<div class=\"ccomment\"  id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"<span style=\"font-size: 13px;font-weight: 700;opacity: 0.5;padding-left: 7px;font-style: italic;\">"+agoTime+"</span></p><p id=\""+data.commentid+"value\" style=\"font-size: 17px;white-space: nowrap;width: 50px;text-overflow: ellipsis;font-style: italic\">"+message+"</p><div style=\"display: flex;gap: 20px;\"> <div id=\""+data.commentid+"votebox\" style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p> <p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p></div> <p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p><p onclick=\"openEditComment(\'"+data.commentid+"\',\'"+message+"\')\" id=\""+data.commentid+"edit\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Edit</p><p id=\""+data.commentid+"delete\" onclick=\"openDeleteComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: red;text-decoration: underline;\">Delete</p></div></div><div id=\""+data.commentid+"childs\"></div></div>";
 }else{
document.getElementById(parent).innerHTML+="<div class=\"ccomment\"  id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"<span style=\"font-size: 13px;font-weight: 700;opacity: 0.5;padding-left: 7px;font-style: italic;\">"+agoTime+"</span></p><p id=\""+data.commentid+"value\" style=\"font-size: 17px;white-space: nowrap;width: 50px;text-overflow: ellipsis;font-style: italic\">"+message+"</p> <div style=\"display: flex;gap: 20px;\"> <div id=\""+data.commentid+"votebox\" style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p><p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p></div><p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p></div></div><div id=\""+data.commentid+"childs\"></div></div>";
}
if(data.childcomments.length>0){
for(var j=0;j<data.childcomments.length;j++){
 var newlikeArr=[];
  if(fullData[data.childcomments[j]]!=undefined){
   for(var k=0;k<fullData[data.childcomments[j]].likes.length;k++){
   if(likesObj[fullData[data.childcomments[j]].likes[k]]!=undefined){
   newlikeArr.push(likesObj[fullData[data.childcomments[j]].likes[k]]);
   }
}
createCommentFor(data.commentid,fullData[data.childcomments[j]],fullData,newlikeArr,likesObj);

   }else{
   document.getElementById(data.commentid+"childs").innerHTML+="<p id=\""+data.commentid+"more\" onclick=\"getMoreComments('"+data.commentid+"','"+data.postid+"')\" style=\"font-size: 17px;font-style: italic;cursor: pointer;color: blue;text-decoration: underline;\">Load More</p>";
   break;
   }
}
}
}
return null;
 }
function showPost(data,postLikes){
var totlikes=0;
var totdislikes=0;
for(var i=0;i<postLikes.length;i++){
if(postLikes[i].status){
totlikes=totlikes+1;
}else{
totdislikes=totdislikes+1;
}
}
data.like=totlikes;
data.dislike=totdislikes;
  localStorage.setItem("post",JSON.stringify(data));


document.getElementById("postOwner").innerHTML=data.created_by;
let agoTime=moment(data.created_at).fromNow();
document.getElementById("postedTime").innerHTML=agoTime;
document.getElementById("postContent").innerHTML=data.content;
document.getElementById("postLikes").innerHTML=totlikes+" Likes";
document.getElementById("postDisLikes").innerHTML=totdislikes+" Dislikes";
document.getElementById("postComments").innerHTML=data.totalComments+" Comments";
 var owner=getCookie("user");
 var message=data.comment;
var like=null;
var likeid=null;
for(var i=0;i<postLikes.length;i++){
if(postLikes[i].username==owner){
like=postLikes[i].status;
likeid=postLikes[i].likeid;
}
}

 document.getElementById("LikesCont").innerHTML="<p id=\"likesdislked\" onclick=\"likepost(\'"+data.postid+"\',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikepost(\'"+data.postid+"\',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";

}


function dislikepost(postid,likeType,likeid){
if((likeType===null || likeType) && likeid=='null'){
var likeId=generateLikeId();
var data={}
data.likeid=likeId;
data.status=false;
data.postid=postid;
data.commentid=null;
data.created_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.updated_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.username=getCookie("user");
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeId]=data;
if(!localStorage.getItem("newlikes")){
var lk=[]
localStorage.setItem("newlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes"));
newlikes.push(likeId);
localStorage.setItem("newlikes",JSON.stringify(newlikes));
let post=JSON.parse(localStorage.getItem("post"));
post.dislike++;
localStorage.setItem("post",JSON.stringify(post));
localStorage.setItem("likes",JSON.stringify(likes));
likeid=likeId;
document.getElementById("postLikes").innerHTML=post.like+" Likes";
document.getElementById("postDisLikes").innerHTML=post.dislike+" Dislikes";
 document.getElementById("LikesCont").innerHTML="<p id=\"likesdislked\" onclick=\"likepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else if(likeType===null || likeType){

console.log("Already There in dislike",likeid);
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeid].status=false;

localStorage.setItem("likes",JSON.stringify(likes));
let post=JSON.parse(localStorage.getItem("post"));
post.like--;
post.dislike++;
localStorage.setItem("post",JSON.stringify(post));

if(!localStorage.getItem("editlikes")){
var lk=[]
localStorage.setItem("editlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes")) || [];
if(!newlikes.includes(likeid)){
var editlikes=JSON.parse(localStorage.getItem("editlikes"));
if(editlikes.length==0 || !editlikes.includes(likeid)){
editlikes.push(likeid);
localStorage.setItem("editlikes",JSON.stringify(editlikes));
}
}
document.getElementById("postLikes").innerHTML=post.like+" Likes";
document.getElementById("postDisLikes").innerHTML=post.dislike+" Dislikes";
 document.getElementById("LikesCont").innerHTML="<p id=\"likesdislked\" onclick=\"likepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else{
toastr["info"]("Comment Already Disliked", "Like");

}
}

function likepost(postid,likeType,likeid){
if((likeType===null || !likeType) && likeid=='null'){
var likeId=generateLikeId();
var data={}
data.likeid=likeId;
data.status=true;
data.postid=postid;
data.commentid=null;
data.created_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.updated_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.username=getCookie("user");
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeId]=data;
if(!localStorage.getItem("newlikes")){
var lk=[]
localStorage.setItem("newlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes"));
newlikes.push(likeId);
localStorage.setItem("newlikes",JSON.stringify(newlikes));
let post=JSON.parse(localStorage.getItem("post"));
post.like++;
localStorage.setItem("post",JSON.stringify(post));
localStorage.setItem("likes",JSON.stringify(likes));
likeid=likeId;
document.getElementById("postLikes").innerHTML=post.like+" Likes";
document.getElementById("postDisLikes").innerHTML=post.dislike+" Dislikes";
 document.getElementById("LikesCont").innerHTML="<p id=\"likesdislked\" onclick=\"likepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else if(likeType===null || !likeType){
console.log("Already There in dislike",likeid);
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeid].status=true;

localStorage.setItem("likes",JSON.stringify(likes));
let post=JSON.parse(localStorage.getItem("post"));
post.like++;
post.dislike--;
localStorage.setItem("post",JSON.stringify(post));
localStorage.setItem("likes",JSON.stringify(likes));
if(!localStorage.getItem("editlikes")){
var lk=[]
localStorage.setItem("editlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes")) || [];
if(!newlikes.includes(likeid)){
var editlikes=JSON.parse(localStorage.getItem("editlikes"));
if(editlikes.length==0 || !editlikes.includes(likeid)){
editlikes.push(likeid);
localStorage.setItem("editlikes",JSON.stringify(editlikes));
}
}
document.getElementById("postLikes").innerHTML=post.like+" Likes";
document.getElementById("postDisLikes").innerHTML=post.dislike+" Dislikes";
 document.getElementById("LikesCont").innerHTML="<p id=\"likesdislked\" onclick=\"likepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikepost(\'"+postid+"\',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else{
toastr["info"]("Comment Already Liked", "Like");
}
}


function likecomment(postid,commentid,likeType,likeid){
if((likeType===null || !likeType) && likeid=='null'){
var likeId=generateLikeId();
var data={}
data.likeid=likeId;
data.status=true;
data.postid=postid;
data.commentid=commentid;
data.created_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.updated_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.username=getCookie("user");
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeId]=data;
if(!localStorage.getItem("newlikes")){
var lk=[]
localStorage.setItem("newlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes"));
newlikes.push(likeId);
localStorage.setItem("newlikes",JSON.stringify(newlikes));
let comments=JSON.parse(localStorage.getItem("comments"));
comments[commentid].like++;
localStorage.setItem("comments",JSON.stringify(comments));
localStorage.setItem("likes",JSON.stringify(likes));
likeid=likeId;
document.getElementById(commentid+"votebox").innerHTML="<p>"+comments[commentid].like+" Likes</p> <p>"+comments[commentid].dislike+" Dislikes</p><p  onclick=\"likecomment(\'"+postid+"\','"+commentid+"',"+data.status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+postid+"\','"+commentid+"',"+data.status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else if(likeType===null || !likeType){
console.log("Already There in dislike",likeid);
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeid].status=true;

localStorage.setItem("likes",JSON.stringify(likes));
let comments=JSON.parse(localStorage.getItem("comments"));
comments[commentid].like++;
comments[commentid].dislike--;
localStorage.setItem("comments",JSON.stringify(comments));
if(!localStorage.getItem("editlikes")){
var lk=[]
localStorage.setItem("editlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes")) || [];
if(!newlikes.includes(likeid)){
var editlikes=JSON.parse(localStorage.getItem("editlikes"));
if(editlikes.length==0 || !editlikes.includes(likeid)){
editlikes.push(likeid);
localStorage.setItem("editlikes",JSON.stringify(editlikes));
}
}
document.getElementById(commentid+"votebox").innerHTML="<p>"+comments[commentid].like+" Likes</p> <p>"+comments[commentid].dislike+" Dislikes</p><p  onclick=\"likecomment(\'"+postid+"\','"+commentid+"',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+postid+"\','"+commentid+"',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";

}else{
toastr["info"]("Comment Already Liked", "Like");
}
}



function dislikecomment(postid,commentid,likeType,likeid){

if((likeType===null || likeType) && likeid=='null'){

var likeId=generateLikeId();
var data={}
data.likeid=likeId;
data.status=false;
data.postid=postid;
data.commentid=commentid;
data.created_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.updated_at=new Date().toISOString().replace('T',' ').replace('Z','+05:30');
data.username=getCookie("user");
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeId]=data;
likeid=likeId;
let comments=JSON.parse(localStorage.getItem("comments"));
comments[commentid].dislike++;
if(!localStorage.getItem("newlikes")){
var lk=[]
localStorage.setItem("newlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes"));
newlikes.push(likeId);
localStorage.setItem("newlikes",JSON.stringify(newlikes));
localStorage.setItem("comments",JSON.stringify(comments));
document.getElementById(commentid+"votebox").innerHTML="<p>"+comments[commentid].like+" Likes</p> <p>"+comments[commentid].dislike+" Dislikes</p><p  onclick=\"likecomment(\'"+postid+"\','"+commentid+"',"+data.status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+postid+"\','"+commentid+"',"+data.status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
localStorage.setItem("likes",JSON.stringify(likes));
}else if(likeType===null || likeType){
console.log("Already There in like",likeid);
let likes=JSON.parse(localStorage.getItem("likes"));
likes[likeid].status=false;
localStorage.setItem("likes",JSON.stringify(likes));
let comments=JSON.parse(localStorage.getItem("comments"));
comments[commentid].like--;
comments[commentid].dislike++;
localStorage.setItem("comments",JSON.stringify(comments));
if(!localStorage.getItem("editlikes")){
var lk=[]
localStorage.setItem("editlikes",JSON.stringify(lk));
}
var newlikes=JSON.parse(localStorage.getItem("newlikes")) || [];
if(!newlikes.includes(likeid)){
var editlikes=JSON.parse(localStorage.getItem("editlikes"));
if(editlikes.length==0 || !editlikes.includes(likeid)){
editlikes.push(likeid);
localStorage.setItem("editlikes",JSON.stringify(editlikes));
}
}
document.getElementById(commentid+"votebox").innerHTML="<p>"+comments[commentid].like+" Likes</p> <p>"+comments[commentid].dislike+" Dislikes</p><p  onclick=\"likecomment(\'"+postid+"\','"+commentid+"',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+postid+"\','"+commentid+"',"+likes[likeid].status+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p>";
}else{
toastr["info"]("Comment Already Disliked", "Like");
}
}

function getPostDetail(){
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let postId=querySearch;
let data=[];
let val;
if(document.getElementById("sortType")){
val=document.getElementById("sortType").value;
}else{
val="default";
}

//console.log("Getting All Conversations");
var dep=4;
if(document.getElementById("depth")){
dep=document.getElementById("depth").value;
}
localStorage.removeItem("likes");
localStorage.removeItem("comments");
localStorage.removeItem("newlikes");
localStorage.removeItem("editlikes");
axios.get('/getPost',{
params:{
"postid":postId,
"sort_type":val,
"parentcomment":null,
"depth":dep
}
})
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data.data;

  let dat={}
   let postLikes=[];


     var likesobj=response.data.data.likesobj || {};
     localStorage.setItem("likes",JSON.stringify(likesobj));
    for(var i=0;i<data.post.likes.length;i++){
    if(likesobj[data.post.likes[i]]){
    postLikes.push(likesobj[data.post.likes[i]]);
    }
    }
  showPost(data.post,postLikes);
  setTimeout(checkBatchSend,5000);
//  data.comments=Object.values(data.comments);
  if(Object.values(data.comments).length === 0){
    document.getElementById("commentAreaBox").innerHTML="<div id=\"nocomments\" style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Comments Found</p></div>";
    val=document.getElementById("sortType").style.display="none";
  }else{
  localStorage.setItem("comments",JSON.stringify(data.comments));
  document.getElementById("sortType").style.display="block";
  data=data;
//response.data.data.comments.sort(custom_sort);
showparentcomment(data.post.comments,data.comments,likesobj);
//showComments(response.data.data.comments,likesobj);


//if(localStorage.getItem("scrollY")){
//document.getElementById("messageArea").scrollTop=localStorage.getItem("scrollY");
//localStorage.removeItem("scrollY");
//}
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}
function custom_sort(a, b) {
    return new Date(a.created_at).getTime() - new Date(b.created_at).getTime();
}

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function openCreateComment(){
document.getElementById("modalArea").style.display="block";
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

function closeCreateComment(){
document.getElementById("modalArea").style.display="none";
document.getElementById("createPostInput").value="";
}

function openChildComment(commentId){
document.getElementById("modalChildArea").style.display="block";
document.getElementById("childCommentId").value=commentId;
}

function closeChildCreateComment(){
document.getElementById("modalChildArea").style.display="none";
document.getElementById("createChildPostInput").value="";
document.getElementById("childCommentId").value="";
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






function openEditComment(commentId,comment){
document.getElementById("modalEditArea").style.display="block";
document.getElementById("editCommentId").value=commentId;
//localStorage.setItem("scrollY", document.getElementById("messageArea").scrollTop);
//console.log(document.getElementById("messageArea").scrollTop);
document.getElementById("EditCommentInput").value=comment;
}

function closeEditComment(){
document.getElementById("modalEditArea").style.display="none";
document.getElementById("EditCommentInput").value="";
//localStorage.removeItem("scrollY", document.getElementById("messageArea").scrollTop);
document.getElementById("editCommentId").value="";
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



function openDeleteComment(commentId,comment){
document.getElementById("modalDeleteArea").style.display="block";
document.getElementById("deleteCommentId").value=commentId;
document.getElementById("deleteCommentInput").value=comment;
//localStorage.setItem("scrollY", document.getElementById("messageArea").scrollTop);
}

function closeDeleteComment(){
document.getElementById("modalDeleteArea").style.display="none";
document.getElementById("deleteCommentInput").value="";
//localStorage.removeItem("scrollY", document.getElementById("messageArea").scrollTop);
document.getElementById("deleteCommentId").value="";
}

function deleteComment(){
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let postId=querySearch;
let commentid=document.getElementById("deleteCommentId").value;
if(postId!="" && commentid!=""){
const params = new URLSearchParams();
params.append('postid', postId);
params.append('commentid',commentid);
axios.post('/deleteComment',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
      document.getElementById("modalDeleteArea").style.display="none";
      document.getElementById("deleteCommentInput").value="";
      var edic=document.getElementById(commentid+"edit");
      var delc=document.getElementById(commentid+"delete")
      if(edic){
      edic.remove();
      }
      if(delc){
      delc.remove();
      }
      document.getElementById(commentid+"value").innerHTML="This Comment was Deleted";
      document.getElementById("deleteCommentId").value="";
//getPostDetail();
  }})
  .catch(function (error) {
    console.log(error);
  });
}
}



function getMoreComments(commentid,postid){
let val;
if(document.getElementById("sortType")){
val=document.getElementById("sortType").value;
}else{
val="new";
}
axios.get('/getPost',{
params:{
"postid":postid,
"sort_type":val,
"parentcomment":commentid,
"depth":4
}
})
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);
      }else{
      document.getElementById("depth").value=Number(document.getElementById("depth").value)+4;
  data=response.data.data;
  let dat={}
   let postLikes=[];
     var likesobj=response.data.data.likesobj || {};
  data=data;
  let parent=data.parentcomment;

appendcommet(commentid,data.comments,likesobj,parent);
document.getElementById(commentid+"more").style.display="none";

//if(localStorage.getItem("scrollY")){
//document.getElementById("messageArea").scrollTop=localStorage.getItem("scrollY");
//localStorage.removeItem("scrollY");
//}

}
  })
  .catch(function (error) {
    console.log(error);
  });
}


function appendcommet(commentid,commentOBJ,likeObj,parent){
for(var j=0;j<parent.childcomments.length;j++){
var newlikeArr=[];
  if(commentOBJ[parent.childcomments[j]]!=undefined){
   for(var k=0;k<commentOBJ[parent.childcomments[j]].likes.length;k++){
   if(likeObj[commentOBJ[parent.childcomments[j]].likes[k]]!=undefined){
   newlikeArr.push(likeObj[commentOBJ[parent.childcomments[j]].likes[k]]);
   }
}
   }

createCommentFor(commentid,commentOBJ[parent.childcomments[j]],commentOBJ,newlikeArr,likeObj);
}
}



function generateLikeId(){
var owner=getCookie("user");
var result='';
result=result+owner+",Like,";
 var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for ( var j = 0; j < 7; j++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
return result;
}



function checkBatchSend(){
var edits=[];
var newlikes=[];
if(localStorage.getItem("editlikes")){
edits=JSON.parse(localStorage.getItem("editlikes"));
}
if(localStorage.getItem("newlikes")){
newlikes=JSON.parse(localStorage.getItem("newlikes"));
}
if(edits.length>0){
console.log("Edit Likes Batch");
console.log(edits);
batchUpdate(edits,"edit");
}
if(newlikes.length>0){
console.log("New Likes Batch");
console.log(newlikes);
batchUpdate(newlikes,"new");
}
setTimeout(checkBatchSend,5000);
}


function batchUpdate(ids,type){
let likeObj=JSON.parse(localStorage.getItem("likes"));
let data=[]
for(var i=0;i<ids.length;i++){
data.push(likeObj[ids[i]]);
}
console.log(data);
const params = new URLSearchParams();
params.append('data', JSON.stringify(data));
params.append('type', type);
axios.post('/batchLikes',params)
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);
      }else{
      console.log(response);
      if(type=="edit"){
      localStorage.removeItem("editlikes");
      }else{
      localStorage.removeItem("newlikes");
      }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}