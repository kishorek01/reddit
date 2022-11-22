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
   commentsArray.sort(custom_sort);
   var obj={}
   var keys=Object.keys(parentComments);
     var owner=getCookie("user");
   for(var i=0;i<commentsArray.length;i++){
   if(commentsArray[i].commentid.includes("Post")){
   var likeArr=[];
   for(var j=0;j<commentsArray[i].likes.length;j++){
   likeArr.push(likesObj[commentsArray[i].likes[j]]);
   }
     createCommentFor("commentAreaBox",commentsArray[i],data,0,likeArr,likesObj)
   }
   obj[commentsArray[i].commentid]=commentsArray[i];
   }

//  for(var i=0;i<keys.length;i++){
//  var comData=parentComments[keys[i]];
//  createCommentFor("commentAreaBox",comData,data,0)
//  }
 }
 function createCommentFor(parent,data,fullData,mlef,likeArr,likesObj){
 var mle=50*mlef;
 var owner=getCookie("user");
 var message=data.comment;
 if(message==null || message==""){
 message="This Comment was Deleted";
 }
 var totlikes=0;
 var totdislikes=0;
 if(likeArr){
 for(var i=0;i<likeArr.length;i++){
 if(likeArr[i].status){
 totlikes=totlikes+1;
 }else{
 totdislikes=totdislikes+1;
 }
 }
 }
 var like=null;
 var likeid=null;
// console.log(likeArr);
 if(likeArr){
 for(var i=0;i<likeArr.length;i++){
 if(likeArr[i].username==owner){
 like=likeArr[i].status;
 likeid=likeArr[i].likeid;
 }
 }
 }
 if(owner==data.username && message!="This Comment was Deleted" ){
 document.getElementById(parent).innerHTML+="<div style=\"margin-left:"+mle+"px;\" id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"</p><p style=\"font-size: 17px;font-style: italic\">"+message+"</p> <div style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p> <p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p> <p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p><p onclick=\"openEditComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Edit</p><p onclick=\"openDeleteComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: red;text-decoration: underline;\">Delete</p></div></div></div>";
 }else{
document.getElementById(parent).innerHTML+="<div style=\"margin-left:"+mle+"px;\" id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"</p><p style=\"font-size: 17px;font-style: italic\">"+message+"</p> <div style=\"display: flex;gap: 20px;\"><p>"+totlikes+" Likes</p><p>"+totdislikes+" Dislikes</p><p  onclick=\"likecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Like</p><p onclick=\"dislikecomment(\'"+data.postid+"\','"+data.commentid+"',"+like+",'"+likeid+"')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">UnLike</p><p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p></div></div></div>";
}
if(data.childcomments.length>0){
mlef=mlef+1;
for(var j=0;j<data.childcomments.length;j++){
 var newlikeArr=[];

   for(var k=0;k<fullData[data.childcomments[j]].likes.length;k++){
   newlikeArr.push(likesObj[fullData[data.childcomments[j]].likes[k]]);
   }
//console.log(newlikeArr)
createCommentFor(data.commentid,fullData[data.childcomments[j]],fullData,mlef,newlikeArr,likesObj);
}
}
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

document.getElementById("postOwner").innerHTML=data.created_by;
let agoTime=moment(data.created_at).fromNow();
document.getElementById("postedTime").innerHTML=agoTime;
document.getElementById("postContent").innerHTML=data.content;
document.getElementById("postLikes").innerHTML=totlikes+" Likes";
document.getElementById("postDisLikes").innerHTML=totdislikes+" Dislikes";
document.getElementById("postComments").innerHTML=data.comments.length+" Comments";
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
getPostDetail();}else{
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
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Disliked Successfully", "Success");
getPostDetail();
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
}

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
getPostDetail();}else{
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
axios.post('/editLike',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
let convId=response.data.data.data;
toastr["success"]("Liked Successfully", "Success");
getPostDetail();
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
getPostDetail();}else{
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
getPostDetail();
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
getPostDetail();}else{
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
getPostDetail();
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

function getPostDetail(){
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let postId=querySearch;
let data=[];
//console.log("Getting All Conversations");
axios.get('/getPost',{
params:{
postid:postId
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
//  console.log(data)
  if(data.comments.length === 0){
    document.getElementById("messageArea").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Messages Found</p></div>";
  }else{
  data=data.data;
  console.log(response.data.data);
  let likesPost=response.data.data.likes;
  var likesobj=response.data.data.likesobj;
  let postLikes=[];
  for(var i=0;i<likesPost.length;i++){
//  console.log(likesPost[i].commentid)
  if(!likesPost[i].commentid){
  postLikes.push(likesPost[i]);
  }
  }
//  console.log(postLikes)
showPost(response.data.data.post,postLikes);
if(response.data.data.post.comments.length==0){
document.getElementById("commentAreaBox").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Comments Found</p></div>";
}
else{
//response.data.data.comments.sort(custom_sort);

showComments(response.data.data.comments,likesobj);
if(localStorage.getItem("scrollY")){
document.getElementById("messageArea").scrollTop=localStorage.getItem("scrollY");
localStorage.removeItem("scrollY");
}
  }
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
      document.getElementById("modalArea").style.display="none";
      document.getElementById("createPostInput").value="";
getPostDetail();

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
getPostDetail();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}






function openEditComment(commentId,comment){
document.getElementById("modalEditArea").style.display="block";
document.getElementById("editCommentId").value=commentId;
localStorage.setItem("scrollY", document.getElementById("messageArea").scrollTop);
console.log(document.getElementById("messageArea").scrollTop);
document.getElementById("EditCommentInput").value=comment;
}

function closeEditComment(){
document.getElementById("modalEditArea").style.display="none";
document.getElementById("EditCommentInput").value="";
localStorage.removeItem("scrollY", document.getElementById("messageArea").scrollTop);
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
getPostDetail();

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
localStorage.setItem("scrollY", document.getElementById("messageArea").scrollTop);
}

function closeDeleteComment(){
document.getElementById("modalDeleteArea").style.display="none";
document.getElementById("deleteCommentInput").value="";
localStorage.removeItem("scrollY", document.getElementById("messageArea").scrollTop);
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
      document.getElementById("deleteCommentId").value="";
getPostDetail();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}