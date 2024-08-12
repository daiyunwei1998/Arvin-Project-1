# STYLiSH
[website url](http://13.250.107.185/)

## About me
My name is Arvin, a to-be software engineer.

## About STYLiSH
This is a tool project for Appwork School Back-End class. 

Tech Stack (my anticipation):
- Three-tier architecture
    - FrontEnd: NextJS
    - BackEnd: Java 17 + Spring Boot 
    - Database: MySQL
- Hosting: 
    - FrontEnd: vercel (or EC2 with nodeJS)
    - BackEnd: AWS EC2
    - Database: AWS EC2
    - CloudFront for CDN
- Reverse Proxy: Nginx
- CI/CD: github action

# Back-end server setting
## Binding Spring Boot app to port 80

Port forwarding is achieved using Nginx web server. It is conviniently achieved by setting `proxy_pass` that forward traffic from 80 to 8080 (the default port Spring Boot app uses).

In `/etc/nginx/nginx.conf`, set the following config under 'html' block.
```
server {
    listen 80;
    server_name localhost;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}

```
I also deleted default welcome page for Nginx, as suggested by [this post](https://stackoverflow.com/a/63168431). Without this step, port 80 is 'hijacked' by the welcome message.

Remember to run `sudo nginx -s reload` so that the changes can be applied.
## Continous deployment using Github action
A github action script `.github\workflows\deploy.yml` is responsible for packaging java app and deploy it on my backend aws EC2 through ssh connection.

SSH connection authentication is done by storing repository secret in github.

The github action will be triggered when new commit is pushed to `test-deploy` branch.

## Running server in the background
Linux has a nohup command for running instances in background.

In the following command, my app.jar file is run using jvm. The standard output and standard error is redirected to app.log. `&` runs the command in the background so that we can still use the terminal.
`nohup java -jar ~/app.jar > app.log 2>&1 &`

To stop this app, we can run `ps -aux | grep java` to find its pid, and kill it using `kill -9  <pid>`. 




