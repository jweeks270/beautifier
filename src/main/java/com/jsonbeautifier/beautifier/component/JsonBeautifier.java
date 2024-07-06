package com.jsonbeautifier.beautifier.component;

import com.jsonbeautifier.beautifier.constants.Constants;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonBeautifier extends JFrame {
    private JButton beautifyBtn;
    private JButton copyOutput;
    private JCheckBox omitTrailingText;
    private JScrollPane inputScrollPane;
    private JScrollPane outputScrollPanel;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;


    public JsonBeautifier() {
        initComponents();
    }

    private void initComponents() {
        setUpComponents();
        addBeautifyActionListener();
        setUpCopyActionListener();
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSON Beautifier");

        setUpInputTxtArea();
        setUpOutputTxtArea();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // Create a parallel group for the horizontal axis
        ParallelGroup hGroup = layout.createParallelGroup(Alignment.LEADING);

        // Create a sequential and a parallel groups
        SequentialGroup parentSequentialGroup = layout.createSequentialGroup();
        ParallelGroup inputParallelGroup = layout.createParallelGroup(Alignment.TRAILING);

        // Add a container gap to the sequential group parentSequentialGroup
        parentSequentialGroup.addContainerGap();

        // Add a scroll pane and a label to the parallel group inputParallelGroup
        inputParallelGroup.addComponent(inputScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

        // Add a container gap to the sequential group parentSequentialGroup
        parentSequentialGroup.addContainerGap();

        // Add a scroll pane and a label to the parallel group inputParallelGroup
        inputParallelGroup.addComponent(outputScrollPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

        // Add a container gap to the sequential group parentSequentialGroup
        parentSequentialGroup.addContainerGap();

        // Create a sequential group searchSequentialGroup
        SequentialGroup searchSequentialGroup = layout.createSequentialGroup();

        searchSequentialGroup.addComponent(beautifyBtn);
        searchSequentialGroup.addContainerGap();
        searchSequentialGroup.addComponent(copyOutput);
        searchSequentialGroup.addContainerGap();
        searchSequentialGroup.addComponent(omitTrailingText);
        searchSequentialGroup.addContainerGap();

        // Add the group searchSequentialGroup to the group inputParallelGroup
        inputParallelGroup.addGroup(searchSequentialGroup);
        // Add the group inputParallelGroup to the group parentSequentialGroup
        parentSequentialGroup.addGroup(inputParallelGroup);

        parentSequentialGroup.addContainerGap();

        // Add the group parentSequentialGroup to the hGroup
        hGroup.addGroup(Alignment.TRAILING, parentSequentialGroup);
        // Create the horizontal group
        layout.setHorizontalGroup(hGroup);


        // Create a parallel group for the vertical axis
        ParallelGroup vGroup = layout.createParallelGroup(Alignment.LEADING);
        // Create a sequential group v1
        SequentialGroup v1 = layout.createSequentialGroup();
        // Add a container gap to the sequential group v1
        v1.addContainerGap();
        // Create a parallel group v2
        ParallelGroup v2 = layout.createParallelGroup(Alignment.BASELINE);
        v2.addComponent(beautifyBtn);
        v2.addComponent(copyOutput);
        v2.addComponent(omitTrailingText);

        // Add the group v2 tp the group v1
        v1.addGroup(v2);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v1.addComponent(inputScrollPane, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v1.addContainerGap();
        v1.addComponent(outputScrollPanel, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v1.addContainerGap();

        // Add the group v1 to the group vGroup
        vGroup.addGroup(v1);
        // Create the vertical group
        layout.setVerticalGroup(vGroup);
        pack();
    }

    private static MouseListener createMouseCopyListener(JTextComponent tc) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String copyString = getCurrentValue(tc);
                if (copyString.trim().length() > 0) {
                    StringSelection stringSelection = new StringSelection(copyString);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            }
        };
    }

    //  Setup
    private void setUpComponents() {
        initializeComponents();
        setUpOmitBlock();
        setUpBeautifyBtn();
        setUpCopyOutputBtn();
        setUpDimensions();
    }

    private void initializeComponents() {
        beautifyBtn = new JButton("Beautify");
        copyOutput = new JButton("Copy Output");
        omitTrailingText = new JCheckBox();
        inputTextArea = new JTextArea();
        outputTextArea = new JTextArea();
    }

    private void setUpOmitBlock() {
        omitTrailingText.setForeground(Color.WHITE);
        omitTrailingText.setText("Omit Preceding/Trailing Text");
        omitTrailingText.setSelected(true);
    }

    private void setUpBeautifyBtn() {
        beautifyBtn.setBackground(Color.BLACK);
        beautifyBtn.setForeground(Color.BLACK);
        beautifyBtn.setOpaque(true);
        beautifyBtn.setFocusPainted(false);
    }

    private void setUpCopyOutputBtn() {
        copyOutput.setBackground(Color.BLACK);
        copyOutput.setForeground(Color.BLACK);
        copyOutput.setOpaque(true);
        copyOutput.setFocusPainted(false);
    }

    private void setUpDimensions() {
        Dimension btnDims = new Dimension();
        btnDims.setSize(150, 30);
        beautifyBtn.setSize(150, 30);
        copyOutput.setSize(150, 30);
        beautifyBtn.setMinimumSize(btnDims);
        copyOutput.setMinimumSize(btnDims);
        beautifyBtn.setMaximumSize(btnDims);
        copyOutput.setMaximumSize(btnDims);
    }

    private void addBeautifyActionListener() {
        beautifyBtn.addActionListener((ActionEvent e) -> {
            if (inputTextArea.getText() != null
                    && inputTextArea.getText().length() > 0) {
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                String jsonToFormat = inputTextArea.getText();
                if (omitTrailingText.isSelected() && inputTextArea.getText().contains("{") && inputTextArea.getText().contains("}")) {
                    jsonToFormat = jsonToFormat.substring(jsonToFormat.indexOf("{"), jsonToFormat.lastIndexOf("}") + 1);
                }
                String jsonString = jsonToFormat;
                try {
                    JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
                    String prettyJson = gson.toJson(json);
                    outputTextArea.setText(prettyJson);
                } catch (Exception ex) {
                    outputTextArea.setText("Invalid JSON Passed.  Check your input JSON and try again.");
                }
            }
        });
    }

    private void setUpCopyActionListener() {
        copyOutput.addActionListener((ActionEvent e) -> {
            if (outputTextArea.getText() != null
                    && outputTextArea.getText().length() > 0) {
                String copyString = outputTextArea.getText();
                if (copyString != null
                        && copyString.trim().length() > 0) {
                    StringSelection stringSelection = new StringSelection(copyString);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            }
        });
    }

    private void setUpInputTxtArea() {
        inputTextArea.setColumns(20);
        inputTextArea.setLineWrap(true);
        inputTextArea.setRows(5);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setEditable(true);
        inputTextArea.setBackground(Color.BLACK);
        inputTextArea.setForeground(Color.WHITE);
        inputTextArea.setCaretColor(Color.WHITE);
        inputTextArea.setFont(inputTextArea.getFont().deriveFont(24f));
        inputTextArea.setMargin( new Insets(10,10,10,10));

        inputScrollPane = new JScrollPane(inputTextArea);
    }

    private void setUpOutputTxtArea() {
        outputTextArea.setColumns(20);
        outputTextArea.setLineWrap(true);
        outputTextArea.setRows(5);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(true);
        outputTextArea.setBackground(Color.BLACK);
        outputTextArea.setForeground(Color.WHITE);
        outputTextArea.setCaretColor(Color.WHITE);
        outputTextArea.setFont(outputTextArea.getFont().deriveFont(24f));
        outputTextArea.setMargin( new Insets(10,10,10,10));
        outputScrollPanel = new JScrollPane(outputTextArea);

        outputTextArea.addMouseListener(createMouseCopyListener(outputTextArea));
    }

    //  Helpers
    private static boolean containsGarbage(String str) {
        if (str == null) {
            return false;
        }
        return Constants.GARBAGE_STRINGS.contains(str.trim());
    }

    //  The Guts to getting the value on the line clicked
    private static String getCurrentValue(JTextComponent tc) {
        try {
            JTextArea ta = (JTextArea) tc;
            int line = ta.getLineOfOffset(ta.getCaretPosition());
            int start = ta.getLineStartOffset(line);
            int end = ta.getLineEndOffset(line);
            String lineText = ta.getDocument().getText(start, end - start).trim();
            if (lineText.trim().startsWith("\"")) {
                String key = lineText.substring(0, lineText.indexOf("\":") + 2);
                lineText = lineText.replace(key, "").trim();
                if (lineText.startsWith("\"")) {
                    lineText = lineText.replaceFirst("\"", "");
                    lineText = lineText.substring(0, lineText.lastIndexOf("\""));
                } else if (lineText.endsWith(",")) {
                    lineText = lineText.substring(0, lineText.lastIndexOf(","));
                }
            }
            if (containsGarbage(lineText)) {
                return "";
            }
            return lineText;
        } catch (Exception ex) {
            return "";
        }
    }

    public void getJsonKeys(JsonObject obj) {
        obj.keySet().forEach(k -> {
            if (obj.get(k) instanceof JsonObject) {
                getJsonKeys((JsonObject) obj.get(k));
            }
        });
    }
}
