precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 TexCoord;
uniform int u_Type;
float near = 0.1;
float far = 100.0;
float LinearizeDepth(float depth);
void main() {
    if (u_Type == 1){
        gl_FragColor = vec4(vec3(gl_FragCoord.z), 1.0);
    } else if (u_Type == 2){
        float depth = LinearizeDepth(gl_FragCoord.z) / far;
        gl_FragColor = vec4(vec3(depth), 1.0);
    } else {
        gl_FragColor = texture2D(u_TextureUnit, TexCoord);
    }
}

float LinearizeDepth(float depth){
    float z = depth * 2.0 - 1.0;// back to NDC
    return (2.0 * near * far) / (far + near - z * (far - near));
}
