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

function showComments(data){
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
     createCommentFor("commentAreaBox",commentsArray[i],data,0)
   }
   obj[commentsArray[i].commentid]=commentsArray[i];
   }

//  for(var i=0;i<keys.length;i++){
//  var comData=parentComments[keys[i]];
//  createCommentFor("commentAreaBox",comData,data,0)
//  }
 }
 function createCommentFor(parent,data,fullData,mlef){
 var mle=50*mlef;
 var owner=getCookie("user");
 var message=data.comment;
 if(message==null || message==""){
 message="This Comment was Deleted";
 }
 if(owner==data.username && message!="This Comment was Deleted" ){
 document.getElementById(parent).innerHTML+="<div style=\"margin-left:"+mle+"px;\" id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"</p><p style=\"font-size: 17px;font-style: italic\">"+message+"</p> <div style=\"    display: flex;gap: 20px;\"><p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p><p onclick=\"openEditComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Edit</p><p onclick=\"openDeleteComment(\'"+data.commentid+"\',\'"+message+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: red;text-decoration: underline;\">Delete</p></div></div></div>";
 }else{
document.getElementById(parent).innerHTML+="<div style=\"margin-left:"+mle+"px;\" id=\""+data.commentid+"\"><div class=\"postsComments\"><p style=\"font-size: 18px;font-weight: 600;font-style: italic;\">"+data.username+"</p><p style=\"font-size: 17px;font-style: italic\">"+message+"</p> <div><p onclick=\"openChildComment(\'"+data.commentid+"\')\" style=\"font-size: 17px;font-style: italic;cursor:pointer;color: blue;text-decoration: underline;\">Reply</p></div></div></div>";
}
if(data.childcomments.length>0){
mlef=mlef+1;
for(var j=0;j<data.childcomments.length;j++){
createCommentFor(data.commentid,fullData[data.childcomments[j]],fullData,mlef);
}
}
 }
function showPost(data){

document.getElementById("postOwner").innerHTML=data.created_by;
let agoTime=moment(data.created_at).fromNow();
document.getElementById("postedTime").innerHTML=agoTime;
document.getElementById("postContent").innerHTML=data.content;
document.getElementById("postLikes").innerHTML=data.likes.length+" Likes";
document.getElementById("postComments").innerHTML=data.comments.length+" Comments";
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
showPost(response.data.data.post)
if(response.data.data.post.comments.length==0){
document.getElementById("commentAreaBox").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Comments Found</p></div>";
}
else{
//response.data.data.comments.sort(custom_sort);

showComments(response.data.data.comments);
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
      document.getElementById("createPostInput").value="";
window.location.reload();

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
      document.getElementById("createChildPostInput").value="";
      document.getElementById("childCommentId").value="";
window.location.reload();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}






function openEditComment(commentId,comment){
document.getElementById("modalEditArea").style.display="block";
document.getElementById("editCommentId").value=commentId;
document.getElementById("EditCommentInput").value=comment;
}

function closeEditComment(){
document.getElementById("modalEditArea").style.display="none";
document.getElementById("EditCommentInput").value="";
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
      document.getElementById("EditCommentInput").value="";
      document.getElementById("editCommentId").value="";
window.location.reload();

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
}

function closeDeleteComment(){
document.getElementById("modalDeleteArea").style.display="none";
document.getElementById("deleteCommentInput").value="";
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
      document.getElementById("deleteCommentInput").value="";
      document.getElementById("deleteCommentId").value="";
window.location.reload();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}