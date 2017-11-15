package videoplayer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerMain{
	public static MainWindow frame;
	public static File file;

	public PlayerMain(){
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"dll/");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
					frame=new MainWindow();
					frame.setTitle("Mini VideoPlayer");
					frame.setVisible(true);
					new SwingWorker<String,Integer>(){
						protected String doInBackground() throws Exception{
							while(true){
								// 变量total，得到打开的视频文件的总时长
								long total=frame.getMediaPlayer().getLength();
								// 变量current，得到播放时的时间点
								long current=frame.getMediaPlayer().getTime();
								float percent=(float)current/total;
								publish((int)(percent*100));
								Thread.sleep(100);
							}
						}

						protected void process(List<Integer> chunks){
							for(int v:chunks){
								frame.getProgressBar().setValue(v);
							}
						};
					}.execute();
					frame.getMediaPlayer().toggleFullScreen();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public static void playVideo(){
		frame.getMediaPlayer().play();
	}

	public static void pauseVideo(){
		frame.getMediaPlayer().pause();
	}

	public static void stopVideo(){
		frame.getMediaPlayer().stop();
	}

	public static void jumpTo(float to){
		frame.getMediaPlayer().setTime((long)(to*frame.getMediaPlayer().getLength()));
	}

	public static void openVideo(){
		JFileChooser chooser=new JFileChooser("N:/");
		final List<String> list=new ArrayList<String>();
		list.add("mkv");
		list.add("mp4");
		list.add("avi");
		list.add("rmvb");
		list.add("mpg");
		chooser.setFileFilter(new FileFilter(){
			public String getDescription(){
				return "Video File(*.mkv;*.mp4;*.avi;*.rmvb;*.mpg...)";
			}

			public boolean accept(File f){
				if(f.isDirectory()){ return true; }
				String name=f.getName();
				int p=name.lastIndexOf('.');
				if(p==-1){ return false; }
				String suffix=name.substring(p+1).toLowerCase();
				return list.contains(suffix);
			}
		});
		int v=chooser.showOpenDialog(frame);
		if(v==JFileChooser.APPROVE_OPTION){
			file=chooser.getSelectedFile();
			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
		}
	}

	public static void setVolume(int v){
		frame.getMediaPlayer().setVolume(v);
	}

	public static void main(String[] args){
		new PlayerMain();
	}
}
