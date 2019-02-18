package c2g2.engine.graph;

import java.nio.FloatBuffer;
import java.lang.Object.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.joml.Matrix4f;

import java.lang.*;

import java.io.Serializable.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private int vaoId;

    private List<Integer> vboIdList;

    private int vertexCount;

    private Material material;
    
    private float[] pos;
    private float[] textco;
    private float[] norms;
    private int[] inds;
    
    
    public Mesh(){
       this(new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.5f,0.0f,0.0f,0.5f,0.5f,0.5f,0.0f,0.0f,0.5f,0.0f,0.5f,0.5f,0.5f,0.0f,0.5f,0.5f,0.5f}, 
    		new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    		new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    		new int[]{0,6,4,0,2,6,0,3,2,0,1,3,2,7,6,2,3,7,4,6,7,4,7,5,0,4,5,0,5,1,1,5,7,1,7,3});
    }
    
    public void setMesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	pos = positions;
    	textco = textCoords;
    	norms = normals;
    	inds = indices;
    	FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        System.out.println("create mesh:");
        System.out.println("v: "+positions.length+" t: "+textCoords.length+" n: "+normals.length+" idx: "+indices.length);
        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<Integer>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
        	
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	setMesh(positions, textCoords, normals, indices);        
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
    	// Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    
    public void setIdentity(Matrix4f matrix){
    	matrix.m00(1.0f);
    	matrix.m10(0.0f);
    	matrix.m20(0.0f);
    	matrix.m30(0.0f);
    	matrix.m01(0.0f);
    	matrix.m11(1.0f);
    	matrix.m21(0.0f);
    	matrix.m31(0.0f);
    	matrix.m02(0.0f);
    	matrix.m12(0.0f);
    	matrix.m22(1.0f);
    	matrix.m32(0.0f);
    	matrix.m03(0.0f);
    	matrix.m13(0.0f);
    	matrix.m23(0.0f);
    	matrix.m33(1.0f);
    }
    
    public void scaleMesh(float sx, float sy, float sz){
    	cleanUp(); //clean up buffer
    	//Reset position of each point
    	//Do not change textco, norms, inds
    	//student code 
    	Vector3f sca = new Vector3f(sx,sy,sz);
    	
    	for (int i = 0; i < pos.length/3; i++) {
			Matrix4f oldMatrix = new Matrix4f();
			setIdentity(oldMatrix);
    		oldMatrix.m03(pos[(i*3)+0]);
    		oldMatrix.m13(pos[(i*3)+1]);
    		oldMatrix.m23(pos[(i*3)+2]);
    		oldMatrix.scale(sca);
			pos[(i*3)+0] = oldMatrix.m03();
			pos[(i*3)+1] = oldMatrix.m13();
			pos[(i*3)+2] = oldMatrix.m23();
		}   	
    	setMesh(pos, textco, norms, inds);
    }
    
    public void translateMesh(Vector3f trans){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
		//float[] components = new float[3];
		//Matrix4f matrix = new Matrix4f();//create 4x4 matrix
		//setIdentity(matrix); //set it to be the identity matrix
		
		//trans.get(components);
		
		//matrix.translation(trans.x(),trans.y(),trans.z());//translate(vec3,source,destination)
    	
    	for(int i=0; i< pos.length/3; i++){
    		//make 3d vector of positions
			//Vector3f oldCoords = new Vector3f(pos[(i*3)+0],pos[(i*3)+1],pos[(i*3)+2]);
			Matrix4f temp = new Matrix4f();
			setIdentity(temp);
			temp.m03(pos[(i*3)+0]);
			temp.m13(pos[(i*3)+1]);
			temp.m23(pos[(i*3)+2]);
			
			//create matrix with components to be transformed
			//temp.set(oldCoords);
			Matrix4f newMatrix = new Matrix4f();
			newMatrix = temp.translation(trans.x(),trans.y(),trans.z());
			
			//newMatrix = matrix.mul(temp);
			
			//newMatrix = matrix*temp; //transformed matrix
			pos[(i*3)+0] = newMatrix.m03();
			pos[(i*3)+1] = newMatrix.m13();
			pos[(i*3)+2] = newMatrix.m23();

    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void rotateMesh(Vector3f axis, float angle){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	
    	//Matrix4f matrix = new Matrix4f();
    	//setIdentity(matrix);
    	//matrix.rotate(angle,axis,matrix);
        	
    	for(int i=0; i< pos.length/3; i++){
    		//Vector3f oldCoords = new Vector3f(pos[(i*3)+0],pos[(i*3)+1],pos[(i*3)+2]);
    		Matrix4f oldMatrix = new Matrix4f();
    		setIdentity(oldMatrix);
    		oldMatrix.m03(pos[(i*3)+0]);
    		oldMatrix.m13(pos[(i*3)+1]);
    		oldMatrix.m23(pos[(i*3)+2]);
    		Matrix4f newMatrix = new Matrix4f();
    		float rad = ((angle*3.14f)/(180.0f));
    		oldMatrix.rotate(rad,axis,newMatrix);
    		
    		//newMatrix = matrix.mul(oldMatrix);
    		
    		pos[(i*3)+0] = newMatrix.m03();
			pos[(i*3)+1] = newMatrix.m13();
			pos[(i*3)+2] = newMatrix.m23();
    		
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void reflectMesh(Vector3f p, Vector3f n){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	
    	for(int i=0; i< pos.length/3; i++){
    		Matrix4f oldMatrix = new Matrix4f();
    		setIdentity(oldMatrix);
    		oldMatrix.m03(pos[(i*3)+0]);
    		oldMatrix.m13(pos[(i*3)+1]);
    		oldMatrix.m23(pos[(i*3)+2]);
    		Matrix4f newMatrix = new Matrix4f();
    		newMatrix = oldMatrix.reflect(n.x(),n.y(),n.z(),p.x(),p.y(),p.z());
    		pos[(i*3)+0] = newMatrix.m03();
    		pos[(i*3)+1] = newMatrix.m13();
    		pos[(i*3)+2] = newMatrix.m23();
    	}
    	setMesh(pos, textco, norms, inds);
    }
}
