import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class OpenFile {

	private String fileName;

	public OpenFile(String fileName) {
		this.fileName = fileName;
	}

	public LinkedList<Task> read() {

		// store line that is read
		String line = null;
		LinkedList<Task> list = null;
		try {
			// FileReader open to start reading
			FileReader fileReader = new FileReader(fileName);

			// FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			list = new LinkedList<Task>();
			LinkedList<Task> newOnes = new LinkedList<Task>();
			// keep reading the file and parse the data
			while ((line = bufferedReader.readLine()) != null) {
				try {
					// split into task and completion time seperated by "|"
					String[] parsedLine = line.split("\\|");

					if (line.length() == parsedLine[0].length() + 1) {
						newOnes.add(new Task(parsedLine[0]));
					} else {

						if (parsedLine.length == 2) {
							Task t = new Task(parsedLine[0]);
							t.setElapsed(parsedLine[1]);
							list.add(t);
						}
					}
				} catch (Exception ex) {
					System.out.println("Corrupted Data");
				}

			}
			while (newOnes.size() != 0) {
				list.addFirst(newOnes.removeLast());
			}

			// close file.
			bufferedReader.close();
		} catch (

		FileNotFoundException ex) {
			System.out.println("Failed to open:'" + fileName + "'");
		} catch (IOException ex1) {
			System.out.println("Error Reading: '" + fileName + "'");
		}

		return list;
	}

	public void save(String fn, LinkedList<Task> tasks) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fn));
		for(int i = 0; i < tasks.size(); i++){
			Task t = tasks.get(i);
			writer.write(t.dataToString()+ '\n');
		}
		writer.close();
	}

}
