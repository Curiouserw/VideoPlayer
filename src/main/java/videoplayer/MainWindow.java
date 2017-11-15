package videoplayer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Kevin
 *         基于VLC播放器内核的JavaGUI视频播放器
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	private JPanel contentPane;
	private EmbeddedMediaPlayerComponent playerComponent;
	private JPanel pnl_ControlPanel;
	private JButton btn_StartPlay;
	private JButton btn_PausePlay;
	private JButton btn_StopPlay;
	private JPanel pnl_EditPanel;
	private JProgressBar progress;
	private JMenuBar menuBar;
	private JMenu mn_FileMenu;
	private JMenuItem mni_OpenVideo;
	private JMenuItem mni_ExitPlayer;
	private JSlider slider;
	private JMenu anoutMenu;
	private JMenuItem mni_AboutAuthorMenuItem;
	private JMenuItem mni_AboutPlayerMeniItem;
	private JPanel pnl_DisplayInfo;
	public JLabel lbl_CurrentTime;
	public JLabel lbl_HoleTime;
	private JPopupMenu popupMenu_DisplayScreen;
	private JMenuItem menuItem_Pause_PopMenu;
	private JPanel pnl_VideoPanel;

	public MainWindow(){
		initialize();
	}

	private void initialize(){
		setTitle("MiniVideoPlayer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,640,480);
		// --------------------------------------------------------------
		menuBar=new JMenuBar();
		setJMenuBar(menuBar);
		// --------------------------------------------------------------
		mn_FileMenu=new JMenu("File");
		menuBar.add(mn_FileMenu);
		// --------------------------------------------------------------
		mni_OpenVideo=new JMenuItem("Open");
		mni_OpenVideo.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.openVideo();
			}
		});
		mn_FileMenu.add(mni_OpenVideo);
		// --------------------------------------------------------------
		mni_ExitPlayer=new JMenuItem("Exit");
		mni_ExitPlayer.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.frame.getMediaPlayer().release();
				System.exit(0);
			}
		});
		mn_FileMenu.add(mni_ExitPlayer);
		// --------------------------------------------------------------
		anoutMenu=new JMenu("About");
		menuBar.add(anoutMenu);
		// --------------------------------------------------------------
		mni_AboutAuthorMenuItem=new JMenuItem("About Author");
		anoutMenu.add(mni_AboutAuthorMenuItem);
		// --------------------------------------------------------------
		mni_AboutPlayerMeniItem=new JMenuItem("About Player");
		mni_AboutPlayerMeniItem.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(PlayerMain.frame,"This VideoPlayer can play all kind of video","Anout Player",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		anoutMenu.add(mni_AboutPlayerMeniItem);
		contentPane=new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		contentPane.setLayout(new BorderLayout(0,0));
		setContentPane(contentPane);
		// --------------------------------------------------------------
		pnl_VideoPanel=new JPanel();
		contentPane.add(pnl_VideoPanel,BorderLayout.CENTER);
		pnl_VideoPanel.setLayout(new BorderLayout(0,0));
		playerComponent=new EmbeddedMediaPlayerComponent();
		playerComponent.setBackground(Color.WHITE);
		pnl_VideoPanel.setOpaque(false);
		pnl_VideoPanel.add(playerComponent);
		// ----------------------------------------------------------------------------
		popupMenu_DisplayScreen=new JPopupMenu();
		addPopup(playerComponent,popupMenu_DisplayScreen);
		// ----------------------------------------------------------------------------
		menuItem_Pause_PopMenu=new JMenuItem("暂停");
		menuItem_Pause_PopMenu.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.pauseVideo();
			}
		});
		menuItem_Pause_PopMenu.setFont(new Font("微软雅黑",Font.PLAIN,14));
		popupMenu_DisplayScreen.add(menuItem_Pause_PopMenu);
		// --------------------------------------------------------------
		pnl_ControlPanel=new JPanel();
		pnl_VideoPanel.add(pnl_ControlPanel,BorderLayout.SOUTH);
		pnl_ControlPanel.setLayout(new BorderLayout(0,0));
		// --------------------------------------------------------------
		pnl_EditPanel=new JPanel();
		pnl_ControlPanel.add(pnl_EditPanel,BorderLayout.CENTER);
		// --------------------------------------------------------------
		btn_StopPlay=new JButton("Stop");
		btn_StopPlay.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.stopVideo();
			}
		});
		pnl_EditPanel.add(btn_StopPlay);
		// --------------------------------------------------------------
		btn_StartPlay=new JButton("Play");
		btn_StartPlay.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.playVideo();
			}
		});
		pnl_EditPanel.add(btn_StartPlay);
		// --------------------------------------------------------------
		btn_PausePlay=new JButton("Pause");
		btn_PausePlay.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				PlayerMain.pauseVideo();
			}
		});
		pnl_EditPanel.add(btn_PausePlay);
		// --------------------------------------------------------------
		slider=new JSlider();
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setMaximum(120);
		slider.addChangeListener(new ChangeListener(){
			
			public void stateChanged(ChangeEvent e){
				PlayerMain.setVolume(slider.getValue());
			}
		});
		pnl_EditPanel.add(slider);
		// --------------------------------------------------------------
		pnl_DisplayInfo=new JPanel();
		pnl_ControlPanel.add(pnl_DisplayInfo,BorderLayout.NORTH);
		pnl_DisplayInfo.setLayout(new BorderLayout(0,0));
		// --------------------------------------------------------------
		lbl_CurrentTime=new JLabel("00:00:00");
		pnl_DisplayInfo.add(lbl_CurrentTime,BorderLayout.WEST);
		// --------------------------------------------------------------
		progress=new JProgressBar();
		pnl_DisplayInfo.add(progress);
		progress.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		progress.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		progress.setBorder(null);
		progress.setBorderPainted(false);
		// --------------------------------------------------------------
		lbl_HoleTime=new JLabel("00:00:00");
		pnl_DisplayInfo.add(lbl_HoleTime,BorderLayout.EAST);
		progress.addMouseListener(new MouseAdapter(){
			
			public void mouseClicked(MouseEvent e){
				int x=e.getX();
				PlayerMain.jumpTo((float)x/progress.getWidth());
			}
		});
	}

	public EmbeddedMediaPlayer getMediaPlayer(){
		return playerComponent.getMediaPlayer();
	}

	public JProgressBar getProgressBar(){
		return progress;
	}

	private static void addPopup(Component component,final JPopupMenu popup){
		component.addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent e){
				if(e.isPopupTrigger()){
					showMenu(e);
				}
			}

			
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger()){
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e){
				popup.show(e.getComponent(),e.getX(),e.getY());
			}
		});
	}
}
