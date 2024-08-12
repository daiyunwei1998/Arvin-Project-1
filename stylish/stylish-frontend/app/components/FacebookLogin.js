"use client"
import React, { useEffect, useState } from 'react';
import { FacebookLoginButton } from "react-social-login-buttons";

export default function FacebookLogin() {
  const [response, setResponse] = useState();
  /* post request to stylish backend with auth data */
  const handlePostAccessToken = async (fbResponse) => {

    const { graphDomain: provider, accessToken: access_token } = fbResponse.authResponse;
    const authData = {
      provider: provider,
      access_token: access_token
    };

    try {
      const response = await fetch('api/1.0/user/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(authData)
      });
  
      console.log("(develop) Sending request body with: " + JSON.stringify(authData));
      
      if (!response.ok) {
        // Handle HTTP errors
        const errorData = await response.json();
        console.error("Error response from backend: ", errorData);
      } else {
        // Handle successful response
        const responseData = await response.json();
        console.log("Received backend response: ", responseData);
        document.cookie = `jwt=${responseData.data.access_token}; path=/; max-age=${responseData.data.access_expired}`;
        // Redirect to /profile
        window.location.href = '/profile';
      }
    } catch (error) {
      // Handle fetch or network errors
      console.error("Network or fetch error: ", error);
    }
  }
  /*  handle facebook login   */
  useEffect(() => {
    // Function to initialize Facebook SDK
    function initFacebookSdk() {
      window.fbAsyncInit = function() {
        FB.init({
          appId: "1150158639614854", 
          cookie: true,
          xfbml: true,
          version: "v12.0"  // Ensure to use a valid version of Facebook SDK
        });
        FB.AppEvents.logPageView();   
        
        // Now that FB SDK is initialized, call getLoginStatus
        FB.getLoginStatus(function(response) {
          console.log('[refreshLoginStatus]', response);
          setResponse(response);
        });
      };

      // Load the Facebook SDK asynchronously
      (function(d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) { return; }
        js = d.createElement(s); js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
      }(document, 'script', 'facebook-jssdk'));
    }

    // Check if FB SDK is already loaded
    if (!window.FB) {
      // If not loaded, initialize FB SDK
      initFacebookSdk();
    }
  }, []); // Empty dependency array ensures this effect runs only once after initial render


  const handleFBLogin = () => {
    // 跳出 Facebook 登入的對話框
    window.FB.login(
      function (response) {
        setResponse(response);
        handlePostAccessToken(response);
      },
      { scope: 'public_profile,email' }
    );
  };

  const handleFBLogout = () => {
    window.FB.logout(function (response) {
      console.log('handleFBLogout', response);
      setResponse(response);
    });
  };


  return (
      <div>
      <FacebookLoginButton onClick={handleFBLogin} />
        {/* <div>
     <button onClick={handleFBLogout}>Facebook Logout</button>
   </div> */}
    </div>
   
  );
}
