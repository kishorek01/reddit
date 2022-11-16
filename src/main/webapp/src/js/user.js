function login(){
console.log("Login");
const username=document.getElementById("InputUserName").value;
const password=document.getElementById("InputPassword").value;
console.log(username,password);
if(username!="" && password!=""){
axios.get('/login', {
params:{
    username: username,
    password: password
    }
  })
  .then(function (response) {
    if(response.data){
    if(response.data.code==203){
    toastr.options = {
      "closeButton": true,
      "debug": false,
      "newestOnTop": false,
      "progressBar": true,
      "positionClass": "toast-top-right",
      "preventDuplicates": true,
      "onclick": null,
      "showDuration": "300",
      "hideDuration": "1000",
      "timeOut": "5000",
      "extendedTimeOut": "1000",
      "showEasing": "swing",
      "hideEasing": "linear",
      "showMethod": "fadeIn",
      "hideMethod": "fadeOut"
    }
toastr["error"](response.data.data.message, "Error");
}else if(response.data.code==200){
toastr.options = {
  "closeButton": true,
  "debug": false,
  "newestOnTop": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "preventDuplicates": false,
  "onclick": null,
  "showDuration": "300",
  "hideDuration": "1000",
  "timeOut": "5000",
  "extendedTimeOut": "1000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}
const data=response.data.data.data;
if(data){
console.log(document.cookie);
    setCookie("username",data.username);
    document.location="home.html";
    console.log("Cookie Created");
}

toastr["success"](response.data.data.message, "Success");
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}else if(username==""){
toastr["error"]("Enter your UserName", "Error");
}else if(password==""){
toastr["error"]("Enter Your Password","Error");
}
}


function register(){
console.log("Register");
const name=document.getElementById("InputName").value;
const email=document.getElementById("InputEmail").value;
const username=document.getElementById("InputUserName").value;
const password=document.getElementById("InputPassword").value;
console.log(name,username,email,password);
if(username!="" && password!="" && email!="" && name!=""){
if(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)){
const params = new URLSearchParams();
params.append('username', username);
params.append('email', email);
params.append('name', name);
params.append('password', password);
axios.post('/registerUser',params)
  .then(function (response) {
    if(response.data){
    if(response.data.code==201){
    toastr.options = {
      "closeButton": true,
      "debug": false,
      "newestOnTop": false,
      "progressBar": true,
      "positionClass": "toast-top-right",
      "preventDuplicates": true,
      "onclick": null,
      "showDuration": "300",
      "hideDuration": "1000",
      "timeOut": "5000",
      "extendedTimeOut": "1000",
      "showEasing": "swing",
      "hideEasing": "linear",
      "showMethod": "fadeIn",
      "hideMethod": "fadeOut"
    }
toastr["error"](response.data.data.message, "Error");
}else if(response.data.code==200){
toastr.options = {
  "closeButton": true,
  "debug": false,
  "newestOnTop": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "preventDuplicates": false,
  "onclick": null,
  "showDuration": "300",
  "hideDuration": "1000",
  "timeOut": "5000",
  "extendedTimeOut": "1000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}
const data=response.data.data.data;
if(data){
console.log(document.cookie);
//    document.location="home.html";
    console.log("Account Created");
    toastr["success"](response.data.data.message, "Success");
    document.location="login.html";

}

}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
  }else{
  toastr["error"]("Enter Valid Email","Error");
  }
}else if(name==""){
 toastr["error"]("Enter Your Name","Error");
}else if(username==""){
toastr["error"]("Enter your UserName", "Error");
}else if(email==""){
 toastr["error"]("Enter Your Email","Error");
}else if(password==""){
toastr["error"]("Enter Your Password","Error");
}
}

function setCookie(cname, cvalue) {
  const d = new Date();
  exdays=1;
//  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));//days
d.setTime(d.getTime() + (10000));
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

