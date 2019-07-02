import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SerializedSave {
	
	private File save;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ArrayList<Level> savedLevel;

	public SerializedSave() {
		this.save = new File("Levels.ora");
		if(!save.exists()) {
			try {
				save.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "IOException ao inicializar o save", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void openFileInput() {
		try {
			input = new ObjectInputStream(new FileInputStream(save));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void openFileOutput() {
		try {
			input = new ObjectInputStream(new FileInputStream(save));
			savedLevel = new ArrayList<Level>();
			ArrayList<Level> savedLevelAdapter = new ArrayList<Level>();
			while(true) {
				try {
					savedLevelAdapter = (ArrayList<Level>) input.readObject();
					for(int i = 0; i < savedLevelAdapter.size(); i++) {
						savedLevel.add(savedLevelAdapter.get(i));
					}
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Arquivo provavelmente corrompido", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (EOFException e) {
			try {
				output = new ObjectOutputStream(new FileOutputStream(save));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "IOException ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
		} 
		
	}
	
	public void addLevel(Level level) {
		try {
			savedLevel.add(level);
			output.writeObject(savedLevel);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException ao salvar levels", "Erro", JOptionPane.ERROR_MESSAGE);
			System.err.println(e);
		}
	}
	
	public ArrayList<Level> readLevels(){
		ArrayList<Level> levels = new ArrayList<Level>();
		try {
			while(true) {
				ArrayList<Level> levelAdapter = (ArrayList<Level>) input.readObject();
				for(int i = 0; i < levelAdapter.size(); i++) {
					levels.add(levelAdapter.get(i));
				}
			}
		}catch(EOFException e) {
			return levels;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Arquivo provavelmente corrompido", "Erro", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException ao ler levels", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public void closeFile() {
		try {
			if(input != null) {
				input.close();
			}
			if(output != null) {
				output.close();
			}
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "IOException ao fechar o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}
