import SwiftUI
import SeaGlassSample

// Add the framework produced on macOS to an iOS Xcode app target.
struct SeaGlassView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
