#version 300 es

precision mediump float;

in vec2 texture_coord;
layout(location = 0) uniform sampler2D sampler_y;
layout(location = 1) uniform sampler2D sampler_u;
layout(location = 2) uniform sampler2D sampler_v;

out vec4 out_color;

void main() {
    float y = texture(sampler_y, texture_coord).r;
    float u = texture(sampler_u, texture_coord).g- 0.5;
    float v = texture(sampler_v, texture_coord).b- 0.5;

    vec3 rgb;
    rgb.r = y + 1.540*v;
    rgb.g = y - 0.183*u - 0.459*v;
    rgb.b = y + 1.818*u;
    out_color = vec4(rgb, 1);
}
