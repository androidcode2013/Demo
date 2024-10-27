precision mediump float;// 数据精度
varying vec2 aCoord;

uniform sampler2D  u_TextureUnit;
void main(){
    vec4 rgba = texture2D(u_TextureUnit, aCoord);//rgba
    float c = (rgba.r * 0.3 + rgba.g * 0.59 + rgba.b * 0.11) / 3.0;
    gl_FragColor = vec4(c, c, c, 1.0);
}