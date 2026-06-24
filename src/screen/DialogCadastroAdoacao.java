package screen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import data.Animal;
import data.Adotante;
import data.Adoacao;

public class DialogCadastroAdoacao extends JDialog {

    private static final Color VERDE       = new Color(46, 125, 50);
    private static final Color VERDE_HOVER = new Color(27, 94, 32);
    private static final Color FUNDO       = new Color(248, 250, 248);
    private static final Color BRANCO      = Color.WHITE;
    private static final Color CINZA_BORDA = new Color(220, 220, 220);
    private static final Color CINZA_TEXTO = new Color(150, 150, 150);
    private static final Color VERMELHO    = new Color(200, 0, 0);

    private JComboBox<String> comboAnimal;
    private JComboBox<String> comboAdotante;
    private JTextField campoData;
    private Runnable refreshCallback;

    public DialogCadastroAdoacao(Window pai, Runnable refreshCallback) {
        super(pai, "Nova Adoção", ModalityType.APPLICATION_MODAL);
        this.refreshCallback = refreshCallback;
        setSize(520, 550);
        setLocationRelativeTo(pai);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarCabecalho(),  BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(),     BorderLayout.SOUTH);
    }

    // ── CABEÇALHO ─────────────────────────────────────────────────────────────
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(VERDE);
        cab.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel("Módulo: Adoção - Nova Adoção");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(BRANCO);

        JLabel sub = new JLabel("Preencha os dados da adoção");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 230, 200));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(sub);

        cab.add(textos, BorderLayout.CENTER);
        return cab;
    }

    // ── FORMULÁRIO ────────────────────────────────────────────────────────────
    private JPanel criarFormulario() {

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(FUNDO);
        form.setBorder(new EmptyBorder(24, 28, 8, 28));

        // Combo Animal
        comboAnimal = new JComboBox<>();
        comboAnimal.setEditable(true);

        for (Animal a : utils.SistemaDados.animais) {
            if (!a.getAdotado()) {
                comboAnimal.addItem(a.getNome());
            }
        }
        configurarAutoCompleteComboAnimal();

        // Combo Adotante
        comboAdotante = new JComboBox<>();
        comboAdotante.setEditable(true);

        for (Adotante ad : utils.SistemaDados.adotantes) {
            comboAdotante.addItem(ad.getNome());
        }
        configurarAutoCompleteComboAdotante();

        // Data

        JPanel gpData = criarCampoData("Data da adoção");
        campoData = (JTextField) gpData.getClientProperty("field");

        form.add(
            criarGrupoCombo(
                "Animal disponível",
                comboAnimal
            )
        );

        form.add(Box.createVerticalStrut(20));

        form.add(
            criarGrupoCombo(
                "Adotante",
                comboAdotante
            )
        );

        form.add(Box.createVerticalStrut(20));

        form.add(gpData);

        return form;
    }


    // ── CAMPO DE DATA COM MÁSCARA DD/MM/AAAA ─────────────────────────────────
    private JPanel criarCampoData(String rotulo) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblErro = new JLabel("Data inválida");
        lblErro.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblErro.setForeground(VERMELHO);
        lblErro.setAlignmentX(LEFT_ALIGNMENT);
        lblErro.setVisible(false);

        JPanel campoPainel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRANCO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CINZA_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        campoPainel.setOpaque(false);
        campoPainel.setMaximumSize(new Dimension(220, 44));
        campoPainel.setPreferredSize(new Dimension(220, 44));
        campoPainel.setAlignmentX(LEFT_ALIGNMENT);

        JTextField campoData = new JTextField("DD/MM/AAAA");
        campoData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoData.setForeground(CINZA_TEXTO);
        campoData.setBorder(new EmptyBorder(10, 14, 10, 8));
        campoData.setOpaque(false);

        campoData.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { e.consume(); return; }
                String atual = campoData.getText();
                if (atual.equals("DD/MM/AAAA")) {
                    campoData.setText("");
                    campoData.setForeground(Color.BLACK);
                    atual = "";
                }
                if (atual.replace("/", "").length() >= 8) { e.consume(); return; }
                SwingUtilities.invokeLater(() -> {
                    String texto = campoData.getText().replace("/", "");
                    StringBuilder f = new StringBuilder();
                    for (int i = 0; i < texto.length(); i++) {
                        if (i == 2 || i == 4) f.append("/");
                        f.append(texto.charAt(i));
                    }
                    campoData.setText(f.toString());
                    campoData.setCaretPosition(campoData.getText().length());
                });
            }
        });

        campoData.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (campoData.getText().equals("DD/MM/AAAA")) {
                    campoData.setText("");
                    campoData.setForeground(Color.BLACK);
                }
                lblErro.setVisible(false);
            }
            public void focusLost(FocusEvent e) {
                String texto = campoData.getText();
                if (texto.isEmpty()) {
                    campoData.setText("DD/MM/AAAA");
                    campoData.setForeground(CINZA_TEXTO);
                    lblErro.setVisible(false);
                } else if (!validarData(texto)) {
                    lblErro.setVisible(true);
                } else {
                    lblErro.setVisible(false);
                }
            }
        });

        JLabel icone = new JLabel("📅");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        icone.setBorder(new EmptyBorder(0, 6, 0, 12));

        campoPainel.add(campoData, BorderLayout.CENTER);
        campoPainel.add(icone,     BorderLayout.EAST);

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(6));
        grupo.add(campoPainel);
        grupo.add(Box.createVerticalStrut(4));
        grupo.add(lblErro);
        grupo.putClientProperty("field", campoData);
        return grupo;
    }

    private boolean validarData(String texto) {
        if (texto.length() != 10) return false;
        try {
            int dia = Integer.parseInt(texto.substring(0, 2));
            int mes = Integer.parseInt(texto.substring(3, 5));
            int ano = Integer.parseInt(texto.substring(6, 10));
            if (mes < 1 || mes > 12) return false;
            if (dia < 1 || dia > diasNoMes(mes, ano)) return false;
            if (ano < 1900 || ano > 2100) return false;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int diasNoMes(int mes, int ano) {
        switch (mes) {
            case 2: return (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }

    // ── BOTÕES ────────────────────────────────────────────────────────────────
    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        painel.setBackground(FUNDO);
        painel.setBorder(new MatteBorder(1, 0, 0, 0, CINZA_BORDA));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(238, 238, 238));
        btnCancelar.setForeground(new Color(60, 60, 60));
        btnCancelar.setBorder(new CompoundBorder(
            new LineBorder(CINZA_BORDA, 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("Confirmar Adoção") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? VERDE_HOVER : VERDE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmar.setForeground(BRANCO);
        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
        String nomeAnimal =
                comboAnimal.getSelectedItem() != null
                ? comboAnimal.getSelectedItem().toString().trim()
                : "";

        String nomeAdotante =
                comboAdotante.getSelectedItem() != null
                ? comboAdotante.getSelectedItem().toString().trim()
                : "";

            //Data    
            String strData = campoData.getText().trim();

            if (nomeAnimal.isEmpty() || nomeAnimal.equals("Buscar animal disponível...")) {
                JOptionPane.showMessageDialog(this, "Por favor, especifique o animal!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nomeAdotante.isEmpty() || nomeAdotante.equals("Buscar adotante cadastrado...")) {
                JOptionPane.showMessageDialog(this, "Por favor, especifique o adotante!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (strData.isEmpty() || strData.equals("DD/MM/AAAA")) {
                JOptionPane.showMessageDialog(this, "Por favor, especifique a data da adoção!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find animal
            Animal animal = null;
            for (Animal a : utils.SistemaDados.animais) {
                if (a.getNome().equalsIgnoreCase(nomeAnimal)) {
                    animal = a;
                    break;
                }
            }
            if (animal == null) {
                JOptionPane.showMessageDialog(this, "Animal \"" + nomeAnimal + "\" não encontrado no sistema!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find adopter
            Adotante adotante = null;
            for (Adotante ad : utils.SistemaDados.adotantes) {
                if (ad.getNome().equalsIgnoreCase(nomeAdotante) || ad.getCpf().equalsIgnoreCase(nomeAdotante)) {
                    adotante = ad;
                    break;
                }
            }
            if (adotante == null) {
                JOptionPane.showMessageDialog(this, "Adotante \"" + nomeAdotante + "\" não encontrado no sistema!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse Date
            Date dataAdoacao;
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                dataAdoacao = sdf.parse(strData);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mark animal as adopted
            animal.setaAotado(true);

            // Generate ID
            int nextNum = utils.SistemaDados.adocoes.size() + 1;
            String newId = String.format("ADO-%04d", nextNum);

            // Create adoption
            Adoacao adocao = new Adoacao(
                newId,
                dataAdoacao,
                adotante,
                animal,
                false,
                null,
                null,
                "Ana Costa",
                "comprovante_" + newId + ".pdf",
                ""
            );

            utils.SistemaDados.adocoes.add(adocao);
            utils.SistemaDados.save();

            JOptionPane.showMessageDialog(this,
                "Adoção confirmada com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (refreshCallback != null) {
                refreshCallback.run();
            }
            dispose();
        });
   
        painel.add(btnCancelar);
        painel.add(btnConfirmar);
        return painel;
    }

    // CAMPO COMPO BUSCA -------------

private JPanel criarGrupoCombo(
        String titulo,
        JComboBox<String> combo) {

    JPanel grupo = new JPanel();
    grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
    grupo.setOpaque(false);

    JLabel lbl = new JLabel(titulo);
    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    combo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 40));

    grupo.add(lbl);
    grupo.add(Box.createVerticalStrut(6));
    grupo.add(combo);

    return grupo;
} 

private void configurarAutoCompleteComboAnimal() {

    JTextField editor =
        (JTextField) comboAnimal.getEditor().getEditorComponent();

    editor.addKeyListener(new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent e) {

            SwingUtilities.invokeLater(() -> {

                String texto = editor.getText();

                comboAnimal.removeAllItems();

                for (Animal a : utils.SistemaDados.animais) {

                    if (!a.getAdotado()
                            && a.getNome().toLowerCase()
                            .contains(texto.toLowerCase())) {

                        comboAnimal.addItem(a.getNome());
                    }
                }

                editor.setText(texto);

                comboAnimal.showPopup();
            });
        }
    });
}

private void configurarAutoCompleteComboAdotante() {

    JTextField editor =
        (JTextField) comboAdotante.getEditor().getEditorComponent();

    editor.addKeyListener(new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent e) {

            SwingUtilities.invokeLater(() -> {

                String texto = editor.getText();

                comboAdotante.removeAllItems();

                for (Adotante ad : utils.SistemaDados.adotantes) {

                    if (ad.getNome().toLowerCase()
                            .contains(texto.toLowerCase())) {

                        comboAdotante.addItem(ad.getNome());
                    }
                }

                editor.setText(texto);

                comboAdotante.showPopup();
            });
        }
    });
}

}