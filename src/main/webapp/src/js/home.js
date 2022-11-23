
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


function openPost(postId){
//console.log("Opening post "+postId);
let urlParams =window.origin+"/post.html?id="+postId;
//console.log(urlParams);
window.location.href=urlParams;
//alert(postId);
}

function editPost(postId,data){
//console.log("Editing post "+postId);
document.getElementById("editArea").style.display= "block";
document.getElementById("editPostInput").value=data;
document.getElementById("editScan").innerHTML="<button onclick=\"closeEditModel()\" class=\"cancelBtn\">Cancel</button>";
document.getElementById("editScan").innerHTML+="<button onclick=\"confirmEdit(\'"+postId+"\')\" class=\"publishPost\">Publish Again</button>"
}

function deletePost(postId,data){
//console.log("Deleting post "+postId);
document.getElementById("deleteArea").style.display= "block";
document.getElementById("deletePostInput").value=data;
document.getElementById("deleteScan").innerHTML="<button onclick=\"closeDeleteModel()\" class=\"cancelBtn\">Cancel</button>";
document.getElementById("deleteScan").innerHTML+="<button onclick=\"confirmDelete(\'"+postId+"\')\" class=\"publishPost\">Delete</button>"
}

function confirmDelete(postId){
//console.log(postId);
if(postId!=""){
const params = new URLSearchParams();
params.append('postid', postId);
axios.post('/deletePost',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
window.location.reload();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}

function confirmEdit(postId){
var data=document.getElementById("editPostInput").value;
//console.log(postId);
if(postId!="" && data!=""){
const params = new URLSearchParams();
params.append('postid', postId);
params.append('content',data);
axios.post('/editPost',params)
  .then(function (response) {
    if(response.data.code==500 || response.data.code!=200){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
window.location.reload();

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}
function closeDeleteModel(){
//console.log("Closing Delete Model");
document.getElementById("deleteArea").style.display="none";
document.getElementById("deletePostInput").value="";
}
function closeEditModel(){
//console.log("Closing Delete Model");
document.getElementById("editArea").style.display="none";
document.getElementById("editPostInput").value="";
}

function getAllPosts(){
let data=[];
//console.log("Getting All Posts");
let val;
if(document.getElementById("sortType")){
val=document.getElementById("sortType").value;
}else{
val="new";
}
axios.get('/getAllPosts',{params:{"sort_type":val}})
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data.data;
//  console.log(data);
  if(data.length==0){
    document.getElementById("postArea").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Posts Found</p></div>";
    }else
    {
    document.getElementById("postArea").innerHTML="";
//      data.sort(custom_sort);

  for(var i=0;i<data.length;i++){
  var postId=data[i].postid;
//  var date=new Date(data[i].created_at).toLocaleDateString('en-us', { year:"numeric", month:"short", day:"numeric"});
//  console.log(date);
//  const time = new Date(data[i].created_at).toLocaleTimeString('en-US');
//  console.log(time);
  let agoTime=moment(data[i].created_at).fromNow();
  document.getElementById("postArea").innerHTML+="<div id=\""+postId+"\" onclick=\"openPost(\'"+postId+"\')\" class=\"posts\"><h3>"+data[i].created_by+"</h3><span>"+agoTime+"</span><p class=\"postContent\">"+data[i].content+"</p><div class=\"details\"><p>"+data[i].countLike+" Likes</p><p>"+data[i].comments.length+" Comments</p></div></div>";
  }
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}

function getMyPosts(){
let data=[];
//console.log("Getting My Posts");
axios.get('/getMyPosts')
  .then(function (response) {
  if(response.data.code==500){
  toastr["error"](response.data.data.message, "Error");
//  console.log(response.data.data.message);
  setTimeout(function(){
  document.location="login.html";
  },3000);

  }else{
  data=response.data.data.data;
//    console.log(data);

//  console.log(data.length)
  if(data.length==0){
  document.getElementById("postArea").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Posts Found</p></div>";
  }else
  {
//    data.sort(custom_sort);

  for(var i=0;i<data.length;i++){
  data[i].content = data[i].content.replace(/(\r\n|\n|\r)/gm, "");

  var postId=data[i].postid;
//  var date=new Date(data[i].created_at).toLocaleDateString('en-us', { year:"numeric", month:"short", day:"numeric"});
//  console.log(date);
//  const time = new Date(data[i].created_at).toLocaleTimeString('en-US');
//  console.log(time);
  let agoTime=moment(data[i].created_at).fromNow();
  document.getElementById("postArea").innerHTML+="<div style=\"cursor: default;\" id=\""+postId+"\" class=\"posts\"><h3>"+data[i].created_by+"</h3><span>"+agoTime+"</span><p class=\"postContent\">"+data[i].content+"</p><div class=\"details\"><p>"+data[i].countLike+" Likes</p><p>"+data[i].comments.length+" Comments</p><p class=\"edit\" onclick=\"editPost(\'"+postId+"\',\'"+data[i].content+"\')\">Edit</p> <p class=\"delete\" onclick=\"deletePost(\'"+postId+"\',\'"+data[i].content+"\')\">Delete</p></div></div>";
  }
  }
  }

  })
  .catch(function (error) {
    console.log(error);
  });
}
function custom_sort(a, b) {
    return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
}



function openCreateModel(){
//console.log("Creating the post");
document.getElementById("modalArea").style.display = "block";
}

function closeCreateModel(){
//console.log("Cancelling the post");
document.getElementById("modalArea").style.display = "none";
document.getElementById("createPostInput").value = "";
}


function publish(){
//console.log("Cancelling the post");
var post=document.getElementById("createPostInput").value;
post=post.replace("'", "`");
//console.log(post);
if(post!=""){
const params = new URLSearchParams();
params.append('content', post);
axios.post('/createPost',params)
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data;
  if(data.code==200){
//  console.log(data);
  data=data.data.data;
  toastr["success"](response.data.data.message, "Success");
  document.getElementById("modalArea").style.display = "none";
  document.getElementById("createPostInput").value = "";
  let agoTime=moment(data.created_at).fromNow();
  window.location.reload();
  var postId=data.postid;
document.getElementById("postArea").innerHTML="<div style=\"cursor: default;\" id=\""+postId+"\" class=\"posts\"><h3>"+data.created_by+"</h3><span>"+agoTime+"</span><p class=\"postContent\">"+data.content+"</p><div class=\"details\"><p>"+data.countLike+" Likes</p><p>"+data.comments.length+" Comments</p><p class=\"edit\" onclick=\"editPost(\'"+postId+"\')\">Edit</p> <p class=\"delete\" onclick=\"deletePost(\'"+postId+"\')\">Delete</p></div></div>"+document.getElementById("postArea").innerHTML;
  }

  }})
  .catch(function (error) {
    console.log(error);
  });
}
}