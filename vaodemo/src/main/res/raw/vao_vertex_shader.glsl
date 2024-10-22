#version 300 es
layout (location = 0) in vec4 a_Position;
out vec4 vColor;
void main() {
     gl_Position  = a_Position;
     gl_PointSize = 10.0;
     vColor = vec4(0.6f,0.4f,0.5f,1.0f);
}