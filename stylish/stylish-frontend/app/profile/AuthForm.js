"use client"
import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import FacebookLogin from '../components/FacebookLogin';

const AuthForm = () => {
  const [isSignUp, setIsSignUp] = useState(false);

  const handleSubmit = async (values) => {
    try {
      const endpoint = isSignUp 
        ? 'https://stylish.yunweidai.net/api/1.0/user/signup' 
        : 'https://stylish.yunweidai.net/api/1.0/user/signin';
        
      // Construct the request body based on sign-up or sign-in
      const body = isSignUp 
        ? JSON.stringify({
            name: values.name,
            email: values.email,
            password: values.password
          })
        : JSON.stringify({
            provider: 'native',
            email: values.email,
            password: values.password
          });

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: body,
        cache: 'no-store' 
      });

      const data = await response.json();
      console.log(data);
      if (response.ok) {
        if (isSignUp) {
          message.success('Sign Up successful!');
            // Store the access token in a cookie
           console.log("jwt is " + data.data.access_token);
	    document.cookie = `jwt=${data.data.access_token}; path=/; max-age=${data.data.access_expired}`;
            // Redirect to /profile
            window.location.href = '/profile';
        } else {
          message.success('Sign In successful!');
           // Store the access token in a cookie
          document.cookie = `jwt=${data.data.access_token}; path=/; max-age=${data.data.access_expired}`;
        // Redirect to /profile
        window.location.href = '/profile';
	}
      } else {
        message.error(data.error || 'An error occurred!');
      }
    } catch (error) {
      message.error('An error occurred!');
      console.error('Sign In error:', error);
    }
  };

  const toggleForm = () => {
    setIsSignUp(!isSignUp);
  };

  return (
    <div style={{ maxWidth: 400, margin: 'auto', padding: '20px' }}>
      <Form
        name="auth"
        onFinish={handleSubmit}
        layout="vertical"
      >
        {isSignUp && (
          <>
            <Form.Item
              name="name"
              label="Name"
              rules={[
                { required: true, message: 'Please input your name!' },
                { min: 3, message: 'Name must be at least 3 characters long!' },
                { max: 20, message: 'Name must be at most 20 characters long!' },
              ]}
            >
              <Input />
            </Form.Item>
          </>
        )}
        <Form.Item
          name="email"
          label="Email"
          rules={[{ required: true, type: 'email', message: 'Please input a valid email!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="password"
          label="Password"
          rules={[
            { required: true, message: 'Please input your password!' },
            { min: 6, message: 'Password must be at least 6 characters long!' },
            { max: 40, message: 'Password must be at most 40 characters long!' },
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" style={{ backgroundColor:'#2B2F31', width: '100%'}}>
            {isSignUp ? 'Sign Up' : 'Sign In'}
          </Button>
        </Form.Item>
        {!isSignUp && (
          <Form.Item>
            <FacebookLogin />
          </Form.Item>
        )}
        <Form.Item>
          <Button type="link" onClick={toggleForm}>
            {isSignUp ? 'Already have an account? Sign In' : 'No account? Sign Up'}
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default AuthForm;
