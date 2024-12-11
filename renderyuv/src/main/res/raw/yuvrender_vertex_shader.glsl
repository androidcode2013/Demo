#version 300 es

layout(location = 0) in vec4 a_Position;
layout(location = 1) in vec2 a_TextureCoordinates;

out vec2 texture_coord;

void main() {
    gl_Position = a_Position;
    texture_coord = a_TextureCoordinates;
}
