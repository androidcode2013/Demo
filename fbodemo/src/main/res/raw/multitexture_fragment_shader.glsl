precision mediump float;// 数据精度

varying vec2 aCoord;
uniform sampler2D  u_TextureUnit;
uniform sampler2D  u_TextureUnit1;

void main(){
     gl_FragColor = mix(texture2D(u_TextureUnit, aCoord),
     texture2D(u_TextureUnit1, aCoord), 0.5);
}