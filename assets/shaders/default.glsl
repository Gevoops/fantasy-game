#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 iso;
uniform mat4 scale;



out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;


void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position =    iso * uProjection * uView * vec4(aPos, 1.0);
    // scale *
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];


out vec4 color;

void main()
{
    int id = int(fTexId);
    if(fTexId > 0) {
        color = fColor * texture(uTextures[id - 1],fTexCoords);
    } else {
        color = fColor;
    }
    //(1,1,1,1) * (some color: x,w,y,z) = somecolor

}



