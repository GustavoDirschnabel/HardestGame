import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
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
	
	public void openFile() {
		try {
			output = new ObjectOutputStream(new FileOutputStream(save));
			input = new ObjectInputStream(new FileInputStream(save));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public void addLevel(Level level) {
		try {
			output.writeObject(level);
		} catch (IOException e) {
		//	JOptionPane.showMessageDialog(null, "IOException ao salvar levels", "Erro", JOptionPane.ERROR_MESSAGE);
			System.err.println(e);
		}
	}
	
	public ArrayList<Level> readLevels(){
		ArrayList<Level> levels = new ArrayList<Level>();
		try {
			while(true) {
				levels.add((Level)input.readObject());
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
