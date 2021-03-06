import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
	private String task;
	private Long time;
	private String elapsed = null;

	public Task(String Task, String time) throws ParseException {
		this.task = Task;
		this.time = Long.parseLong(time);
	}

	public void setElapsed(String e){
		this.elapsed = e;
	}
	public Task(String Task, Long time) {
		this.time = time;
		this.task = Task;
	}

	public String getElapsedString(){
		return this.elapsed;
	}
	public String dataToString() {
		if (this.elapsed == null) {
			return this.task + "|";
		} else {
			return this.task + "|" + this.elapsed;
		}
	}

	public Task(String Task) {
		this.task = Task;
		this.time = null;
	}

	public String getTask() {
		return task;
	}

	public long getTime() {
		if (time == null) {
			return -1;
		} else {
			return time;
		}
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getElapsed() {
		long elapsedTime;
		double seconds;
		if (this.time == null) {
			elapsedTime = 0;
			seconds = 0;
		} else {
			elapsedTime = System.nanoTime() - this.time;
			seconds = (double) elapsedTime / 1000000000.0;
			this.elapsed = Math.round(seconds) + "";
		}
		return seconds;

	}

}
