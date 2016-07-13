/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author SKings
 */
public class MessagePanel extends javax.swing.JPanel {

    /**
     * Creates new form MessagePanel
     */
    MainFrame mainFrame;
    Box messageBox, listBox;
    ArrayList<Message> messages;
    private CurrentChat currentChat;

    public MessagePanel(MainFrame mainFrame) {
        currentChat = null;
        initComponents();
        this.mainFrame = mainFrame;
        MessagePanel own = this;
        customInit();

//        currentChat = new CurrentChat("akbar", "asghar", "private", 20);
        Thread refresh = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("messagePanel showing " + own.isShowing());
                while (own.isShowing()) {
                    updatePanel();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessagePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                System.out.println("messagePanel stop showing ");
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    refresh.start();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessagePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    /**
     * programmer function
     */
    public void customInit() {
        messages = new ArrayList<>();
        messageScrollPane.getVerticalScrollBar().setAutoscrolls(true);
        message_main_panel.setLayout(new BorderLayout());
        messageBox = Box.createVerticalBox();
        message_main_panel.add(messageBox, BorderLayout.NORTH);
        message_main_panel.revalidate();
        message_main_panel.repaint();

        listScrollpane.getVerticalScrollBar().setAutoscrolls(true);
        friend_group_panel.setLayout(new BorderLayout());
        listBox = Box.createVerticalBox();
        friend_group_panel.add(listBox, BorderLayout.NORTH);
        friend_group_panel.revalidate();
        friend_group_panel.repaint();
        /*
         for (int i = 0; i < 5; i++) {
         listBox.add(new JButton("asghaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar"));
         }
         */
    }

    public void addNewMessage(String usename, String text) {
        messages.add(new Message(usename, text, new Date()));
        mainFrame.addToResultBox(message_main_panel, messageBox, new MessageCloud(usename, text));
        Thread tmp = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessagePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
            }
        });
        tmp.start();
    }

    public void updatePanel() {
        System.out.println("Updating panel ...");
        if (currentChat != null) {
            messagePanel.setVisible(true);
            if (currentChat.chatType.equalsIgnoreCase("friend")) {
                changeByType(true, false, true, true, true, true);
            } else if (currentChat.chatType.equalsIgnoreCase("group")) {
                changeByType(true, false, false, false, true, false);
            } else if (currentChat.chatType.equalsIgnoreCase("channel")) {
                if (currentChat.chatAdmin.equals(mainFrame.user.getUsername())) {
                    changeByType(true, false, false, false, true, false);
                } else {
                    changeByType(false, false, false, false, true, false);
                }
            } else if (currentChat.chatType.equalsIgnoreCase("private")) {
                changeByType(true, true, true, false, true, true);
            } else {//unknown
                changeByType(true, false, true, true, true, true);
            }
            message_username_lbl.setText(currentChat.chatName);
            if (currentChat.chatType.equals("private")) {
                message_username_lbl.setText("Private: " + currentChat.chatName);
            }
            if (currentChat.chatType.equals("unknown")) {
                message_username_lbl.setText("Unknown: " + currentChat.chatName);
            }

        } else {
            messagePanel.setVisible(false);
        }
        updateSideList();
        if (currentChat != null) {
            updateMessages();
        }
        this.revalidate();
        this.repaint();
        System.out.println("Updating Done.");
    }

    // get_tree ke ba estefade az on friend va gheyre moshakhas mishavad
    public void getTree() {
        JSONObject obj = new JSONObject();
        obj.put("command", "get_tree").put("options", new JSONObject().put("user_name", mainFrame.user.getUsername()));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(result);
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("err")) {
            JOptionPane.showMessageDialog(mainFrame, "user not found");
        } else {
            res = new JSONObject(res.get("payload").toString());
            System.out.println(res);
            // EMPTY all arraylist
            mainFrame.user.getFriends().clear();
            mainFrame.user.getUnknowns().clear();
            mainFrame.user.getPchats().clear();
            mainFrame.user.getGroups().clear();
            mainFrame.user.getChannels().clear();
            // add friend to user
            JSONArray jarr = res.getJSONArray("directs");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                mainFrame.user.getFriends().add(new Friend(tmp.getString("username"), tmp.getString("chat_id")));
            }

            /*
             test:
             // khat e badi faqat baraye test e
             mainFrame.user.getUnknowns().add(new Unknown("akbar", "1"));
             */
            jarr = res.getJSONArray("unknowns");
            for (int i = 0; i < jarr.length(); i++) {
                System.out.println(jarr.toString());
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                mainFrame.user.getUnknowns().add(new Unknown(tmp.getString("username"), tmp.getString("chat_id")));
            }

            jarr = res.getJSONArray("privates");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                mainFrame.user.getPchats().add(new Private(tmp.getString("username"), tmp.getString("chat_id")));
            }

            jarr = res.getJSONArray("groups");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                System.out.println(tmp);
                JSONArray participants = tmp.getJSONArray("participants");
                ArrayList<String> parr = new ArrayList<>();
                for (int j = 0; j < participants.length(); j++) {
                    if (!participants.get(i).toString().equals("null")) {
                        parr.add(participants.getString(i));
                    }
                }
                System.out.println(tmp.getBoolean("mentioned"));
                mainFrame.user.getGroups().add(new Groups(tmp.getString("name"), tmp.getString("chat_id"), parr, tmp.getBoolean("mentioned")));
            }

            jarr = res.getJSONArray("channels");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                mainFrame.user.getChannels().add(new Channel(tmp.getString("name"), tmp.getString("chat_id"), tmp.getString("admin")));
            }
        }
    }

    public void updateSideList() {
        getTree();
        listBox.removeAll();
        JLabel jl = new JLabel("Friends");
        jl.setFont(new Font("Calibri", 0, 18));
        jl.setBackground(Color.white);
        mainFrame.addToResultBox(sidePanel, listBox, jl);

        for (int i = 0; i < mainFrame.user.getFriends().size(); i++) {
            final Friend f = mainFrame.user.getFriends().get(i);
            jl = new JLabel(f.fusername);
            jl.setFont(new Font("Calibri", 0, 14));
            jl.setBackground(Color.white);
            jl.setName("f" + i);
            jl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentChat = new CurrentChat(f.fchatid, f.fusername, "friend", 20);
                }
            });
            listBox.add(jl);
        }

        jl = new JLabel("Groups");
        jl.setFont(new Font("Calibri", 0, 18));
        jl.setBackground(Color.white);
        mainFrame.addToResultBox(sidePanel, listBox, jl);

        for (int i = 0; i < mainFrame.user.getGroups().size(); i++) {
            final Groups g = mainFrame.user.getGroups().get(i);
            jl = new JLabel(g.name);
            jl.setFont(new Font("Calibri", 0, 14));
            if (g.mentioned) {
                
                System.out.println("VAREDD IN KOOFTI SHOD");
                jl.setForeground(Color.red);
                jl.revalidate();
                jl.repaint();
            } else {
                jl.setBackground(Color.white);
                jl.revalidate();
                jl.repaint();
            }
            jl.setName("g" + i);
            jl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentChat = new CurrentChat(g.chatid, g.name, "group", 100);
                }
            });
            listBox.add(jl);
        }

        jl = new JLabel("Channels");
        jl.setFont(new Font("Calibri", 0, 18));
        jl.setBackground(Color.white);
        mainFrame.addToResultBox(sidePanel, listBox, jl);

        for (int i = 0; i < mainFrame.user.getChannels().size(); i++) {
            final Channel c = mainFrame.user.getChannels().get(i);
            jl = new JLabel(c.name);
            jl.setFont(new Font("Calibri", 0, 14));
            jl.setBackground(Color.white);
            jl.setName("c" + i);
            jl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentChat = new CurrentChat(c.chatid, c.name, "channel", 100, 0, c.admin);
                }
            });
            listBox.add(jl);
        }

        jl = new JLabel("Privates");
        jl.setFont(new Font("Calibri", 0, 18));
        jl.setBackground(Color.white);
        mainFrame.addToResultBox(sidePanel, listBox, jl);

        for (int i = 0; i < mainFrame.user.getPchats().size(); i++) {
            final Private p = mainFrame.user.getPchats().get(i);
            jl = new JLabel(p.fusername);
            jl.setFont(new Font("Calibri", 0, 14));
            jl.setBackground(Color.white);
            jl.setName("p" + i);
            jl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentChat = new CurrentChat(p.chatid, p.fusername, "private", 20);
                }
            });
            listBox.add(jl);
        }

        jl = new JLabel("Unknowns");
        jl.setFont(new Font("Calibri", 0, 18));
        jl.setBackground(Color.white);
        mainFrame.addToResultBox(sidePanel, listBox, jl);

        for (int i = 0; i < mainFrame.user.getUnknowns().size(); i++) {
            final Unknown u = mainFrame.user.getUnknowns().get(i);
            jl = new JLabel(u.fusername);
            jl.setFont(new Font("Calibri", 0, 14));
            jl.setBackground(Color.white);
            jl.setName("u" + i);
            jl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentChat = new CurrentChat(u.fchatid, u.fusername, "unknown", 20);
                }
            });
            listBox.add(jl);
        }
    }

    //// get message eshtebahe in ro dorost kon
    public void updateMessages() {
        JSONObject obj = new JSONObject();
        obj.put("command", "get_messages").put("options", new JSONObject().put("username", mainFrame.user.getUsername())
                .put("chat_id", currentChat.chatid)
                .put("count", currentChat.numberMsg));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        System.out.println(obj.toString());
        System.out.println(result);
        JSONObject res = new JSONObject(result);
        if (res.get("status").toString().equalsIgnoreCase("err")) {
            System.out.println("error in message reading !!!!!!!!");
        } else {
            res = new JSONObject(res.get("payload").toString());
            System.out.println(res);
            JSONArray jarr = res.getJSONArray("messages");
            messages.clear();
            messageBox.removeAll();
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject tmp = new JSONObject(jarr.get(i).toString());
                // shayad lazem bashe aval messages .clear() she
                addNewMessage(tmp.getString("sender"), tmp.getString("body"));
                // wtf??
//                mainFrame.user.getUnknowns().add(new Unknown(tmp.getString("username"), tmp.getString("chat_id")));
            }
        }
    }

    public void changeByType(boolean canSendMessage, boolean canSelfDest, boolean canOtherPro, boolean canPChat, boolean canLeave, boolean canBlock) {
        jScrollPane2.setVisible(canSendMessage);
        message_send_btn.setVisible(canSendMessage);

        s_lbl.setVisible(canSelfDest);
        desttime_text.setVisible(canSelfDest);
        message_stopwatch_btn.setVisible(canSelfDest);

        message_fprofile_btn.setVisible(canOtherPro);
        message_pchat_btn.setVisible(canPChat);
        message_unfriend_btn.setVisible(canLeave);
        message_block_btn.setVisible(canBlock);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messagePanel = new javax.swing.JPanel();
        message_block_btn = new javax.swing.JLabel();
        message_unfriend_btn = new javax.swing.JLabel();
        message_username_lbl = new javax.swing.JLabel();
        messageScrollPane = new javax.swing.JScrollPane();
        message_main_panel = new javax.swing.JPanel();
        message_pchat_btn = new javax.swing.JLabel();
        message_fprofile_btn = new javax.swing.JLabel();
        mesage_more_btn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        message_send_btn = new javax.swing.JLabel();
        desttime_text = new javax.swing.JFormattedTextField();
        s_lbl = new javax.swing.JLabel();
        message_stopwatch_btn = new javax.swing.JLabel();
        sidePanel = new javax.swing.JPanel();
        listScrollpane = new javax.swing.JScrollPane();
        friend_group_panel = new javax.swing.JPanel();
        message_profile_lbl = new javax.swing.JLabel();
        message_search_btn = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        messagePanel.setBackground(new java.awt.Color(255, 255, 255));

        message_block_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_block_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_block_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/stop.png"))); // NOI18N
        message_block_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_block_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_block_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_block_btnMouseExited(evt);
            }
        });

        message_unfriend_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_unfriend_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_unfriend_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/remove_friend.png"))); // NOI18N
        message_unfriend_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_unfriend_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_unfriend_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_unfriend_btnMouseExited(evt);
            }
        });

        message_username_lbl.setBackground(new java.awt.Color(255, 255, 255));
        message_username_lbl.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_username_lbl.setText("Username");

        messageScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        messageScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 137, 226)));
        messageScrollPane.setPreferredSize(new Dimension(485,258));

        message_main_panel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout message_main_panelLayout = new javax.swing.GroupLayout(message_main_panel);
        message_main_panel.setLayout(message_main_panelLayout);
        message_main_panelLayout.setHorizontalGroup(
            message_main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );
        message_main_panelLayout.setVerticalGroup(
            message_main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 259, Short.MAX_VALUE)
        );

        messageScrollPane.setViewportView(message_main_panel);

        message_pchat_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_pchat_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_pchat_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/pchat.png"))); // NOI18N
        message_pchat_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_pchat_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_pchat_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_pchat_btnMouseExited(evt);
            }
        });

        message_fprofile_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_fprofile_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_fprofile_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/fprofile.png"))); // NOI18N
        message_fprofile_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_fprofile_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_fprofile_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_fprofile_btnMouseExited(evt);
            }
        });

        mesage_more_btn.setBackground(new java.awt.Color(47, 169, 226));
        mesage_more_btn.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mesage_more_btn.setForeground(new java.awt.Color(255, 255, 255));
        mesage_more_btn.setText("More Message");
        mesage_more_btn.setBorder(null);
        mesage_more_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mesage_more_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mesage_more_btnMouseExited(evt);
            }
        });
        mesage_more_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mesage_more_btnActionPerformed(evt);
            }
        });

        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        messageTextArea.setForeground(new java.awt.Color(20, 10, 10));
        messageTextArea.setRows(5);
        jScrollPane2.setViewportView(messageTextArea);

        message_send_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_send_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_send_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/send.png"))); // NOI18N
        message_send_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_send_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_send_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_send_btnMouseExited(evt);
            }
        });

        desttime_text.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 167, 226)));
        desttime_text.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        s_lbl.setBackground(new java.awt.Color(255, 255, 255));
        s_lbl.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        s_lbl.setText("s");

        message_stopwatch_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_stopwatch_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_stopwatch_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/stopwatch.png"))); // NOI18N
        message_stopwatch_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_stopwatch_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_stopwatch_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_stopwatch_btnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addComponent(messageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messagePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(message_username_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(message_fprofile_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(message_pchat_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(message_unfriend_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(message_block_btn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messagePanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(messagePanelLayout.createSequentialGroup()
                                .addComponent(message_send_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(message_stopwatch_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(desttime_text, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(s_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mesage_more_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)))
                .addContainerGap())
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(message_unfriend_btn)
                    .addComponent(message_block_btn)
                    .addComponent(message_pchat_btn)
                    .addComponent(message_fprofile_btn)
                    .addComponent(message_username_lbl, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addComponent(mesage_more_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(message_send_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(s_lbl)
                            .addComponent(message_stopwatch_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(desttime_text)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        sidePanel.setBackground(new java.awt.Color(255, 255, 255));

        listScrollpane.setBackground(new java.awt.Color(255, 255, 255));
        listScrollpane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 137, 226)));
        listScrollpane.setPreferredSize(new Dimension(190,300));

        friend_group_panel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout friend_group_panelLayout = new javax.swing.GroupLayout(friend_group_panel);
        friend_group_panel.setLayout(friend_group_panelLayout);
        friend_group_panelLayout.setHorizontalGroup(
            friend_group_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );
        friend_group_panelLayout.setVerticalGroup(
            friend_group_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        listScrollpane.setViewportView(friend_group_panel);

        message_profile_lbl.setBackground(new java.awt.Color(255, 255, 255));
        message_profile_lbl.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_profile_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/mprofile.png"))); // NOI18N
        message_profile_lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_profile_lblMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_profile_lblMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_profile_lblMouseExited(evt);
            }
        });

        message_search_btn.setBackground(new java.awt.Color(255, 255, 255));
        message_search_btn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        message_search_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/selegram/search2.png"))); // NOI18N
        message_search_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                message_search_btnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                message_search_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                message_search_btnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(message_search_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(message_profile_lbl)
                .addContainerGap())
            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(listScrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(message_search_btn)
                    .addComponent(message_profile_lbl))
                .addGap(24, 24, 24))
            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sidePanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(listScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(messagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void message_unfriend_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_unfriend_btnMouseClicked
        // TODO add your handling code here:
        JSONObject obj = new JSONObject();
        if (currentChat.chatType.equalsIgnoreCase("group") || currentChat.chatType.equalsIgnoreCase("channel")) {
            obj.put("command", "unfriend").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", currentChat.chatName));
            String result = mainFrame.clientHandler.getAnswer(obj.toString());
            JSONObject res = new JSONObject(result);
        } else {
            obj.put("command", "unfriend").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", currentChat.chatName));
            String result = mainFrame.clientHandler.getAnswer(obj.toString());
            JSONObject res = new JSONObject(result);
        }
        currentChat = null;
    }//GEN-LAST:event_message_unfriend_btnMouseClicked

    private void message_unfriend_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_unfriend_btnMouseEntered
        // TODO add your handling code here:
        message_unfriend_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/remove_friendh.png")));
    }//GEN-LAST:event_message_unfriend_btnMouseEntered

    private void message_unfriend_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_unfriend_btnMouseExited
        // TODO add your handling code here:
        message_unfriend_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/remove_friend.png")));
    }//GEN-LAST:event_message_unfriend_btnMouseExited

    private void message_block_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_block_btnMouseClicked
        // TODO add your handling code here:
        JSONObject obj = new JSONObject();
        obj.put("command", "report").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("report_username", currentChat.chatName));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        JSONObject res = new JSONObject(result);
    }//GEN-LAST:event_message_block_btnMouseClicked

    private void message_block_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_block_btnMouseEntered
        // TODO add your handling code here:
        message_block_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/stoph.png")));
    }//GEN-LAST:event_message_block_btnMouseEntered

    private void message_block_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_block_btnMouseExited
        // TODO add your handling code here:
        message_block_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/stop.png")));
    }//GEN-LAST:event_message_block_btnMouseExited

    private void message_search_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_search_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new searchPanel(mainFrame));
    }//GEN-LAST:event_message_search_btnMouseClicked

    private void message_search_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_search_btnMouseEntered
        // TODO add your handling code here:
        message_search_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search2h.png")));
    }//GEN-LAST:event_message_search_btnMouseEntered

    private void message_search_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_search_btnMouseExited
        // TODO add your handling code here:
        message_search_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/search2.png")));
    }//GEN-LAST:event_message_search_btnMouseExited

    private void message_pchat_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_pchat_btnMouseClicked
        // TODO add your handling code here:
