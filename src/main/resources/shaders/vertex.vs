#version 400 core

in vec3 pos;

out vec3 colour;

void main(){
    gl_Position = vec4(pos, 1.0);
    colour = vec3(0.0, 0.0, 1.0);
}