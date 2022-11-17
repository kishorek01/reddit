function setCookie(cname, cvalue) {
  const d = new Date();
  exdays=1;
//  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));//days
d.setTime(d.getTime() + (60*60*1000));
  let expires = "expires="+d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires ;
}
function getCookie(cname) {
  let name = cname + "=";
  let ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
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

function checkCookie() {
  let user = getCookie("username");
  if (user != "" && user!=null) {
    setCookie("username",user);
  } else {
  document.location = "login.html";
  }
}


function logout(){
let user = getCookie("username");
document.cookie=document.cookie+";max-age=0";
    cookie1=document.cookie;
    console.log("Logging Out");
    user = getCookie("username");
        if (user != "" && user!=null) {
          console.log("Cookie Not Deleted");
        } else {
        console.log("Cookie Deleted");
        document.location = "login.html";
        }
}

function startTime() {
    console.log("Loop Started");
  let user = getCookie("username");
    if (user != "" && user!=null) {
      console.log("Cookie Active");
    } else {
    console.log("Cookie Expired");
    document.location = "login.html";
    }
  setTimeout(function() {startTime()}, 1000);
}

//startTime();


function openPost(postId){
console.log("Opening post "+postId);
alert(postId);
}

function getAllPosts(){
let data=[];
console.log("Getting All Posts");
axios.get('/getAllPosts')
  .then(function (response) {
  data=response.data.data;
  data.sort(custom_sort);
  console.log(data);
  for(var i=0;i<data.length;i++){
  var postId=data[i].postid;
  document.getElementById("postArea").innerHTML+="<div id=\""+postId+"\" onclick=\"openPost(\'"+postId+"\')\" class=\"posts\"><h3>"+data[i].created_by+"</h3><p class=\"postContent\">"+data[i].content+"</p><div class=\"details\"><p>"+data[i].likes.length+" Likes</p><p>"+data[i].comments.length+" Comments</p></div></div>";
  }

  })
  .catch(function (error) {
    console.log(error);
  });
}
function custom_sort(a, b) {
    return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
}

