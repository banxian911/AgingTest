precision mediump float;

uniform vec3 aLightPosition;//��λ��
uniform vec3 aEyePosition;//�۾�λ��
uniform mat4 uMMatrix;//ģ�;���

uniform sampler2D uTexture0;//0������
uniform sampler2D uTexture1;//0������

varying vec3 vNormal;//������
varying vec3 vPosition;//��������
varying vec2 vTexCoord;	//��������

void main(){
	

	vec4 colorsDay = texture2D(uTexture0, vTexCoord);
	vec4 colorsNight = texture2D(uTexture1, vTexCoord);
	
	float roughness = 40.0;
	vec4 environmentIntens = vec4(0.15,0.15,0.15,1);
	vec4 spIntens = vec4(0.7,0.7,0.7,1);
	vec4 scIntens = vec4(0.8,0.8,0.8,1);
	
	vec3 normal = normalize(uMMatrix * vec4(vNormal + vPosition,1)).xyz - normalize(uMMatrix * vec4(vPosition, 1)).xyz;
	normal = normalize(normal);
	
	vec3 vLight = normalize(aLightPosition - vPosition);
	vec3 vEye = normalize(aEyePosition - vPosition);
	vec3 vHalf = normalize(vLight + vEye);
	
	spIntens = spIntens * max(dot(normal,vLight), 0.0);
	scIntens = scIntens * max(pow(dot(normal,vHalf), roughness), 0.0);
	
	colorsDay = colorsDay*environmentIntens + colorsDay*spIntens + colorsDay*scIntens;
	colorsNight = colorsNight*environmentIntens + colorsNight*spIntens + colorsNight*scIntens;
	
	if(spIntens.x > 0.21){
		gl_FragColor = colorsDay;
	}else if(spIntens.x < 0.05){
		gl_FragColor = colorsNight;
	}else{
		float tt = (spIntens.x - 0.05)/0.16;
		gl_FragColor = tt * colorsDay + (1.0 - tt) * colorsNight;
	}
}