//        JSONArray farr = new JSONArray();
//        for(int i=0;i<participant.size();i++){
//            farr.put(participant.get(i).fusername);
//        }
//        String command = "make_"+type;
        JSONObject obj = new JSONObject();
        obj.put("command", "make_private").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("friend_username", currentChat.chatName));
        String result = mainFrame.clientHandler.getAnswer(obj.toString());
        JSONObject res = new JSONObject(result);
    }//GEN-LAST:event_message_pchat_btnMouseClicked

    private void message_pchat_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_pchat_btnMouseEntered
        // TODO add your handling code here:
        message_pchat_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/pchath.png")));
    }//GEN-LAST:event_message_pchat_btnMouseEntered

    private void message_pchat_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_pchat_btnMouseExited
        // TODO add your handling code here:
        message_pchat_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/pchat.png")));
    }//GEN-LAST:event_message_pchat_btnMouseExited

    private void message_fprofile_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_fprofile_btnMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new profilePanel(mainFrame, currentChat.chatName));
    }//GEN-LAST:event_message_fprofile_btnMouseClicked

    private void message_fprofile_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_fprofile_btnMouseEntered
        // TODO add your handling code here:
        message_fprofile_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/fprofileh.png")));
    }//GEN-LAST:event_message_fprofile_btnMouseEntered

    private void message_fprofile_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_fprofile_btnMouseExited
        // TODO add your handling code here:
        message_fprofile_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/fprofile.png")));
    }//GEN-LAST:event_message_fprofile_btnMouseExited

    private void mesage_more_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mesage_more_btnMouseEntered
        // TODO add your handling code here:
        mesage_more_btn.setBackground(new Color(37, 159, 216));
    }//GEN-LAST:event_mesage_more_btnMouseEntered

    private void mesage_more_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mesage_more_btnMouseExited
        // TODO add your handling code here:
        mesage_more_btn.setBackground(new Color(47, 169, 226));
    }//GEN-LAST:event_mesage_more_btnMouseExited

    private void mesage_more_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mesage_more_btnActionPerformed
        // TODO add your handling code here:
        //mainFrame.switchPanels(this, new searchPanel(mainFrame));
        if (currentChat.chatName.equals("channel") || currentChat.chatName.equals("group")) {
            currentChat.numberMsg += 100;
        } else {
            currentChat.numberMsg += 20;
        }
    }//GEN-LAST:event_mesage_more_btnActionPerformed

    private void message_send_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_send_btnMouseClicked
        // TODO add your handling code here:
        if (!messageTextArea.getText().trim().isEmpty()) {
            JSONObject obj = new JSONObject();
            obj.put("command", "send_message").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("recipient", currentChat.chatName).put("chat_id", currentChat.chatid).put("body", messageTextArea.getText()));
            String result = mainFrame.clientHandler.getAnswer(obj.toString());
            JSONObject res = new JSONObject(result);
            if (res.getString("status").equalsIgnoreCase("ok")) {
                addNewMessage(mainFrame.user.getUsername(), messageTextArea.getText().trim());
                messageTextArea.setText("");
            }

        }
    }//GEN-LAST:event_message_send_btnMouseClicked

    private void message_send_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_send_btnMouseEntered
        // TODO add your handling code here:
        message_send_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/sendh.png")));
    }//GEN-LAST:event_message_send_btnMouseEntered

    private void message_send_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_send_btnMouseExited
        // TODO add your handling code here:
        message_send_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/send.png")));
    }//GEN-LAST:event_message_send_btnMouseExited

    private void message_profile_lblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_profile_lblMouseClicked
        // TODO add your handling code here:
        mainFrame.switchPanels(this, new profilePanel(mainFrame, mainFrame.user.getUsername()));
    }//GEN-LAST:event_message_profile_lblMouseClicked

    private void message_profile_lblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_profile_lblMouseEntered
        // TODO add your handling code here:
        message_profile_lbl.setIcon(new ImageIcon(getClass().getResource("/selegram/mprofileh.png")));
    }//GEN-LAST:event_message_profile_lblMouseEntered

    private void message_profile_lblMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_profile_lblMouseExited
        // TODO add your handling code here:
        message_profile_lbl.setIcon(new ImageIcon(getClass().getResource("/selegram/mprofile.png")));
    }//GEN-LAST:event_message_profile_lblMouseExited

    private void message_stopwatch_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_stopwatch_btnMouseClicked
        // TODO add your handling code here:
        if (!desttime_text.getText().equals("")) {
            int destructTime = Integer.parseInt(desttime_text.getText());
            if (destructTime > 0) {
                JSONObject obj = new JSONObject();
                obj.put("command", "set_dest").put("options", new JSONObject().put("chat_id", currentChat.chatid).put("time", destructTime));
                String result = mainFrame.clientHandler.getAnswer(obj.toString());
                JSONObject res = new JSONObject(result);
                if (res.getString("status").equalsIgnoreCase("ok")) {
                    obj = new JSONObject();
                    obj.put("command", "send_message").put("options", new JSONObject().put("username", mainFrame.user.getUsername()).put("recipient", currentChat.chatName).put("chat_id", currentChat.chatid).put("body", "Destruction Time change to "+destructTime+"s"));
                    result = mainFrame.clientHandler.getAnswer(obj.toString());
                    res = new JSONObject(result);
                    if (res.getString("status").equalsIgnoreCase("ok")) {
                        addNewMessage(mainFrame.user.getUsername(), messageTextArea.getText().trim());
                        messageTextArea.setText("");
                    }
                    currentChat.selfdest = destructTime;
                }
            }
        }

    }//GEN-LAST:event_message_stopwatch_btnMouseClicked

    private void message_stopwatch_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_stopwatch_btnMouseEntered
        // TODO add your handling code here:
        message_stopwatch_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/stopwatchh.png")));
    }//GEN-LAST:event_message_stopwatch_btnMouseEntered

    private void message_stopwatch_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_message_stopwatch_btnMouseExited
        // TODO add your handling code here:
        message_stopwatch_btn.setIcon(new ImageIcon(getClass().getResource("/selegram/stopwatch.png")));
    }//GEN-LAST:event_message_stopwatch_btnMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField desttime_text;
    private javax.swing.JPanel friend_group_panel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane listScrollpane;
    private javax.swing.JButton mesage_more_btn;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel message_block_btn;
    private javax.swing.JLabel message_fprofile_btn;
    private javax.swing.JPanel message_main_panel;
    private javax.swing.JLabel message_pchat_btn;
    private javax.swing.JLabel message_profile_lbl;
    private javax.swing.JLabel message_search_btn;
    private javax.swing.JLabel message_send_btn;
    private javax.swing.JLabel message_stopwatch_btn;
    private javax.swing.JLabel message_unfriend_btn;
    private javax.swing.JLabel message_username_lbl;
    private javax.swing.JLabel s_lbl;
    private javax.swing.JPanel sidePanel;
    // End of variables declaration//GEN-END:variables
}
