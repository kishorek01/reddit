

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
  if (user != "") {
    document.location = "home.html";
  } else {
  document.location = "login.html";
//    user = prompt("Please enter your name:", "");
//    if (user != "" && user != null) {
//      setCookie("username", user, 1);
//    }
  }
}
checkCookie();
//setCookie("username","kishore",1)