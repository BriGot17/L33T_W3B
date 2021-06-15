package at.brigot.l33t;

import at.brigot.l33t.beans.Node;
import at.brigot.l33t.bl.GameCommandExecutor;
import at.brigot.l33t.io.JSON_Parser;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import javafx.scene.control.Tab;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class GameClient extends ApplicationAdapter {
	private Stage stage;
	private Skin skin;

	private float gameWidth;
	private float gameHeight;

	private Table loginTable, chatRoomTable, fileSystemTable, CATTable;
	public Table currentTable;
	private Console console;

	private Socket socket;
	private String sid;

	private Node filesystem;
	public String currentDir = "";
	public boolean connected = false;
	private Map<String,String> possibleHosts = new HashMap<>();
	private String username;

	private JSON_Parser json_parser;

	private InputStream socketInputStream;
	PrintWriter pw;
	BufferedReader br;

	public GameClient() {
		json_parser = JSON_Parser.getInstance();
		filesystem = json_parser.getTestNode();
		out.println(filesystem);
	}

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		out.println(filesystem);

		console = new GUIConsole();
		console.setSizePercent(100, 33);
		console.setPositionPercent(0, 67);
		console.setDisplayKeyID(Input.Keys.Z);

		console.setCommandExecutor(new GameCommandExecutor(console,this));

		gameWidth = stage.getWidth();
		gameHeight = stage.getHeight();

		loginTable = buildLoginTable();
		chatRoomTable = buildChatRoomTable();
		fileSystemTable = buildFileSystem();
		CATTable = buildCATTable();

		stage.addActor(loginTable);
		stage.addActor(chatRoomTable);
		stage.addActor(fileSystemTable);
		stage.addActor(CATTable);

		currentTable = loginTable;
	}

	// login table actors

	private TextButton join_button;
	private TextButton testArea;
	private TextField name_field;
	private TextField password_field;

	private Table buildLoginTable(){
		final Table table = new Table();
		table.setFillParent(true);

		Window window = new Window("Login", skin);
		window.getTitleLabel().setAlignment(Align.center);

		join_button = new TextButton("Join", skin);
		testArea = new TextButton("Test Area", skin);
		name_field = new TextField("", skin);
		password_field = new TextField("", skin);

		window.add(new Label("Enter Your Name", skin));
		window.row();
		window.add(name_field);
		window.row();
		window.add(new Label("Enter Your Password",skin));
		window.row();
		window.add(password_field);
		window.row();
		window.add(join_button);
		window.add(testArea);
		table.add(window);

		testArea.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				loginTable.setVisible(false);
				CATTable.setVisible(true);
				currentTable = CATTable;
			}
		});

		join_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				username = name_field.getText();

				if(!username.isEmpty()){
					loginTable.setVisible(false);
					chatRoomTable.setVisible(true);
					currentTable = chatRoomTable;
					try {
						socket = new Socket("localhost",9999);
						socketInputStream = socket.getInputStream();
						br = new BufferedReader( new InputStreamReader(socketInputStream));
						pw = new PrintWriter(socket.getOutputStream(),true);
						pw.println(username);  // send name to server

						new MessagesThread().start();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return table;
	}

	//Command Line table actors
	private TextButton back_button;

	private ScrollPane editor_scroll;
	private Label editor_label;
	public TextArea editor_area;
	public String currentFile="";

	private Table buildCATTable(){
		Table table = new Table();
		table.setFillParent(true);

		editor_label = new Label("", skin);
		editor_label.setWrap(true);
		editor_label.setAlignment(Align.topLeft);

		editor_scroll = new ScrollPane(editor_label, skin);
		editor_scroll.setFadeScrollBars(false);

		editor_area = new TextArea("",skin);

		editor_area.setText(filesystem.getFilesystem().getLib().get("secret doc").toString());
		editor_area.setDisabled(true);
		//table.add(command_scroll).width(Gdx.graphics.getWidth()-100f).height(400f).colspan(1).center();
		//table.row();

		back_button = new TextButton("Back", skin);
		back_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CATTable.setVisible(false);
			}
		});

		//table.add(back_button).colspan(2);

		table.row().colspan(2).expand();
		table.add(editor_area).fill();
		table.row().colspan(2).expandX().fillX();
		table.add(back_button);

		//table.add(commandInput_scroll).width(300f).colspan(2);

		table.setVisible(false);

		return table;
	}

	// File System table actors
	public List filelist;
	private List.ListStyle fileStyle;
	private ScrollPane scrollPane;
	private String[] files = {"placeholder"};

	private Table buildFileSystem(){
		Table table = new Table();
		table.setFillParent(true);

		filelist = new List(skin);
		fileStyle = new List.ListStyle();

		filelist.setItems(files);

		scrollPane = new ScrollPane(filelist);
		scrollPane.setFillParent(true);
		scrollPane.setSmoothScrolling(false);
		scrollPane.setTransform(true);
		scrollPane.setScale(1f);

		table.addActor(scrollPane);

		table.setVisible(false);

		return table;
	}

	// chat room table actors
	private TextButton send_button;
	private TextArea message_field;
	private ScrollPane input_scroll;

	private List<String> users_list;
	private ScrollPane chat_scroll;
	private ScrollPane users_scroll;
	private Label chat_label;

	private Table buildChatRoomTable(){
		Table table = new Table();
		table.setFillParent(true);

		chat_label = new Label("", skin);
		chat_label.setWrap(true);
		chat_label.setAlignment(Align.topLeft);

		chat_scroll = new ScrollPane(chat_label, skin);
		chat_scroll.setFadeScrollBars(false);

		table.add(chat_scroll).width(300f).height(400f).colspan(2);

		users_list = new List<String>(skin, "dimmed");

		users_scroll = new ScrollPane(users_list, skin);
		users_scroll.setFadeScrollBars(false);

		table.add(users_scroll).width(150f).height(400f).colspan(2);

		message_field = new TextArea("", skin);
		message_field.setPrefRows(2);

		input_scroll = new ScrollPane(message_field, skin);
		input_scroll.setFadeScrollBars(false);

		table.row();
		table.add(input_scroll).width(300f).colspan(2);

		send_button = new TextButton("Send", skin);
		table.add(send_button).colspan(2);

		table.setVisible(false);

		send_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				String text = message_field.getText();

				if(!text.isEmpty()){
					//Text senden
					try {
						String jsonMessage = json_parser.parseMessageToJSON(username, message_field.getText());
						pw.println(jsonMessage);
						pw.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.println("Error reading or parsing message");
					}
					message_field.setText("");
				}
			}
		});

		return table;
	}

	class  MessagesThread extends Thread {
		public void run() {
			try {
				while(true) {
					String jsonRaw = br.readLine();
					String message = json_parser.parseMessageToString(jsonRaw);
					if(message.startsWith("mes_")){
						chat_label.setText(chat_label.getText() + message.substring(4).replace("\"", "") + "\n");
					}
					else{
						users_list.setItems(message.split(";"));
					}
				} // end of while
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Node getFilesystem() {
		return filesystem;
	}
	public Map<String, String> getPossibleHosts() {
		return possibleHosts;
	}
	public String getUsername() {
		return username;
	}
	public JSON_Parser getJson_parser() {
		return json_parser;
	}
	public Stage getStage() {
		return stage;
	}
	public Table getLoginTable() {
		return loginTable;
	}
	public Table getChatRoomTable() {
		return chatRoomTable;
	}
	public Table getFileSystemTable() {
		return fileSystemTable;
	}
	public Table getCATTable() {
		return CATTable;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		console.draw();
	}

	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
		console.dispose();
	}
}
