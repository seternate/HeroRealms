package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.networking.NetworkHelper;


public class LobbyScreen implements Screen{
    final Main game;
    Stage stage;
    NetworkHelper networkHelper;

    public LobbyScreen(final Main game, NetworkHelper networkHelper) {
        this.game = game;
        stage = new Stage();
        this.networkHelper = networkHelper;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0 ,0 ,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }




    /*final Main game;
    Stage stage;
    Server server;
    ServerData serverData;
    Client client;
    ClientData clientData;

    Skin skin;
    Label player1Label, player2Label, player3Label, player4Label;
    Table layoutTable;


    public LobbyScreen(final Main game){
        this.game = game;
        stage = new Stage();
        //setting up networking
        openClientAndOrServer();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        player1Label = new Label(game.player.getName(), skin, "white-big");
        player2Label = new Label("", skin, "white-big");
        player3Label = new Label("", skin, "white-big");
        player4Label = new Label("", skin, "white-big");
        layoutTable = new Table();

        layoutTable.setDebug(true);
        layoutTable.setFillParent(true);
        layoutTable.add(player1Label);
        layoutTable.row();
        layoutTable.add(player2Label);
        layoutTable.row();
        layoutTable.add(player3Label);
        layoutTable.row();
        layoutTable.add(player4Label);
    }

    //Todo: if there is a server, check first if the server is full
    private boolean isServer() {
        List<InetAddress> addresses = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);
        if(addresses.isEmpty()) return false;
        for(InetAddress address : addresses) {
            if(!address.getHostAddress().equals("127.0.0.1")) return true;
        }
        return false;
    }

    private InetAddress getServer() {
        List<InetAddress> addresses = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);
        if(addresses.isEmpty()) return null;
        for(InetAddress address : addresses) {
            if(!address.getHostAddress().equals("127.0.0.1")) return address;
        }
        return null;
    }

    private void register(Kryo kryo) {
        kryo.register(ClientConnectMessage.class);
        kryo.register(ServerConnectMessage.class);
        kryo.register(Player.class);
        kryo.register(ServerData.class);
        kryo.register(Deck.class);
        kryo.register(Card.class);
        kryo.register(HashMap.class);
        kryo.register(Defense.class);
        kryo.register(CardRole.class);
        kryo.register(Faction.class);
        kryo.register(String[].class);
        kryo.register(ArrayList.class);
    }

    public void openServer() {
        server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        server.start();
        serverData = new ServerData(game.gameDataXML);
        try {
            server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Register classes for networking
        register(server.getKryo());
        addServerListener();
    }

    public void addServerListener() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                if(serverData.getPlayerNumber() >= 4) connection.close();
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ClientConnectMessage) {
                    Player player = ((ClientConnectMessage)object).getData();
                    serverData.addPlayer(connection.getID(), player);
                    server.sendToAllTCP(new ServerConnectMessage(serverData));
                }
            }
        });
    }

    public void openClientAndOrServer() {
        new Thread() {
            @Override
            public void run() {
                //Creating & starting client
                client = new Client(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
                client.start();
                //Register for client the classes for networking
                register(client.getKryo());
                //Add Listener to client
                addClientListener();
                *//*
                Search for open servers over UDP. If no server was found, create a server and get
                its address.
                 *//*
                clientData = new ClientData();
                if(!isServer()) openServer();
                clientData.setServerAddress(getServer());
                //Todo: if connection fails show user something
                try {
                    client.connect(NetworkConstants.C_TIMEOUT, clientData.getServerAddress(), NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void addClientListener() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                game.player.setNetworkID(connection.getID());
                client.sendTCP(new ClientConnectMessage(game.player));
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ServerConnectMessage) {
                    ServerData serverData = ((ServerConnectMessage)object).getData();
                    int i = 0;
                    for(Player player : serverData.getPlayer()) {
                        if(player.getNetworkID() == connection.getID()) continue;
                        if(i == 0) player2Label.setText(player.getName());
                        else if(i == 1) player3Label.setText(player.getName());
                        else if(i == 2) player4Label.setText(player.getName());
                        i++;
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(layoutTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        try {
            server.dispose();
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
