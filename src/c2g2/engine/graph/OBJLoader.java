package c2g2.engine.graph;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.io.*; 
import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;


public class OBJLoader {
    public static Mesh loadMesh(String fileName) throws Exception {
    	//// --- student code ---
    	
        List<Float> pos = new ArrayList<Float>();
        List<Float> text = new ArrayList<Float>();
        List<Float> no = new ArrayList<Float>();
        List<Integer> ind = new ArrayList<Integer>();
        
        //your task is to read data from an .obj file and fill in those arrays.
        //the data in those arrays should use following format.
        //positions[0]=v[0].position.x positions[1]=v[0].position.y positions[2]=v[0].position.z positions[3]=v[1].position.x ...
        //textCoords[0]=v[0].texture_coordinates.x textCoords[1]=v[0].texture_coordinates.y textCoords[2]=v[1].texture_coordinates.x ...
        //norms[0]=v[0].normals.x norms[1]=v[0].normals.y norms[2]=v[0].normals.z norms[3]=v[1].normals.x...
        //indices[0]=face[0].ind[0] indices[1]=face[0].ind[1] indices[2]=face[0].ind[2] indices[3]=face[1].ind[0]...(assuming all the faces are triangle face)
        
        //open file
        
        BufferedReader reader;
        try{
        	reader = new BufferedReader(new FileReader(
        	"/Users/Maddy/Documents/Graphics/pa1/src/resources/models/" + fileName));
		
			//int count=0;
			String line = reader.readLine();
			while( line != null ) {
				
				
				String[] parse = line.split(" ");
				//System.out.println(parse[0]);
				//position
				if ( parse[0].equals("v") ) {
					//System.out.println("in v loop");
					//loop through elements of line
					for(int i=0 ; i<3; i++){
						//first position
						pos.add(Float.valueOf(parse[i+1]));
						//positions[count+i] = Float.valueOf(parse[i+1]);
					}
				}
				//norms
				else if (parse[0].equals("vn")){
					for(int i=0 ; i<3; i++){
						no.add(Float.valueOf(parse[i+1]));
						//norms[count+i] = Float.valueOf(parse[i+1]);
					}
				}
				//textCoords
				else if (parse[0].equals("vt")){
					for(int i=0 ; i<2; i++){
						text.add(Float.valueOf(parse[i+1]));
						//norms[count+i] = Float.valueOf(parse[i+1]);
					}
				}
				//indices
				else if(parse[0].equals("f")){
					//get first number
					for(int i=0 ; i<3; i++){
						String[] numString = parse[i+1].split("//");
						int num = Integer.parseInt(numString[0]);
						ind.add(num);
						//indices[count+i] = num;
					}        		
				}
			
				//count = count+3;
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
				
		float[] positions = new float[pos.size()];
		int i = 0;
		for(Float f: pos){
			positions[i] = pos.get(i).floatValue();
			i++;
		}
				
		float[] norms = new float[no.size()];
		int j = 0;
		for(Float f: no){
			norms[j] = no.get(j).floatValue();
			j++;
		}
		
		int[] indices = new int[ind.size()];
		int k = 0;
		for(Integer f: ind){
			indices[k] = ind.get(k);
			k++;
		}
		
		float[] textCoords = new float[ind.size()];
		int l = 0;
		for(Float f: text){
			textCoords[l] = text.get(l).floatValue();
			l++;
		}
		
		//System.out.println(Arrays.toString(positions));
        return new Mesh(positions, textCoords, norms, indices);
    }

}
