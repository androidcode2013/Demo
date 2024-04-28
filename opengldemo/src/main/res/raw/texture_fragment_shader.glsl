precision mediump float; 
      	 				
uniform sampler2D u_TextureUnit0;
uniform sampler2D u_TextureUnit1;
varying vec2 v_TextureCoordinates;      	   								
  
void main()                    		
{                              	
    gl_FragColor = texture2D(u_TextureUnit0, v_TextureCoordinates)
    + texture2D(u_TextureUnit1, v_TextureCoordinates);
}