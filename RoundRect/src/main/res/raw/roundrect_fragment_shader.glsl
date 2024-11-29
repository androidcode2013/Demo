precision mediump float;
uniform vec4 u_Color;
varying vec2 vRectCoord;

void main()
{
    if(abs(vRectCoord.x) > 0.45 && abs(vRectCoord.y) > 0.75
     && sqrt(
     (abs(vRectCoord.x) -0.45)*(abs(vRectCoord.x) -0.45)
     + (abs(vRectCoord.y) -0.75)*(abs(vRectCoord.y) -0.75)
     ) > 0.05){
       gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
    } else {
       gl_FragColor = u_Color;
    }
}