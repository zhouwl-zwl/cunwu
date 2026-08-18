import cv2
import numpy as np
from PIL import Image, ImageEnhance

def enhance_image(input_path, output_path):
    # Read image using OpenCV
    img = cv2.imread(input_path)
    
    if img is None:
        print(f"Error: Could not read image from {input_path}")
        return False
    
    # Convert to RGB
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    
    # Apply Gaussian blur to reduce noise
    blurred = cv2.GaussianBlur(img_rgb, (1, 1), 0)
    
    # Apply sharpening
    kernel = np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]])
    sharpened = cv2.filter2D(blurred, -1, kernel)
    
    # Enhance contrast and sharpness using PIL
    pil_img = Image.fromarray(sharpened)
    
    # Enhance sharpness
    sharpness_enhancer = ImageEnhance.Sharpness(pil_img)
    img_sharp = sharpness_enhancer.enhance(2.0)
    
    # Enhance contrast
    contrast_enhancer = ImageEnhance.Contrast(img_sharp)
    img_contrast = contrast_enhancer.enhance(1.2)
    
    # Enhance brightness
    brightness_enhancer = ImageEnhance.Brightness(img_contrast)
    img_final = brightness_enhancer.enhance(1.05)
    
    # Save the enhanced image
    img_final.save(output_path)
    print(f"Enhanced image saved to: {output_path}")
    return True

if __name__ == "__main__":
    input_image = r"D:\tup\5beed0e124a649d0fc61ccf752662eb7.jpg"
    output_image = r"d:\cunwu\mobile-h5\public\banner.jpg"
    
    success = enhance_image(input_image, output_image)
    
    if success:
        print("✅ Image enhancement completed successfully!")
    else:
        print("❌ Image enhancement failed.")